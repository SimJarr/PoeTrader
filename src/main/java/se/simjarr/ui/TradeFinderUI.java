package se.simjarr.ui;

import com.vaadin.ui.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import se.simjarr.model.CurrencyTradeUrlBuilder;
import se.simjarr.model.CurrencyValue;
import se.simjarr.model.TradeOffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static se.simjarr.global.GlobalVariables.CURRENCY;
import static se.simjarr.global.GlobalVariables.HC_LEGACY;

public class TradeFinderUI extends VerticalLayout{

    private TextArea responseText;
    private String url;

    public TradeFinderUI() {
        addHeader();
        addCurrencySelection();
        addResponseText();
    }

    private void addHeader() {
        Label header = new Label("poe-trade-application");
        this.addComponent(header);
    }

    private void addCurrencySelection() {
        HorizontalLayout formLayout = new HorizontalLayout();

        CheckBoxGroup<CurrencyValue> have = new CheckBoxGroup<>();
        have.setItems(CURRENCY);

        CheckBoxGroup<CurrencyValue> want = new CheckBoxGroup<>();
        want.setItems(CURRENCY);

        Button send = new Button("Send");
        send.addClickListener(clickEvent -> {
            setRequestUrl(have, want);
            List<TradeOffer> tradeOffers = fetchTradeOffers();

            StringBuilder responseBuilder = new StringBuilder();
            tradeOffers.forEach(x -> responseBuilder.append(x.getUsername()).append(", "));
            responseText.setValue(responseBuilder.toString());
        });

        formLayout.addComponents(have, want, send);
        this.addComponent(formLayout);
    }

    private void addResponseText() {
        responseText = new TextArea("Response");
        responseText.setEnabled(false);
        responseText.setWidth("50%");
        this.addComponent(responseText);
    }

    private void setRequestUrl(CheckBoxGroup<CurrencyValue> have, CheckBoxGroup<CurrencyValue> want) {
        CurrencyTradeUrlBuilder urlBuilder = new CurrencyTradeUrlBuilder(HC_LEGACY, true);
        urlBuilder.setHave(have.getSelectedItems()).setWant(want.getSelectedItems());
        url = urlBuilder.build();
    }

    private List<TradeOffer> fetchTradeOffers() {
        List<TradeOffer> tradeOffers = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements trades = doc.select(".displayoffer");
            trades.forEach(tradeOffer -> {
                //TODO: BeanParam ? ? ? ?
                TradeOffer offer = new TradeOffer(tradeOffer.attr("data-username"), tradeOffer.attr("data-sellcurrency"), tradeOffer.attr("data-sellvalue"),
                        tradeOffer.attr("data-buycurrency"), tradeOffer.attr("data-buyvalue"), tradeOffer.attr("data-ign"));
                tradeOffers.add(offer);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tradeOffers;
    }

}
