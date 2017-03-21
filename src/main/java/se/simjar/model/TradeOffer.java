package se.simjar.model;

public class TradeOffer {

    private String username;
    private String sellCurrency;
    private String sellValue;
    private String buyCurrency;
    private String buyValue;
    private String ign;

    public TradeOffer(String username, String sellCurrency, String sellValue, String buyCurrency, String buyValue, String ign) {
        this.username = username;
        this.sellCurrency = sellCurrency;
        this.sellValue = sellValue;
        this.buyCurrency = buyCurrency;
        this.buyValue = buyValue;
        this.ign = ign;
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
}
