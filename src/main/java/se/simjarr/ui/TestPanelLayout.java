package se.simjarr.ui;

import com.vaadin.ui.*;
import se.simjarr.global.Currency;
import se.simjarr.global.ThreadLocalVariables;
import se.simjarr.model.CurrencyTradeUrlBuilder;
import se.simjarr.model.HttpRequestHandler;
import se.simjarr.model.TradeOffer;

import java.util.List;

import static se.simjarr.global.Currency.*;

public class TestPanelLayout extends VerticalLayout{

    private TextArea responseText;
    private String url;
    private ThreadLocalVariables threadLocalVariables;

    public TestPanelLayout() {
        threadLocalVariables = ((ApplicationUI) UI.getCurrent()).getThreadLocalVariables();
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

        CheckBoxGroup<Currency> have = new CheckBoxGroup<>();
        have.setItems(ORB_OF_ALTERATION, ORB_OF_FUSING, ORB_OF_ALCHEMY, CHAOS_ORB);

        CheckBoxGroup<Currency> want = new CheckBoxGroup<>();
        want.setItems(ORB_OF_ALTERATION, ORB_OF_FUSING, ORB_OF_ALCHEMY, CHAOS_ORB);

        Button send = new Button("Send");
        send.addClickListener(clickEvent -> {
            setRequestUrl(have, want);
            List<TradeOffer> tradeOffers = HttpRequestHandler.fetchTradesFromUrl(url);
            StringBuilder responseBuilder = new StringBuilder();
            tradeOffers.forEach(x -> responseBuilder.append(x.getUsername()).append(", "));
            //TODO this is now my testing-output
            responseText.setValue(threadLocalVariables.getEstimatedValues().toString());
        });

        formLayout.addComponents(have, want, send);
        this.addComponent(formLayout);
    }

    private void addResponseText() {
        responseText = new TextArea("Response");
        responseText.setEnabled(false);
        responseText.setWidth("100%");
        this.addComponent(responseText);
    }

    private void setRequestUrl(CheckBoxGroup<Currency> have, CheckBoxGroup<Currency> want) {
        CurrencyTradeUrlBuilder urlBuilder = new CurrencyTradeUrlBuilder(threadLocalVariables.getSelectedLeague().getUrlName(), true);
        urlBuilder.setHave(have.getSelectedItems().toArray(new Currency[have.getSelectedItems().size()]))
                  .setWant(want.getSelectedItems().toArray(new Currency[want.getSelectedItems().size()]));
        url = urlBuilder.build();
    }

}




















