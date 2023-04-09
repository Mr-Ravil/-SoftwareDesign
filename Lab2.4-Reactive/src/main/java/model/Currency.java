package model;

public enum Currency {
    USD("USD", 1),
    EUR("EUR", 0.91),
    RUB("RUB", 81.10);

    private final String currencyCode;
    private final double ratio;

    Currency(String currencyCode, double ratio) {
        this.currencyCode = currencyCode;
        this.ratio = ratio;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public double getRatio() {
        return ratio;
    }
}
