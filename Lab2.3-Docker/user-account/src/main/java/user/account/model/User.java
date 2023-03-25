package user.account.model;

import java.util.List;

public class User {
    private long id;
    private String name;
    private long balance;
    private List<Portfolio> portfolios;

    public User() {}

    public User(long id, String companyName, long price, long amount) {
        this.id = id;
        this.name = companyName;
        this.balance = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }


    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }


}
