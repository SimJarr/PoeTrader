package se.simjarr.model;

import se.simjarr.global.Currency;

import java.util.*;

import static se.simjarr.global.GlobalVariables.ESTIMATED_VALUES;
import static se.simjarr.global.GlobalVariables.HC_LEGACY;

public class TradeFinder {

    private Map<Currency, Integer> availableCurrency;
    private Set<Currency> acceptableTradeCurrency;

    private Map<TradeOffer, Integer> tradeStock;
    private Set<TradeOffer> notUsable;

    public TradeFinder() {
        availableCurrency = new HashMap<>();
        acceptableTradeCurrency = new HashSet<>();
        tradeStock = new HashMap<>();
        notUsable = new HashSet<>();
        insertTestValues();
    }

    public void setAvailableCurrency(Map<Currency, Integer> availableCurrency) {
        this.availableCurrency = availableCurrency;
    }

    private void insertTestValues() {
        for(int i = 0; i < Currency.values().length; i ++) {
            acceptableTradeCurrency.add(Currency.values()[i]);
        }
    }

    public List<TradeOffer> tradeChainer(double minProfit, List<TradeOffer> tradeChain) {
        List<TradeOffer> tradeChainChain = new ArrayList<>();
        if(tradeChain != null) {
            tradeChainChain.addAll(tradeChain);
        }
        boolean keepChaining;
        do {
            int lengthBefore = tradeChainChain.size();
            tradeChainChain.addAll(generateTradeChain(minProfit));
            keepChaining = tradeChainChain.size() > lengthBefore;
        }
        while(keepChaining);
        if(tradeChainChain.size() > (tradeChain != null ? tradeChain.size() : 0)) return tradeChainer(minProfit, tradeChainChain);
        return tradeChainChain;
    }

    private Set<TradeOffer> generateTradeChain(double minProfit) {
        Set<TradeOffer> tradeChain = new HashSet<>();
        Map<Currency, Integer> boughtCurrency = new HashMap<>();
        findValueTrades().forEach((trade,value) -> {
            if(value >= minProfit) {
                Currency buyCurrency = Currency.fromValue(trade.getBuyCurrency());
                int buyValue = trade.getBuyValue();
                Currency sellCurrency = Currency.fromValue(trade.getSellCurrency());
                int sellValue = trade.getSellValue();
                if(availableCurrency.get(buyCurrency) >= buyValue && buyCurrency != sellCurrency && !notUsable.contains(trade)) {
                    System.out.println("buying : " + Currency.fromValue(trade.getSellCurrency()) + " x " + trade.getSellValue() + " for " + Currency.fromValue(trade.getBuyCurrency()) + " x " + trade.getBuyValue());
                    updateTradeStock(trade);
                    updateCurrency(boughtCurrency, sellCurrency, sellValue);
                    updateCurrency(availableCurrency, buyCurrency, -buyValue);
                    tradeChain.add(trade);
                }
            }
        });
        boughtCurrency.forEach((k,v) -> updateCurrency(availableCurrency, k, v));
        return tradeChain;
    }

    private void updateTradeStock(TradeOffer trade) {
        if(trade.getStock() != -1)
            if(tradeStock.containsKey(trade)) {
                tradeStock.put(trade, tradeStock.get(trade) - trade.getSellValue());
                if(tradeStock.get(trade) == 0)
                    notUsable.add(trade);
            }
            else
                tradeStock.put(trade, trade.getStock());
        else
            notUsable.add(trade);
    }

    private double calculateInventoryValue(Map<Currency, Integer> inventory) {
        double value = 0;
        for(Map.Entry<Currency, Integer> entry : inventory.entrySet()) {
            value += entry.getValue() * ESTIMATED_VALUES.get(entry.getKey());
        }
        return value;
    }

    private void updateCurrency(Map<Currency, Integer> currencyMap, Currency currency, int amount) {
        int currentValue = 0;
        if(currencyMap.containsKey(currency))
            currentValue = currencyMap.get(currency);
        int newValue = currentValue + amount;
        currencyMap.put(currency, newValue);
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
        double sellValue = tradeOffer.getSellValue();
        double buyValue = tradeOffer.getBuyValue();

        double sellValueAsReferenceCurrency = sellValue * ESTIMATED_VALUES.get(Currency.fromValue(tradeOffer.getSellCurrency()));
        double buyValueAsReferenceCurrency = buyValue * ESTIMATED_VALUES.get(Currency.fromValue(tradeOffer.getBuyCurrency()));

        return sellValueAsReferenceCurrency - buyValueAsReferenceCurrency;
    }

    private List<TradeOffer> fetchTrades() {
        CurrencyTradeUrlBuilder urlBuilder = new CurrencyTradeUrlBuilder(HC_LEGACY, true);
        urlBuilder.setHave(availableCurrency.keySet().toArray(new Currency[availableCurrency.size()]));
        urlBuilder.setWant(acceptableTradeCurrency.toArray(new Currency[acceptableTradeCurrency.size()]));
        return HttpRequestHandler.fetchTradesFromUrl(urlBuilder.build());
    }
}










