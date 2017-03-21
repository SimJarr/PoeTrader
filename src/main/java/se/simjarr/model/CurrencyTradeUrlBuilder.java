package se.simjarr.model;

import java.util.Set;

public class CurrencyTradeUrlBuilder {

    private StringBuilder builder;

    public CurrencyTradeUrlBuilder(String league, boolean online) {
        String rootPath = "http://currency.poe.trade/search?league=";
        builder = new StringBuilder(rootPath);
        builder.append(league);
        builder.append("&online=");
        if(online) builder.append("x");
    }

    public String build() {
        return builder.toString();
    }

    public CurrencyTradeUrlBuilder setHave(Set<CurrencyValue> haveCurrency) {
        String have = currencyToUrlStringMapper(haveCurrency);
        builder.append("&have=").append(have);
        return this;
    }

    public CurrencyTradeUrlBuilder setWant(Set<CurrencyValue> wantCurrency) {
        String want = currencyToUrlStringMapper(wantCurrency);
        builder.append("&want=").append(want);
        return this;
    }

    private String currencyToUrlStringMapper(Set<CurrencyValue> selectedCurrency) {
        StringBuilder sb = new StringBuilder();
        selectedCurrency.forEach(s -> {
            sb.append(s.getReturnVal());
            sb.append("-");
        });
        int size = sb.toString().length();
        sb.delete(size-1, size);
        return sb.toString();
    }
}
