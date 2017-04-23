package se.simjarr.model;

import se.simjarr.global.Currency;

import java.util.ArrayList;
import java.util.List;

public class TradePreviewHandler {

    private List<TradeOffer> tradeOffers;
    private double firstPositionPricePerUnit;
    private Currency sellCurrency;
    private Currency buyCurrency;

    public TradePreviewHandler(Currency sellCurrency, Currency buyCurrency) {
        tradeOffers = TradeBank.selectTrades(buyCurrency, sellCurrency, null, null);
        //tradeOffers.sort(TradeOffer.sortByValue);
        this.sellCurrency = sellCurrency;
        this.buyCurrency = buyCurrency;
        firstPositionPricePerUnit = findFirstPosition();
    }

    public List<TradePreview> getTradePreviews() {
        List<TradePreview> tradePreviews = new ArrayList<>();
        for(double i = firstPositionPricePerUnit; i < firstPositionPricePerUnit + 1; i += 0.05) {

            int sellValue = calculateSellValue(i);
            int buyValue = (int)(sellValue * i);

            TradeOffer temp = new TradeOffer(null, sellCurrency.getStringValue(), String.valueOf(sellValue), buyCurrency.getStringValue(), String.valueOf(buyValue), null, "");

            TradePreview tradePreview = new TradePreview(findPosition(i), sellValue, buyValue, temp.calculateTradeValue());
            tradePreviews.add(tradePreview);
        }
        return tradePreviews;
    }

    private int calculateSellValue(double pricePerUnit) {
        int hundredth = (int)((pricePerUnit * 100) % 10);
        if(hundredth == 5) {
            return 20;
        } else {
            int tenth = (int)((pricePerUnit * 10) % 10);
            if(tenth == 0 && hundredth == 0) return 1;
            if(tenth == 5) return 2;
            else{
                if(tenth % 2 == 0) return 5;
                else return 10;
            }
        }
    }

    private int findPosition(double pricePerUnit) {
        int position = 1;
        for(TradeOffer tradeOffer : tradeOffers) {
            if(pricePerUnit >= tradeOffer.getPricePerUnit()) position++;
            else break;
        }
        return position;
    }

    private double findFirstPosition() {
        TradeOffer currentFirstPosition = tradeOffers.get(0);
        double currentPricePerUnit = currentFirstPosition.getPricePerUnit();

        return findNextLowerPrice(currentPricePerUnit);
    }

    private double findNextLowerPrice(double currentPricePerUnit) {
        int currentHundredth = (int)(currentPricePerUnit * 100) % 10;
        int nextHundredth = (currentHundredth > 5 || currentHundredth == 0) ? 5 : 0;

        double nextLowerPrice = (int)(currentPricePerUnit * 10);
        nextLowerPrice *= 10;
        nextLowerPrice -= nextHundredth;
        nextLowerPrice /= 100;

        return nextLowerPrice;
    }
}




















