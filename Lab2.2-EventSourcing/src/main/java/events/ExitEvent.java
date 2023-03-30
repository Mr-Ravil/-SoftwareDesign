package events;

import java.time.LocalDateTime;

public class ExitEvent implements Event {
    private int id;
    private int accountId;
    private LocalDateTime exitTime;

    public ExitEvent() {
    }

    public ExitEvent(int accountId, LocalDateTime exitTime) {
        this.accountId = accountId;
        this.exitTime = exitTime;
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

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
}
