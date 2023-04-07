package services;

import events.EntryEvent;
import events.ExitEvent;
import exceptions.membership.MembershipIsOutdatedException;
import exceptions.turnstile.UserAlreadyEnteredException;
import exceptions.turnstile.UserNotEnteredException;
import exceptions.user.NoSuchAccountException;
import model.Account;
import storage.CommandApplication;
import storage.EventStorage;

import java.time.Clock;
import java.time.LocalDateTime;

public class TurnstileService extends Service {
    private final EventStorage eventStorage;
    private final CommandApplication commandApplication;

    public TurnstileService(Clock clock, EventStorage eventStorage, CommandApplication commandApplication) {
        super(clock);
        this.eventStorage = eventStorage;
        this.commandApplication = commandApplication;
    }

    public void enter(int accountId) throws UserAlreadyEnteredException, NoSuchAccountException, MembershipIsOutdatedException {
        Account account = eventStorage.getAccount(accountId);
        LocalDateTime entryTime = LocalDateTime.now(clock);

        if (account.getMembershipEnd().isBefore(entryTime)) {
            throw new MembershipIsOutdatedException();
        }

        LocalDateTime lastEntryTime = account.getLastVisit();
        ExitEvent lastExitEvent = eventStorage.getLastExitEvent(accountId);

        if (lastEntryTime != null && (lastExitEvent == null || lastEntryTime.isAfter(lastExitEvent.getExitTime()))) {
            throw new UserAlreadyEnteredException();
        }

        EntryEvent entryEvent = commandApplication.enter(accountId, entryTime);

        eventStorage.addEntryEvent(entryEvent);
    }

    public void exit(int accountId) throws UserNotEnteredException {
        EntryEvent lastEntryEvent = eventStorage.getLastEntryEvent(accountId);
        ExitEvent lastExitEvent = eventStorage.getLastExitEvent(accountId);

        if (lastEntryEvent == null || (lastExitEvent != null
                && lastEntryEvent.getEntryTime().isBefore(lastExitEvent.getExitTime()))) {
            throw new UserNotEnteredException();
        }

        LocalDateTime exitTime = LocalDateTime.now(clock);
        ExitEvent exitEvent = commandApplication.exit(accountId, exitTime);

        eventStorage.addExitEvent(exitEvent);
    }
}
