package stock.exchange.model;

public class Stock {
    private long id;
    private String companyName;
    private long price;
    private long amount;


    public Stock() {}

    public Stock(long id, String companyName, long price, long amount) {
        this.id = id;
        this.companyName = companyName;
        this.price = price;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

}
