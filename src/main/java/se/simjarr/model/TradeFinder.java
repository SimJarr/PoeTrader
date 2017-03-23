package se.simjarr.model;

import se.simjarr.global.Currency;

import java.util.*;

import static se.simjarr.global.GlobalVariables.ESTIMATED_VALUES;
import static se.simjarr.global.GlobalVariables.HC_LEGACY;

public class TradeFinder {

    private Map<Currency, Integer> availableCurrency;
    private Set<Currency> acceptableTradeCurrency;

    public TradeFinder() {
        availableCurrency = new HashMap<>();
        acceptableTradeCurrency = new HashSet<>();
        insertTestValues();
    }

    private void insertTestValues() {
        availableCurrency.put(Currency.CHAOS_ORB, 25);
        availableCurrency.put(Currency.EXALTED_ORB, 4);
        availableCurrency.put(Currency.ORB_OF_FUSING, 60);

        acceptableTradeCurrency.add(Currency.CHAOS_ORB);
        acceptableTradeCurrency.add(Currency.ORB_OF_ALCHEMY);
    }

    public Set<TradeOffer> generateTradeChain(int minProfit) {
        Set<TradeOffer> tradeChain = new HashSet<>(); // chain of trades for maximum profit
        Map<Currency, Integer> boughtCurrency = new HashMap<>(); // all currency gained from trading inside current tradechain
        findValueTrades().forEach((trade,value) -> {
            if(value >= minProfit) {
                Currency buyCurrency = Currency.fromValue(Integer.parseInt(trade.getBuyCurrency()));
                int buyValue = Integer.parseInt(trade.getBuyValue());
                if(availableCurrency.get(buyCurrency) >= buyValue) { // everything ok, perform trade
                    Currency sellCurrency = Currency.fromValue(Integer.parseInt(trade.getSellCurrency()));
                    int sellValue = Integer.parseInt(trade.getSellValue());
                    boughtCurrency.put(sellCurrency, sellValue);
                    reduceCurrency(buyCurrency, buyValue);
                }
            }
        });
        System.out.println("inventory");
        availableCurrency.forEach((k,v) -> {
            System.out.println("currency: " + k.name() + " value: " + v);
        });
        System.out.println("bought currency");
        boughtCurrency.forEach((k,v) -> {
            System.out.println("currency: " + k.name() + " value: " + v);
        });
        return tradeChain;
    }

    private void reduceCurrency(Currency currency, int amount) {
        int currentValue = availableCurrency.get(currency);
        currentValue -= amount;
        availableCurrency.put(currency, currentValue);
    }

    private Map<TradeOffer, Double> findValueTrades() {
        List<TradeOffer> trades = fetchTrades();
        Map<TradeOffer, Double> tradeValue = new HashMap<>();
        trades.forEach(tradeOffer -> {
            double value = getTradeValue(tradeOffer);
            if(value > 0)
                tradeValue.put(tradeOffer, value);
        });

        return tradeValue;
    }

    private double getTradeValue(TradeOffer tradeOffer) {
        double sellValue = Double.parseDouble(tradeOffer.getSellValue());
        double buyValue = Double.parseDouble(tradeOffer.getBuyValue());

        double sellValueAnReferenceCurrency = sellValue * ESTIMATED_VALUES.get(Currency.fromValue(Integer.valueOf(tradeOffer.getSellCurrency())));
        double buyValueAsReferenceCurrency = buyValue * ESTIMATED_VALUES.get(Currency.fromValue(Integer.valueOf(tradeOffer.getBuyCurrency())));

        return sellValueAnReferenceCurrency - buyValueAsReferenceCurrency;
    }

    private List<TradeOffer> fetchTrades() {
        CurrencyTradeUrlBuilder urlBuilder = new CurrencyTradeUrlBuilder(HC_LEGACY, true);
        urlBuilder.setHave(availableCurrency.keySet().toArray(new Currency[availableCurrency.size()]));
        urlBuilder.setWant(acceptableTradeCurrency.toArray(new Currency[acceptableTradeCurrency.size()]));
        return HttpRequestHandler.fetchTradesFromUrl(urlBuilder.build());
    }
}










