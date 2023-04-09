package model;

import org.bson.Document;

public class User {
    private int id;
    private Currency currency;

    public User(int id, Currency currency) {
        this.id = id;
        this.currency = currency;
    }

    public User(Document doc) {
        this(doc.getInteger("_id"), Currency.valueOf(doc.getString("currency")));
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Document toDocument() {
        return new Document("_id", this.id).append("currency", this.currency.getCurrencyCode());
    }
}
