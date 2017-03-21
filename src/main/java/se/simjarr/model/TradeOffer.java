package se.simjarr.model;


import static se.simjarr.global.GlobalVariables.REFERENCE_CURRENCY;

public class TradeOffer {

    private String username;
    private String sellCurrency;
    private String sellValue;
    private String buyCurrency;
    private String buyValue;
    private String ign;
    private String stock;
    private double referenceRatio;

    public TradeOffer(String username, String sellCurrency, String sellValue, String buyCurrency, String buyValue, String ign, String stock) {
        this.username = username;
        this.sellCurrency = sellCurrency;
        this.sellValue = sellValue;
        this.buyCurrency = buyCurrency;
        this.buyValue = buyValue;
        this.ign = ign;
        this.stock = stock;
        this.referenceRatio = calculateRatioComparedToReference();
    }

    public double getReferenceRatio(){
        return referenceRatio;
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

    //TODO: no good, we want a real one
    private double calculateRatioComparedToReference(){

        int intSellCurrency = Integer.parseInt(sellCurrency);
        int intSellValue = Integer.parseInt(sellValue);
        int intBuyCurrency = Integer.parseInt(buyCurrency);
        int intBuyValue = Integer.parseInt(buyValue);

        if (intSellCurrency != REFERENCE_CURRENCY && intBuyCurrency != REFERENCE_CURRENCY){
            return 0;
        }

        return (intSellCurrency == REFERENCE_CURRENCY) ? intBuyValue/intSellValue : intSellValue/intBuyValue;
    }
}
