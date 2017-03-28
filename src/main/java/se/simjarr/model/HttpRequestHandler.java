package se.simjarr.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class HttpRequestHandler {

    public static List<TradeOffer> fetchTradesFromUrl(String requestUrl) {
        List<TradeOffer> tradeOffers = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(requestUrl).maxBodySize(0).get();
            Elements trades = doc.select(".displayoffer");
            trades.forEach(tradeOfferElement -> {
                if (validateTrade(tradeOfferElement)) {
                    TradeOffer offer = new TradeOffer(tradeOfferElement.attr("data-username"), tradeOfferElement.attr("data-sellcurrency"), tradeOfferElement.attr("data-sellvalue"),
                            tradeOfferElement.attr("data-buycurrency"), tradeOfferElement.attr("data-buyvalue"), tradeOfferElement.attr("data-ign"), tradeOfferElement.attr("data-stock"));
                    tradeOffers.add(offer);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tradeOffers;
    }

    public static List<TradeOffer> fetchTradesFromUrl(String requestUrl, int size) {
        List<TradeOffer> trades = fetchTradesFromUrl(requestUrl);
        if (trades.size() > size) {
            trades = trades.subList(0, size);
        }
        return trades;
    }

    private static boolean validateTrade(Element tradeElement){

        if (tradeElement.attr("data-buycurrency").equals(tradeElement.attr("data-sellcurrency"))) return false;
        if (!validateValue(tradeElement.attr("data-buyvalue")) || !validateValue(tradeElement.attr("data-sellvalue"))) return false;
        return true;
    }

    private static boolean validateValue(String value) {
        if(value.equals("")) return false;
        int intValue = (int) Double.parseDouble(value);
        double doubleValue = Double.parseDouble(value);
        return !((doubleValue - intValue) > 0);
    }
}
