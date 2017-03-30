package se.simjarr.model;

import se.simjarr.global.Currency;

import java.util.*;

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
        acceptAllCurrencies();
    }

    public void setTestCurrencies() {
        Map<Currency, Integer> map = new HashMap<>();
        for (Currency c : Currency.values()) {
            map.put(c, 0);
        }
        map.put(Currency.EXALTED_ORB, 10);
        setAvailableCurrency(map);
    }

    public void setAvailableCurrency(Map<Currency, Integer> availableCurrency) {
        this.availableCurrency = availableCurrency;
    }

    private void acceptAllCurrencies() {
        acceptableTradeCurrency.addAll(Arrays.asList(Currency.values()));
    }

    public List<List<TradeOffer>> tradeChainer(Double minValue) {
        List<List<TradeOffer>> chainOfTradeChains = new ArrayList<>();
        boolean keepChaining;
        do {
            int lengthBefore = chainOfTradeChains.size();
            List<TradeOffer> result = generateTradeChain(minValue);
            if (result.size() != 0) chainOfTradeChains.add(result);
            keepChaining = chainOfTradeChains.size() > lengthBefore;
        }
        while (keepChaining);
        return chainOfTradeChains;
    }

    public List<TradeOffer> generateTradeChain(Double minValue) {
        List<TradeOffer> tradeChain = new ArrayList<>();
        findValueTrades(minValue).forEach((trade, value) -> {
            Currency buyCurrency = Currency.fromValue(trade.getBuyCurrency());
            int buyValue = trade.getBuyValue();
            if (availableCurrency.get(buyCurrency) >= buyValue && !notUsable.contains(trade)) {
                tradeChain.add(trade);
            }
            //TODO add "advanced search" meaning find nice trades we don't have currencies for (yet)
        });
        tradeChain.sort(TradeOffer.sortByValue);
        return validateAndPerformTrades(tradeChain);
    }

    private Map<TradeOffer, Double> findValueTrades(Double minValue) {
        List<TradeOffer> trades = TradeBank.selectTrades(availableCurrency.keySet(), minValue);
        Map<TradeOffer, Double> tradeValue = new HashMap<>();
        trades.forEach(tradeOffer -> {
            double value = tradeOffer.calculateTradeValue();
            if (value > 0)
                tradeValue.put(tradeOffer, value);
        });
        return tradeValue;
    }

    private List<TradeOffer> validateAndPerformTrades(List<TradeOffer> trades) {
        List<TradeOffer> list = new ArrayList<>();
        for (TradeOffer trade : trades) {
            Currency sellCurrency = Currency.fromValue(trade.getSellCurrency());
            Currency buyCurrency = Currency.fromValue(trade.getBuyCurrency());
            int buyValue = trade.getBuyValue();
            int sellValue = trade.getSellValue();
            if (availableCurrency.get(buyCurrency) >= buyValue) {
                do {
                    list.add(trade);
                    updateTradeStock(trade);
                    updateCurrency(availableCurrency, sellCurrency, trade.getSellValue());
                    updateCurrency(availableCurrency, buyCurrency, -trade.getBuyValue());
                } while (!notUsable.contains(trade) && availableCurrency.get(buyCurrency) >= buyValue);
            }
        }
        return list;
    }

    private void updateTradeStock(TradeOffer trade) {
        if (trade.getStock() != -1) {
            if (tradeStock.containsKey(trade))
                tradeStock.put(trade, tradeStock.get(trade) - trade.getSellValue());
            else
                tradeStock.put(trade, trade.getStock() - trade.getSellValue());
            if (tradeStock.get(trade) < trade.getSellValue())
                notUsable.add(trade);
        } else
            notUsable.add(trade);
    }

    private void updateCurrency(Map<Currency, Integer> currencyMap, Currency currency, int amount) {
        int currentValue = currencyMap.get(currency);
        int newValue = currentValue + amount;
        currencyMap.put(currency, newValue);
    }
}
