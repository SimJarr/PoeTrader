package se.simjarr.model;

public class TradeOffer {

    private String username;
    private String sellCurrency;
    private String sellValue;
    private String buyCurrency;
    private String buyValue;
    private String ign;
    private String stock;

    public TradeOffer(String username, String sellCurrency, String sellValue, String buyCurrency, String buyValue, String ign, String stock) {
        this.username = username;
        this.sellCurrency = sellCurrency;
        this.sellValue = sellValue;
        this.buyCurrency = buyCurrency;
        this.buyValue = buyValue;
        this.ign = ign;
        this.stock = stock;
    }

    public String getUsername() {
        return username;
    }

    public String getSellCurrency() {
        return sellCurrency;
    }

    public String getSellValue() {
        return sellValue;
    }

    public String getBuyCurrency() {
        return buyCurrency;
    }

    public String getBuyValue() {
        return buyValue;
    }

    public String getIgn() {
        return ign;
    }

    public String getStock() {
        return stock;
    }
}
