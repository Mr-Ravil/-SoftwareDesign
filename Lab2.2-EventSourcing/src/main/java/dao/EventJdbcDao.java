package dao;

import events.AccountCreationEvent;
import events.EntryEvent;
import events.ExitEvent;
import events.MembershipRenewalEvent;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


public class EventJdbcDao extends JdbcDaoSupport implements EventDao {

    public EventJdbcDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    @Override
    public int addAccountCreationEvent(AccountCreationEvent event) {
        String sql = "INSERT INTO AccountCreationEvent (name, phoneNumber, createdTime) VALUES (?, ?, ?)";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().update(sql, event.getName(), event.getPhoneNumber(), Timestamp.valueOf(event.getCreatedTime()));
    }

    @Override
    public Optional<AccountCreationEvent> getAccountCreationEvent(int id) {
        String sql = "SELECT * FROM AccountCreationEvent WHERE id = ?";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(AccountCreationEvent.class), id).stream().findFirst();
    }

    @Override
    public Optional<AccountCreationEvent> getAccountCreationEventByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM AccountCreationEvent WHERE phoneNumber = ?";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(AccountCreationEvent.class), phoneNumber).stream().findFirst();
    }

    @Override
    public int addMembershipRenewalEvent(MembershipRenewalEvent event) {
        String sql = "INSERT INTO MembershipRenewalEvent (accountId, membershipEnd) VALUES (?, ?)";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().update(sql, event.getAccountId(), Timestamp.valueOf(event.getMembershipEnd()));
    }

    @Override
    public List<MembershipRenewalEvent> getMembershipRenewalEvents(int accountId) {
        String sql = "SELECT * FROM MembershipRenewalEvent WHERE accountId = ? ORDER BY id";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(MembershipRenewalEvent.class), accountId);
    }

    @Override
    public List<EntryEvent> getEntryEvents(int accountId) {
        String sql = "SELECT * FROM EntryEvent WHERE accountId = ? ORDER BY id";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(EntryEvent.class), accountId);
    }

    @Override
    public List<ExitEvent> getExitEvents(int accountId) {
        String sql = "SELECT * FROM ExitEvent WHERE accountId = ? ORDER BY id";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(ExitEvent.class), accountId);
    }

    @Override
    public int addEntryEvent(EntryEvent event) {
        String sql = "INSERT INTO EntryEvent (accountId, entryTime) VALUES (?, ?)";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().update(sql, event.getAccountId(), Timestamp.valueOf(event.getEntryTime()));
    }

    @Override
    public int addExitEvent(ExitEvent event) {
        String sql = "INSERT INTO ExitEvent (accountId, exitTime) VALUES (?, ?)";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().update(sql, event.getAccountId(), Timestamp.valueOf(event.getExitTime()));
    }

    @Override
    public void dropAndCreateTables() {
        String sqlDropAccountCreationEvent = "DROP TABLE IF EXISTS AccountCreationEvent";
        String sqlDropMembershipRenewalEvent = "DROP TABLE IF EXISTS MembershipRenewalEvent";
        String sqlDropEntryEvent = "DROP TABLE IF EXISTS EntryEvent";
        String sqlDropExitEvent = "DROP TABLE IF EXISTS ExitEvent";

        String sqlCreateAccountCreationEvent =
                "CREATE TABLE IF NOT EXISTS AccountCreationEvent" +
                        "(" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    name VARCHAR(255) NOT NULL," +
                        "    phoneNumber VARCHAR(20) UNIQUE NOT NULL," +
                        "    createdTime TIMESTAMP NOT NULL" +
                        ");";

        String sqlCreateMembershipRenewalEvent =
                "CREATE TABLE IF NOT EXISTS MembershipRenewalEvent" +
                        "(" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    accountId INTEGER," +
                        "    membershipEnd TIMESTAMP NOT NULL," +
                        "    FOREIGN KEY (accountId) REFERENCES AccountCreationEvent(accountId)" +
                        ");";

        String sqlCreateEntryEvent =
                "CREATE TABLE IF NOT EXISTS EntryEvent" +
                        "(" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    accountId INTEGER," +
                        "    entryTime TIMESTAMP NOT NULL," +
                        "    FOREIGN KEY (accountId) REFERENCES AccountCreationEvent(accountId)" +
                        ");";

        String sqlCreateExitEvent =
                "CREATE TABLE IF NOT EXISTS ExitEvent" +
                        "(" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    accountId INTEGER," +
                        "    exitTime TIMESTAMP NOT NULL," +
                        "    FOREIGN KEY (accountId) REFERENCES AccountCreationEvent(accountId)" +
                        ");";

        assert getJdbcTemplate() != null;
        // drops
        getJdbcTemplate().update(sqlDropAccountCreationEvent);
        getJdbcTemplate().update(sqlDropMembershipRenewalEvent);
        getJdbcTemplate().update(sqlDropEntryEvent);
        getJdbcTemplate().update(sqlDropExitEvent);
        // creates
        getJdbcTemplate().update(sqlCreateAccountCreationEvent);
        getJdbcTemplate().update(sqlCreateMembershipRenewalEvent);
        getJdbcTemplate().update(sqlCreateEntryEvent);
        getJdbcTemplate().update(sqlCreateExitEvent);

    }


}
