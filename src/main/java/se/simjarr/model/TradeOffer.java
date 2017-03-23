package se.simjarr.model;

import se.simjarr.global.Currency;

import static se.simjarr.global.GlobalVariables.REFERENCE_CURRENCY;

public class TradeOffer {

    private String username;
    private int sellCurrency;
    private int sellValue;
    private int buyCurrency;
    private int buyValue;
    private String ign;
    private int stock;
    private double referenceRatio;

    public TradeOffer(String username, String sellCurrency, String sellValue, String buyCurrency, String buyValue, String ign, String stock) {
        this.username = username;
        this.sellCurrency = Integer.parseInt(sellCurrency);
        this.sellValue = (int) Double.parseDouble(sellValue);
        this.buyCurrency = Integer.parseInt(buyCurrency);
        this.buyValue = (int) Double.parseDouble(buyValue);
        this.ign = ign;
        this.stock = (stock.equals("")) ? -1 : Integer.parseInt(stock);
        this.referenceRatio = calculateRatioComparedToReference();
    }

    public String getUsername() {
        return username;
    }

    public int getSellCurrency() {
        return sellCurrency;
    }

    public int getSellValue() {
        return sellValue;
    }

    public int getBuyCurrency() {
        return buyCurrency;
    }

    public int getBuyValue() {
        return buyValue;
    }

    public String getIgn() {
        return ign;
    }

    public int getStock() {
        return stock;
    }

    public double getReferenceRatio() {
        return referenceRatio;
    }

    //TODO: no good, we want a real one
    private double calculateRatioComparedToReference() {
        if (sellCurrency != REFERENCE_CURRENCY.getIntValue() && buyCurrency != REFERENCE_CURRENCY.getIntValue()) {
            return 0;
        }
        return (sellCurrency == REFERENCE_CURRENCY.getIntValue()) ? (double)buyValue/(double)sellValue : (double)sellValue/(double)buyValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TradeOffer that = (TradeOffer) o;

        if (sellCurrency != that.sellCurrency) return false;
        if (sellValue != that.sellValue) return false;
        if (buyCurrency != that.buyCurrency) return false;
        if (buyValue != that.buyValue) return false;
        if (stock != that.stock) return false;
        if (Double.compare(that.referenceRatio, referenceRatio) != 0) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return ign != null ? ign.equals(that.ign) : that.ign == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = username != null ? username.hashCode() : 0;
        result = 31 * result + sellCurrency;
        result = 31 * result + sellValue;
        result = 31 * result + buyCurrency;
        result = 31 * result + buyValue;
        result = 31 * result + (ign != null ? ign.hashCode() : 0);
        result = 31 * result + stock;
        temp = Double.doubleToLongBits(referenceRatio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
