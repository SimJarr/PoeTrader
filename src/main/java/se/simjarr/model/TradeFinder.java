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
        for (int i = 0; i < Currency.values().length; i++) {
            acceptableTradeCurrency.add(Currency.values()[i]);
        }
    }

    public List<TradeOffer> tradeChainer(double minProfit, List<TradeOffer> tradeChain, boolean advancedSearch) {
        List<TradeOffer> tradeChainChain = new ArrayList<>();
        if (tradeChain != null) {
            tradeChainChain.addAll(tradeChain);
        }
        boolean keepChaining;
        do {
            int lengthBefore = tradeChainChain.size();
            tradeChainChain.addAll(generateTradeChain(minProfit, advancedSearch));
            keepChaining = tradeChainChain.size() > lengthBefore;
        }
        while (keepChaining);
        if (tradeChainChain.size() > (tradeChain != null ? tradeChain.size() : 0))
            return tradeChainer(minProfit, tradeChainChain, advancedSearch);
        return tradeChainChain;
    }

    private Set<TradeOffer> generateTradeChain(double minProfit, boolean advancedSearch) {
        Set<TradeOffer> tradeChain = new HashSet<>();
        findValueTrades().forEach((trade, value) -> {
            if (value >= minProfit) {
                Currency buyCurrency = Currency.fromValue(trade.getBuyCurrency());
                int buyValue = trade.getBuyValue();
                Currency sellCurrency = Currency.fromValue(trade.getSellCurrency());
                if (availableCurrency.get(buyCurrency) >= buyValue && buyCurrency != sellCurrency && !notUsable.contains(trade)) {
                    performTrade(trade);
                    tradeChain.add(trade);
                } else if (advancedSearch && value > minProfit * 2 && !notUsable.contains(trade)) {
                    List<TradeOffer> highValueTradeChain = highValueTradeChain(trade, minProfit);
                    if(highValueTradeChain != null) {
                        highValueTradeChain.forEach(this::performTrade);
                        tradeChain.addAll(highValueTradeChain);
                    }
                }
            }
        });
        return tradeChain;
    }

    private List<TradeOffer> highValueTradeChain(TradeOffer highValueTrade, double minProfit) {
        List<TradeOffer> tradeChain = new ArrayList<>();

        Currency requiredCurrency = Currency.fromValue(highValueTrade.getBuyCurrency());
        int requiredAmount = highValueTrade.getBuyValue();

        List<TradeOffer> potentialTrades = fetchTrades(availableCurrency.keySet(), new HashSet<>(Collections.singletonList(requiredCurrency)));
        potentialTrades.sort(TradeOffer.sortByValue);

        for(TradeOffer trade : potentialTrades) {
            int stock = (trade.getStock() == -1) ? trade.getSellValue() : trade.getStock();
            int howManyTimes = requiredAmount / trade.getSellValue();
            if (stock > requiredAmount) {
                double totalValuePerTrade = highValueTrade.calculateTradeValue() + (howManyTimes * trade.calculateTradeValue());
                double totalMinProfitPerTrade = minProfit * (howManyTimes+1);
                if (totalValuePerTrade > totalMinProfitPerTrade && availableCurrency.get(requiredCurrency) > requiredAmount) {
                    for (int i = 0; i < howManyTimes; i++) {
                        tradeChain.add(trade);
                    }
                    tradeChain.add(highValueTrade);
                    return tradeChain;
                }
            }
        }
        return null;
    }

    private void performTrade(TradeOffer trade) {
        System.out.println("buying : " + Currency.fromValue(trade.getSellCurrency()) + " x " + trade.getSellValue() + " for " + Currency.fromValue(trade.getBuyCurrency()) + " x " + trade.getBuyValue());
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

    private Map<TradeOffer, Double> findValueTrades() {
        List<TradeOffer> trades = fetchTrades(availableCurrency.keySet(), acceptableTradeCurrency);
        Map<TradeOffer, Double> tradeValue = new HashMap<>();
        trades.forEach(tradeOffer -> {
            double value = tradeOffer.calculateTradeValue();
            if (value > 0)
                tradeValue.put(tradeOffer, value);
        });

        return tradeValue;
    }

    private List<TradeOffer> fetchTrades(Set<Currency> have, Set<Currency> want) {
        CurrencyTradeUrlBuilder urlBuilder = new CurrencyTradeUrlBuilder(HC_LEGACY, true);
        urlBuilder.setHave(have.toArray(new Currency[have.size()]));
        urlBuilder.setWant(want.toArray(new Currency[want.size()]));
        return HttpRequestHandler.fetchTradesFromUrl(urlBuilder.build());
    }
}










