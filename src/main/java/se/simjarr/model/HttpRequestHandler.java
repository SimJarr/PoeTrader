package se.simjarr.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class HttpRequestHandler {

    public static List<TradeOffer> fetchTradesFromUrl(String requestUrl) {
        List<TradeOffer> tradeOffers = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(requestUrl).get();
            Elements trades = doc.select(".displayoffer");
            trades.forEach(tradeOffer -> {
                TradeOffer offer = new TradeOffer(tradeOffer.attr("data-username"), tradeOffer.attr("data-sellcurrency"), tradeOffer.attr("data-sellvalue"),
                        tradeOffer.attr("data-buycurrency"), tradeOffer.attr("data-buyvalue"), tradeOffer.attr("data-ign"), tradeOffer.attr("data-stock"));
                tradeOffers.add(offer);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tradeOffers;
    }

    public static List<TradeOffer> fetchTradesFromUrl(String requestUrl, int size) {
        List<TradeOffer> trades = fetchTradesFromUrl(requestUrl);
        if(trades.size() > size) {
            trades = trades.subList(0, size);
        }
        return trades;
    }
}
