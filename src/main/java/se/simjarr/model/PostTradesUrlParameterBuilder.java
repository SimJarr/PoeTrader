package se.simjarr.model;

import se.simjarr.global.Currency;
import se.simjarr.global.League;

public class PostTradesUrlParameterBuilder {

    private StringBuilder builder;
    private League league;
    private String apikey;

    public PostTradesUrlParameterBuilder(League league, String apikey) {
        this.league = league;
        this.apikey = apikey;
        builder = new StringBuilder("league=");
        builder.append(league.getUrlName());
        builder.append("&apikey=");
        builder.append(apikey);
    }

    public String build() {
        return builder.toString();
    }

    public PostTradesUrlParameterBuilder addTrade(Currency sellCurrency, int sellValue, int buyValue, Currency buyCurrency){
        builder.append("&sell_currency=").append(sellCurrency.getUrlName()).append("&sell_value=").append(sellValue).append("&buy_value=").append(buyValue).append("&buy_currency=").append(buyCurrency.getUrlName());
        return this;
    }
}
