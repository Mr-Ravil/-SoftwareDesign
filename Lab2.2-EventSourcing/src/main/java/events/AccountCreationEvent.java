package events;

import java.time.LocalDateTime;

public class AccountCreationEvent implements Event {
    private int id;
    private String name;
    private String phoneNumber;
    private LocalDateTime createdTime;

    public AccountCreationEvent() {}

    public AccountCreationEvent(String name, String phoneNumber, LocalDateTime createdTime) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.createdTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
