package services;

import exceptions.membership.MembershipException;
import exceptions.membership.NewMembershipEndIsOutdatedException;
import exceptions.user.NoSuchAccountException;
import model.Account;
import storage.EventStorage;

import java.time.Clock;
import java.time.LocalDateTime;

public class ManagerService extends Service {
    private final EventStorage eventStorage;

    public ManagerService(Clock clock, EventStorage eventStorage) {
        super(clock);
        this.eventStorage = eventStorage;
    }

    public Account getAccount(String phoneNumber) throws NoSuchAccountException {
        return eventStorage.getAccount(phoneNumber);
    }

    public int createAccount(String userName, String phoneNumber) {
        return eventStorage.createAccount(userName, phoneNumber, LocalDateTime.now(clock));
    }

    public void renewalMembership(String phoneNumber, LocalDateTime newMembershipEnd) throws NoSuchAccountException, MembershipException {
        if (LocalDateTime.now(clock).isAfter(newMembershipEnd)) {
            throw new NewMembershipEndIsOutdatedException();
        }

        eventStorage.renewalMembership(phoneNumber, newMembershipEnd);
    }
}
