package se.simjarr.model;

import se.simjarr.global.Currency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static se.simjarr.global.GlobalVariables.REFERENCE_CURRENCY;

public abstract class TradeBank {

    private static List<TradeOffer> allTrades;

    public static double estimateValue(Currency currency, int sampleSize) {
        List<TradeOffer> tradeBuyCurrency = selectTrades(currency, REFERENCE_CURRENCY, null, null);
        List<TradeOffer> tradeSellCurrency = selectTrades(REFERENCE_CURRENCY, currency, null, null);
        int minSize = tradeBuyCurrency.size() > tradeSellCurrency.size() ? tradeSellCurrency.size() : tradeBuyCurrency.size();
        sampleSize = minSize > sampleSize ? sampleSize : minSize;

        double value = 0;
        for(int i = 0; i < sampleSize; i++) {
            double buyValue = tradeBuyCurrency.get(i).getReferenceRatio();
            double sellValue = tradeSellCurrency.get(i).getReferenceRatio();
            value += (buyValue + sellValue) / 2;
        }
        return value / sampleSize;
    }

    public static List<TradeOffer> selectTrades(Currency buyCurrency, Currency sellCurrency, Double minValue, Integer sampleSize) {
        List<TradeOffer> trades = new ArrayList<>(allTrades);
        if(buyCurrency != null) trades.removeIf(trade -> !(Currency.fromValue(trade.getBuyCurrency()) == buyCurrency));
        if(sellCurrency != null) trades.removeIf(trade -> !(Currency.fromValue(trade.getSellCurrency()) == sellCurrency));
        if(minValue != null) trades.removeIf(trade -> trade.calculateTradeValue() < minValue);
        return (sampleSize == null) ? trades : (sampleSize < trades.size()) ? trades.subList(0, sampleSize) : trades;
    }

    public static List<TradeOffer> selectTrades(Collection<Currency> availableCurrencies, Double minValue){
        List<TradeOffer> result = new ArrayList<>();
        availableCurrencies.forEach(currency -> result.addAll(selectTrades(currency, null, minValue, null)));
        return result;
    }

    public static void refreshTrades(String league, boolean online) {
        Currency[] arr = new Currency[Currency.values().length];
        int counter = 0;
        for(Currency c : Currency.values()) {
            arr[counter] = c;
            counter++;
        }
        CurrencyTradeUrlBuilder urlBuilder = new CurrencyTradeUrlBuilder(league, online);
        String requestUrl = urlBuilder.setHave(arr).setWant(arr).build();
        allTrades = HttpRequestHandler.fetchTradesFromUrl(requestUrl);
    }
}
