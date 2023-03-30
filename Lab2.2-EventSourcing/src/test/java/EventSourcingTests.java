import clock.MockClock;
import config.JdbcDaoContextConfiguration;
import dao.EventDao;
import dao.StatisticInMemoryDao;
import exceptions.membership.MembershipIsOutdatedException;
import exceptions.turnstile.TurnstileException;
import exceptions.turnstile.UserAlreadyEnteredException;
import exceptions.membership.MembershipException;
import exceptions.membership.MembershipNotEndedException;
import exceptions.membership.NewMembershipEndIsOutdatedException;
import exceptions.turnstile.UserNotEnteredException;
import exceptions.user.NoSuchAccountException;
import model.Account;
import model.Stats;
import org.junit.Before;
import org.junit.Test;
import services.ManagerService;
import services.ReportService;
import services.TurnstileService;
import storage.CommandApplication;
import storage.EventStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EventSourcingTests {
    private ManagerService managerService;
    private ReportService reportService;
    private TurnstileService turnstileService;
    private MockClock clock;

    @Before
    public void initServices() {
        JdbcDaoContextConfiguration configuration = new JdbcDaoContextConfiguration();
        EventDao eventDao = configuration.eventDao(configuration.dataSource());
        eventDao.dropAndCreateTables();

        EventStorage eventStorage = new EventStorage(eventDao);
        CommandApplication commandApplication = new CommandApplication(new StatisticInMemoryDao());
        clock = new MockClock();

        managerService = new ManagerService(clock, eventStorage);
        reportService = new ReportService(clock, commandApplication);
        turnstileService = new TurnstileService(clock, eventStorage, commandApplication);
    }


    @Test
    public void addAccountTest() throws NoSuchAccountException {
        Account expectedAccount = new Account("userName", "00000", LocalDateTime.now(clock));
        managerService.createAccount(expectedAccount.getName(), expectedAccount.getPhoneNumber());

        Account actualAccount = managerService.getAccount(expectedAccount.getPhoneNumber());

        assertEquals(expectedAccount, actualAccount);
    }

    @Test
    public void addSeveralAccountsTest() throws NoSuchAccountException {
        Account expectedAccount1 = new Account("userName", "00000", LocalDateTime.now(clock));
        Account expectedAccount2 = new Account("userName2", "11111", LocalDateTime.now(clock));

        managerService.createAccount(expectedAccount1.getName(), expectedAccount1.getPhoneNumber());

        managerService.createAccount(expectedAccount2.getName(), expectedAccount2.getPhoneNumber());

        Account actualAccount1 = managerService.getAccount(expectedAccount1.getPhoneNumber());
        assertEquals(expectedAccount1, actualAccount1);

        Account actualAccount2 = managerService.getAccount(expectedAccount2.getPhoneNumber());
        assertEquals(expectedAccount2, actualAccount2);
    }

    @Test
    public void NoSuchAccountTest() {
        assertThrows(NoSuchAccountException.class, () -> managerService.getAccount("00000"));
    }

    @Test
    public void renewalMembershipEventTest() throws NoSuchAccountException, MembershipException {
        Account expectedAccount = new Account("userName", "00000", LocalDateTime.now(clock));
        managerService.createAccount(expectedAccount.getName(), expectedAccount.getPhoneNumber());

        Account actualAccount = managerService.getAccount(expectedAccount.getPhoneNumber());

        assertEquals(expectedAccount, actualAccount);

        LocalDateTime newMembershipEnd = LocalDateTime.now(clock).plus(Duration.ofDays(1));
        managerService.renewalMembership(expectedAccount.getPhoneNumber(), newMembershipEnd);

        expectedAccount.setMembershipEnd(newMembershipEnd);
        actualAccount = managerService.getAccount(expectedAccount.getPhoneNumber());

        assertEquals(expectedAccount, actualAccount);
    }

    @Test
    public void newMembershipEndIsOutdatedTest() throws NoSuchAccountException {
        Account account = new Account("userName", "00000", LocalDateTime.now(clock));
        managerService.createAccount(account.getName(), account.getPhoneNumber());

        LocalDateTime newMembershipEnd = LocalDateTime.now(clock).minus(Duration.ofDays(1));

        assertThrows(NewMembershipEndIsOutdatedException.class,
                () -> managerService.renewalMembership(account.getPhoneNumber(), newMembershipEnd));

        Account actualAccount = managerService.getAccount(account.getPhoneNumber());

        assertEquals(account, actualAccount);
    }

    @Test
    public void membershipNotEndedTest() throws NoSuchAccountException, MembershipException {
        Account expectedAccount = new Account("userName", "00000", LocalDateTime.now(clock));
        managerService.createAccount(expectedAccount.getName(), expectedAccount.getPhoneNumber());

        LocalDateTime newMembershipEnd = LocalDateTime.now(clock).plus(Duration.ofDays(1));
        managerService.renewalMembership(expectedAccount.getPhoneNumber(), newMembershipEnd);

        newMembershipEnd = LocalDateTime.now(clock).plus(Duration.ofDays(2));
        managerService.renewalMembership(expectedAccount.getPhoneNumber(), newMembershipEnd);

        LocalDateTime wrongNewMembershipEnd2 = LocalDateTime.now(clock).plus(Duration.ofDays(1));
        assertThrows(MembershipNotEndedException.class,
                () -> managerService.renewalMembership(expectedAccount.getPhoneNumber(), wrongNewMembershipEnd2));


        expectedAccount.setMembershipEnd(newMembershipEnd);
        Account actualAccount = managerService.getAccount(expectedAccount.getPhoneNumber());

        assertEquals(expectedAccount, actualAccount);
    }

    @Test
    public void entryAndExitEventsTest() throws NoSuchAccountException, MembershipException, TurnstileException {
        Account account = new Account("userName", "00000"
                , LocalDateTime.now(clock), LocalDateTime.now(clock).plus(Duration.ofDays(1)));

        managerService.createAccount(account.getName(), account.getPhoneNumber());
        account.setId(managerService.getAccount(account.getPhoneNumber()).getId());
        managerService.renewalMembership(account.getPhoneNumber(), account.getMembershipEnd());

        clock.plusSeconds(1);
        assertThrows(UserNotEnteredException.class, () -> turnstileService.exit(account.getId()));

        clock.plusSeconds(1);
        turnstileService.enter(account.getId());

        clock.plusSeconds(1);
        assertThrows(UserAlreadyEnteredException.class, () -> turnstileService.enter(account.getId()));

        turnstileService.exit(account.getId());

        clock.plus(Duration.ofDays(1));
        assertThrows(MembershipIsOutdatedException.class, () -> turnstileService.enter(account.getId()));
    }

    @Test
    public void GlobalStatsTest() throws NoSuchAccountException, MembershipException, TurnstileException {
        Account account1 = new Account("userName", "00000"
                , LocalDateTime.now(clock), LocalDateTime.now(clock).plus(Duration.ofDays(10)));
        Account account2 = new Account("userName2", "11111"
                , LocalDateTime.now(clock), LocalDateTime.now(clock).plus(Duration.ofDays(10)));

        managerService.createAccount(account1.getName(), account1.getPhoneNumber());
        account1.setId(managerService.getAccount(account1.getPhoneNumber()).getId());
        managerService.renewalMembership(account1.getPhoneNumber(), account1.getMembershipEnd());

        managerService.createAccount(account2.getName(), account2.getPhoneNumber());
        account2.setId(managerService.getAccount(account2.getPhoneNumber()).getId());
        managerService.renewalMembership(account2.getPhoneNumber(), account2.getMembershipEnd());


        clock.plusSeconds(1);
        turnstileService.enter(account1.getId());

        clock.plusSeconds(10);
        turnstileService.exit(account1.getId());

        assertEquals(1, reportService.getNumberOfVisits());

        assertEquals(Duration.ofSeconds(10), reportService.getAverageDuration(LocalDateTime.now(clock)));

        turnstileService.enter(account2.getId());

        clock.plus(Duration.ofDays(3));
        turnstileService.exit(account2.getId());


        assertEquals(2, reportService.getNumberOfVisits());

        Duration expectedDuration = Duration.ofSeconds(10).plus(Duration.ofDays(3)).dividedBy(2);
        assertEquals(expectedDuration, reportService.getAverageDuration(LocalDateTime.now(clock)));
    }

    @Test
    public void dailyStatsTest() throws NoSuchAccountException, MembershipException, TurnstileException {
        Account account = new Account("userName", "00000"
                , LocalDateTime.now(clock), LocalDateTime.now(clock).plus(Duration.ofDays(10)));

        managerService.createAccount(account.getName(), account.getPhoneNumber());
        account.setId(managerService.getAccount(account.getPhoneNumber()).getId());
        managerService.renewalMembership(account.getPhoneNumber(), account.getMembershipEnd());

        clock.plus(Duration.ofDays(1)); // d1.s0 acc entered
        turnstileService.enter(account.getId());

        clock.plusSeconds(100); // d1.s100 acc exited
        turnstileService.exit(account.getId());

        clock.plus(Duration.ofDays(2)); // d3.s100 acc entered
        turnstileService.enter(account.getId());

        clock.plus(Duration.ofDays(2));


        /*
            day0 -> nobody
            day1 -> acc = 100 sec
            day2 -> nobody
            day3 -> acc1 = 1 day - 100 sec
            day4 -> acc1 = 1 day
            day5 -> acc1 = 1 day
         */
        List<Stats> expectedStats = List.of(
                new Stats(0, Duration.ZERO),
                new Stats(1, Duration.ofSeconds(100)),
                new Stats(0, Duration.ZERO),
                new Stats(1, Duration.ofDays(1).minus(Duration.ofSeconds(100))),
                new Stats(1, Duration.ofDays(1)),
                new Stats(1, Duration.ofDays(1))
        );

        List<Stats> actualStats = reportService.getStats(LocalDate.EPOCH, LocalDate.now(clock).plusDays(1));

        assertEquals(expectedStats, actualStats);
    }

    @Test
    public void dailyStatsSeveralAccountsTest() throws NoSuchAccountException, MembershipException, TurnstileException {
        Account account1 = new Account("userName", "00000"
                , LocalDateTime.now(clock), LocalDateTime.now(clock).plus(Duration.ofDays(10)));
        Account account2 = new Account("userName2", "11111"
                , LocalDateTime.now(clock), LocalDateTime.now(clock).plus(Duration.ofDays(10)));

        managerService.createAccount(account1.getName(), account1.getPhoneNumber());
        account1.setId(managerService.getAccount(account1.getPhoneNumber()).getId());
        managerService.renewalMembership(account1.getPhoneNumber(), account1.getMembershipEnd());

        managerService.createAccount(account2.getName(), account2.getPhoneNumber());
        account2.setId(managerService.getAccount(account2.getPhoneNumber()).getId());
        managerService.renewalMembership(account2.getPhoneNumber(), account2.getMembershipEnd());


        clock.plus(Duration.ofDays(1)); // d1.s0 acc1 entered
        turnstileService.enter(account1.getId());

        clock.plusSeconds(10); // d1.s10 acc2 entered
        turnstileService.enter(account2.getId());

        clock.plusSeconds(90); // d1.s100 acc1 exited
        turnstileService.exit(account1.getId());

        clock.plus(Duration.ofDays(2)); // d3.s100 acc2 exited
        turnstileService.exit(account2.getId());

        clock.plus(Duration.ofDays(1)); // d4.s100 acc1 entered
        turnstileService.enter(account1.getId());

        clock.plus(Duration.ofDays(1));


        /*
            day0 -> nobody
            day1 -> acc1 = 100 sec | acc2 = 1 day - 10 sec
            day2 -> acc2 = 1 day
            day3 -> acc2 = 100 sec
            day4 -> acc1 = acc2 = 1 day - 100 sec
            day5 -> acc1 = 1 day
         */
        List<Stats> expectedStats = List.of(
                new Stats(0, Duration.ZERO),
                new Stats(2, Duration.ofDays(1).plus(Duration.ofSeconds(90)).dividedBy(2)),
                new Stats(1, Duration.ofDays(1)),
                new Stats(1, Duration.ofSeconds(100)),
                new Stats(1, Duration.ofDays(1).minus(Duration.ofSeconds(100))),
                new Stats(1, Duration.ofDays(1))
        );

        List<Stats> actualStats = reportService.getStats(LocalDate.EPOCH, LocalDate.now(clock).plusDays(1));

        assertEquals(expectedStats, actualStats);
    }
}
