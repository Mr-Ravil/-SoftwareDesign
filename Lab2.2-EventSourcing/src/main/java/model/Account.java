package model;

import events.AccountCreationEvent;
import events.EntryEvent;
import events.MembershipRenewalEvent;

import java.time.LocalDateTime;
import java.util.Objects;

public class Account {
    private int id;
    private String name;
    private String phoneNumber;
    private LocalDateTime createdTime;
    private LocalDateTime lastVisit;
    private LocalDateTime membershipEnd;

    public Account() {
    }

    public Account(String name, String phoneNumber, LocalDateTime createdTime) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.createdTime = createdTime;
    }

    public Account(String name, String phoneNumber, LocalDateTime createdTime, LocalDateTime membershipEnd) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.createdTime = createdTime;
        this.membershipEnd = membershipEnd;
    }

    public Account(String name, String phoneNumber, LocalDateTime createdTime, LocalDateTime lastVisit, LocalDateTime membershipEnd) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.createdTime = createdTime;
        this.lastVisit = lastVisit;
        this.membershipEnd = membershipEnd;
    }

    public Account applyAccountCreationEvent(AccountCreationEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.phoneNumber = event.getPhoneNumber();
        this.createdTime = event.getCreatedTime();
        return this;
    }

    public Account applyMembershipRenewalEvent(MembershipRenewalEvent event) {
        this.membershipEnd = event.getMembershipEnd();
        return this;
    }

    public Account applyEntryEvent(EntryEvent event) {
        this.lastVisit = event.getEntryTime();
        return this;
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

    public LocalDateTime getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(LocalDateTime lastVisit) {
        this.lastVisit = lastVisit;
    }

    public LocalDateTime getMembershipEnd() {
        return membershipEnd;
    }

    public void setMembershipEnd(LocalDateTime membershipEnd) {
        this.membershipEnd = membershipEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(name, account.name) && Objects.equals(phoneNumber, account.phoneNumber) && Objects.equals(createdTime, account.createdTime) && Objects.equals(lastVisit, account.lastVisit) && Objects.equals(membershipEnd, account.membershipEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber, createdTime, lastVisit, membershipEnd);
    }
}
