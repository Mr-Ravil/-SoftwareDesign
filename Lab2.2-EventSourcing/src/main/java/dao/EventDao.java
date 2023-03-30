package dao;

import events.AccountCreationEvent;
import events.EntryEvent;
import events.ExitEvent;
import events.MembershipRenewalEvent;

import java.util.List;
import java.util.Optional;

public interface EventDao {
    int addAccountCreationEvent(AccountCreationEvent event);

    Optional<AccountCreationEvent> getAccountCreationEvent(int id);

    Optional<AccountCreationEvent> getAccountCreationEventByPhoneNumber(String phoneNumber);

    int addMembershipRenewalEvent(MembershipRenewalEvent event);

    List<MembershipRenewalEvent> getMembershipRenewalEvents(int accountId);

    List<EntryEvent> getEntryEvents(int accountId);

    List<ExitEvent> getExitEvents(int accountId);

    int addEntryEvent(EntryEvent event);

    int addExitEvent(ExitEvent event);

    void dropAndCreateTables();
}
