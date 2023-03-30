package events;

import java.time.LocalDateTime;

public class EntryEvent implements Event {
    private int id;
    private int accountId;
    private LocalDateTime entryTime;

    public EntryEvent() {
    }

    public EntryEvent(int accountId, LocalDateTime entryTime) {
        this.accountId = accountId;
        this.entryTime = entryTime;
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

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
}
