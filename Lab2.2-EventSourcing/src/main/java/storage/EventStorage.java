package storage;

import dao.EventDao;
import events.AccountCreationEvent;
import events.EntryEvent;
import events.ExitEvent;
import events.MembershipRenewalEvent;
import exceptions.membership.MembershipNotEndedException;
import exceptions.user.NoSuchAccountException;
import model.Account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class EventStorage {
    private final EventDao eventDao;

    public EventStorage(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public int createAccount(String userName, String phoneNumber, LocalDateTime createdTime) {
        AccountCreationEvent event = new AccountCreationEvent(userName, phoneNumber, createdTime);
        return eventDao.addAccountCreationEvent(event);
    }

    public Account getAccount(String phoneNumber) throws NoSuchAccountException {
        return getAccount(getRawAccount(phoneNumber));
    }

    public Account getAccount(int id) throws NoSuchAccountException {
        return getAccount(getRawAccount(id));
    }

    private Account getAccount(Account rawAccount) {
        List<MembershipRenewalEvent> membershipRenewalEvents = eventDao.getMembershipRenewalEvents(rawAccount.getId());

        for (MembershipRenewalEvent membershipRenewalEvent : membershipRenewalEvents) {
            rawAccount.applyMembershipRenewalEvent(membershipRenewalEvent);
        }

        List<EntryEvent> entryEvents = eventDao.getEntryEvents(rawAccount.getId());

        for (EntryEvent entryEvent : entryEvents) {
            rawAccount.applyEntryEvent(entryEvent);
        }

        return rawAccount;
    }

    private Account getRawAccount(String phoneNumber) throws NoSuchAccountException {
        Optional<AccountCreationEvent> optionalAccountCreationEvent = eventDao.getAccountCreationEventByPhoneNumber(phoneNumber);
        return getRawAccount(optionalAccountCreationEvent);
    }

    private Account getRawAccount(int id) throws NoSuchAccountException {
        Optional<AccountCreationEvent> optionalAccountCreationEvent = eventDao.getAccountCreationEvent(id);
        return getRawAccount(optionalAccountCreationEvent);
    }

    private Account getRawAccount(Optional<AccountCreationEvent> optionalAccountCreationEvent) throws NoSuchAccountException {
        if (optionalAccountCreationEvent.isEmpty()) {
            throw new NoSuchAccountException();
        }

        AccountCreationEvent accountCreationEvent = optionalAccountCreationEvent.get();

        return new Account().applyAccountCreationEvent(accountCreationEvent);
    }

    public void renewalMembership(String phoneNumber, LocalDateTime newMembershipEnd) throws NoSuchAccountException, MembershipNotEndedException {
        Account account = getRawAccount(phoneNumber);

        List<MembershipRenewalEvent> membershipRenewalEvents = eventDao.getMembershipRenewalEvents(account.getId());

        for (MembershipRenewalEvent membershipRenewalEvent : membershipRenewalEvents) {
            account.applyMembershipRenewalEvent(membershipRenewalEvent);
        }

        if (account.getMembershipEnd() != null && account.getMembershipEnd().isAfter(newMembershipEnd)) {
            throw new MembershipNotEndedException();
        }

        MembershipRenewalEvent membershipRenewalEvent = new MembershipRenewalEvent(account.getId(), newMembershipEnd);

        eventDao.addMembershipRenewalEvent(membershipRenewalEvent);
    }

    public EntryEvent getLastEntryEvent(int accountId) {
        List<EntryEvent> entryEvents = eventDao.getEntryEvents(accountId);

        return entryEvents.isEmpty() ? null : entryEvents.get(entryEvents.size() - 1);
    }

    public ExitEvent getLastExitEvent(int accountId) {
        List<ExitEvent> exitEvents = eventDao.getExitEvents(accountId);

        return exitEvents.isEmpty() ? null : exitEvents.get(exitEvents.size() - 1);
    }

    public void addEntryEvent(EntryEvent entryEvent) {
        eventDao.addEntryEvent(entryEvent);
    }

    public void addExitEvent(ExitEvent exitEvent) {
        eventDao.addExitEvent(exitEvent);
    }
}
