package model;

import org.bson.Document;

public class Product {
    private String name;
    private double usdPrice;

    public Product(String name, double usdPrice) {
        this.name = name;
        this.usdPrice = usdPrice;
    }

    public Product(String name, double price, Currency currency) {
        this.name = name;
        this.usdPrice = price / currency.getRatio() * Currency.USD.getRatio();
    }

    public Product(Document doc) {
        this(doc.getString("name"), doc.getDouble("price"));
    }

    public Document toDocument() {
        return new Document("name", this.name).append("price", this.usdPrice);
    }

    public String getName() {
        return name;
    }

    public double getPrice(Currency currency) {
        return usdPrice * currency.getRatio();
    }

    public String toString(Currency currency) {
        return "Product: " +
                "name='" + name + '\'' +
                ", price=" + getPrice(currency);
    }
}
