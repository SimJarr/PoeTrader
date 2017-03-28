package se.simjarr.model;

import se.simjarr.global.Currency;

import java.util.*;

public class TradeFinderReborn {

    private Map<Currency, Integer> availableCurrency;
    private Set<Currency> acceptableTradeCurrency;

    private Map<TradeOffer, Integer> tradeStock;
    private Set<TradeOffer> notUsable;

    private List<TradeOffer> tradeChain;
    private List<List<TradeOffer>> chainOfTradeChains;

    public TradeFinderReborn() {
        availableCurrency = new HashMap<>();
        acceptableTradeCurrency = new HashSet<>();
        tradeStock = new HashMap<>();
        notUsable = new HashSet<>();
        acceptAllCurrencies();
    }

    public void setAvailableCurrency(Map<Currency, Integer> availableCurrency) {
        this.availableCurrency = availableCurrency;
    }

    private void acceptAllCurrencies() {
        acceptableTradeCurrency.addAll(Arrays.asList(Currency.values()));
    }

    private List<TradeOffer> generateTradeChain(Double minValue) {
        List<TradeOffer> tradeChain = new ArrayList<>();
        findValueTrades(minValue).forEach((trade, value) -> {
            Currency buyCurrency = Currency.fromValue(trade.getBuyCurrency());
            int buyValue = trade.getBuyValue();
            if (availableCurrency.get(buyCurrency) >= buyValue && !notUsable.contains(trade)) {
                performTrade(trade);
                tradeChain.add(trade);
            }
            //TODO add "advanced search" meaning find nice trades we don't have currencies for (yet)
            /*else if (advancedSearch && value > minValue * 2 && !notUsable.contains(trade)) {
                List<TradeOffer> highValueTradeChain = highValueTradeChain(trade, minValue);
                if (highValueTradeChain != null) {
                    highValueTradeChain.forEach(this::performTrade);
                    tradeChain.addAll(highValueTradeChain);
                }
            }*/
        });
        return tradeChain;
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

    private void performTrade(TradeOffer trade) {
        updateTradeStock(trade);
        Currency sellCurrency = Currency.fromValue(trade.getSellCurrency());
        Currency buyCurrency = Currency.fromValue(trade.getBuyCurrency());
        updateCurrency(availableCurrency, sellCurrency, trade.getSellValue());
        updateCurrency(availableCurrency, buyCurrency, -trade.getBuyValue());
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
        int currentValue = 0;
        if (currencyMap.containsKey(currency))
            currentValue = currencyMap.get(currency);
        int newValue = currentValue + amount;
        if (newValue == 0)
            currencyMap.remove(currency);
        else
            currencyMap.put(currency, newValue);
    }
}
