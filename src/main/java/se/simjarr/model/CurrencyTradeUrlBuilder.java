package se.simjarr.model;

import se.simjarr.global.Currency;
import se.simjarr.global.League;

import java.util.Arrays;
import java.util.List;

public class CurrencyTradeUrlBuilder {

    private StringBuilder builder;
    private League league;
    private boolean online;

    public CurrencyTradeUrlBuilder(League league, boolean online) {
        this.league = league;
        this.online = online;
        String rootPath = "http://currency.poe.trade/search?league=";
        builder = new StringBuilder(rootPath);
        builder.append(league.getUrlName());
        builder.append("&online=");
        if(online) builder.append("x");
    }

    public String build() {
        return builder.toString();
    }

    public CurrencyTradeUrlBuilder reset() {
        return new CurrencyTradeUrlBuilder(league, online);
    }

    public CurrencyTradeUrlBuilder setHave(Currency... haveCurrency) {
        String have = currencyToUrlStringMapper(haveCurrency);
        builder.append("&have=").append(have);
        return this;
    }

    public CurrencyTradeUrlBuilder setWant(Currency... wantCurrency) {
        String want = currencyToUrlStringMapper(wantCurrency);
        builder.append("&want=").append(want);
        return this;
    }

    private String currencyToUrlStringMapper(Currency... selectedCurrency) {
        StringBuilder sb = new StringBuilder();
        List<Currency> list = Arrays.asList(selectedCurrency);
        list.forEach(s -> {
            sb.append(s.getStringValue());
            sb.append("-");
        });
        int size = sb.toString().length();
        if(size > 0) sb.delete(size-1,size);
        return sb.toString();
    }
}
