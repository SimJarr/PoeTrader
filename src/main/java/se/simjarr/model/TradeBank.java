package se.simjarr.model;

import se.simjarr.global.Currency;
import se.simjarr.global.League;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static se.simjarr.global.GlobalVariables.REFERENCE_CURRENCY;


public abstract class TradeBank {

    private static List<TradeOffer> allTrades;

    public static double estimateValue(Currency currency, int sampleSize) {
        List<TradeOffer> tradeBuyCurrency = selectTrades(currency, REFERENCE_CURRENCY, null, null);
        List<TradeOffer> tradeSellCurrency = selectTrades(REFERENCE_CURRENCY, currency, null, null);
        int buySize = tradeBuyCurrency.size() < sampleSize ? tradeBuyCurrency.size() : sampleSize;
        int sellSize = tradeSellCurrency.size() < sampleSize ? tradeSellCurrency.size() : sampleSize;

        if (buySize <= 0 || sellSize <= 0) return -1;

        double buyValue = findOneWayRatio(tradeBuyCurrency, buySize);
        double sellValue = findOneWayRatio(tradeSellCurrency, sellSize);

        return (buyValue + sellValue) / 2;
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

    public static void refreshTrades(League league, boolean online) {
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

    private static double findOneWayRatio(List<TradeOffer> tradeCurrency, int size) {

        double value = 0;
        for(int i = 0; i < size; i++) {
            value += tradeCurrency.get(i).getReferenceRatio();
        }
        return value / size;
    }
}
