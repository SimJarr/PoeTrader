package se.simjarr.model;

import se.simjarr.global.Currency;

import java.util.ArrayList;
import java.util.List;

public class TradePreviewHandler {

    private List<TradeOffer> tradeOffers;
    private double firstPositionPricePerUnit;
    private Currency sellCurrency;
    private Currency buyCurrency;
    private boolean moreDecimals;

    public TradePreviewHandler(Currency sellCurrency, Currency buyCurrency) {
        tradeOffers = TradeBank.selectTrades(buyCurrency, sellCurrency, null, null);
        moreDecimals = placeholderName();
        firstPositionPricePerUnit = findFirstPositionPricePerUnit();

        this.sellCurrency = sellCurrency;
        this.buyCurrency = buyCurrency;
    }

    public List<TradePreview> getTradePreviews() {
        List<TradePreview> tradePreviews = new ArrayList<>();
        double x = (moreDecimals) ? 0.01 : 1.00;
        double increment = x / 20;
        double start = firstPositionPricePerUnit;
        double stop = start + x;

        for (double i = start; i < stop; i += increment) {
            TradePreview tradePreview;
            if (moreDecimals) {
                i = Math.round(i * 10000.0) / 10000.0;
                int buyValue = calculateSellValue(i);
                int sellValue = (int) (buyValue / i);
                TradeOffer temp = new TradeOffer(null, sellCurrency.getStringValue(), String.valueOf(sellValue), buyCurrency.getStringValue(), String.valueOf(buyValue), null, "");
                tradePreview = new TradePreview(findPosition(i), sellValue, buyValue, temp.calculateTradeValue() / buyValue * -1);
            } else {
                i = Math.round(i*100.0)/100.0;
                int sellValue = calculateSellValue(i);
                int buyValue = (int)(sellValue * i);
                TradeOffer temp = new TradeOffer(null, sellCurrency.getStringValue(), String.valueOf(sellValue), buyCurrency.getStringValue(), String.valueOf(buyValue), null, "");
                tradePreview = new TradePreview(findPosition(i), sellValue, buyValue, temp.calculateTradeValue()/sellValue * -1);
                tradePreviews.add(tradePreview);
            }
            tradePreviews.add(tradePreview);
        }
        return tradePreviews;
    }

    private int calculateSellValue(double pricePerUnit) {
        int hundredth = (int) ((pricePerUnit * 100) % 10);
        if (hundredth == 5) {
            return 20;
        } else {
            int tenth = (int) ((pricePerUnit * 10) % 10);
            if (tenth == 0 && hundredth == 0) return 1;
            if (tenth == 5) return 2;
            else {
                if (tenth % 2 == 0) return 5;
                else return 10;
            }
        }
    }

    private int findPosition(double pricePerUnit) {
        int position = 1;
        for (TradeOffer tradeOffer : tradeOffers) {
            if (pricePerUnit >= tradeOffer.getPricePerUnit()) position++;
            else break;
        }
        return position;
    }

    private double findFirstPositionPricePerUnit() {
        TradeOffer currentFirstPosition = tradeOffers.get(0);
        double currentPricePerUnit = currentFirstPosition.getPricePerUnit();

        int relevantDecimal = (moreDecimals) ? 4 : 2;
        return findNextLowerPrice(currentPricePerUnit, relevantDecimal);
    }

    private double findNextLowerPrice(double currentPricePerUnit, int relevantDecimal) {
        int currentDecimal = findNthDecimal(currentPricePerUnit, relevantDecimal);
        int nextDecimal = (currentDecimal > 5 || currentDecimal == 0) ? 5 : 0;

        String nextLowerPrice = (int) currentPricePerUnit + ".";
        for (int i = 1; i < relevantDecimal; i++) {
            nextLowerPrice += findNthDecimal(currentPricePerUnit, i);
        }
        nextLowerPrice += nextDecimal;
        return Double.parseDouble(nextLowerPrice);
    }

    // TODO: rename
    private boolean placeholderName() {
        double pricePerUnit = tradeOffers.get(0).getPricePerUnit();
        if (pricePerUnit - (int) pricePerUnit == 0) return false;
        return (findNthDecimal(pricePerUnit, 1) == 0);
    }

    private int findNthDecimal(double num, int decimal) {
        return (int) (Math.abs(num) * Math.pow(10, decimal)) % 10;
    }
}




















