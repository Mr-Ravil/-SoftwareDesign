package events;

import java.time.LocalDateTime;

public class MembershipRenewalEvent implements Event {
    private int id;
    private int accountId;
    private LocalDateTime membershipEnd;

    public MembershipRenewalEvent() {
    }

    public MembershipRenewalEvent(int accountId, LocalDateTime membershipEnd) {
        this.accountId = accountId;
        this.membershipEnd = membershipEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getMembershipEnd() {
        return membershipEnd;
    }

    public void setMembershipEnd(LocalDateTime membershipEnd) {
        this.membershipEnd = membershipEnd;
    }
}
