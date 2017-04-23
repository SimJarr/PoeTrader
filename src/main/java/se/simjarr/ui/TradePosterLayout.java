package se.simjarr.ui;

import com.vaadin.ui.*;
import se.simjarr.global.Currency;
import se.simjarr.global.ThreadLocalVariables;
import se.simjarr.model.PostTradesUrlParameterBuilder;
import se.simjarr.model.TradePreview;
import se.simjarr.model.TradePreviewHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TradePosterLayout extends VerticalLayout {

    private ThreadLocalVariables threadLocalVariables;
    private VerticalLayout userOfferContainer;
    private List<TradePreviewLayout> userOfferList;

    public TradePosterLayout() {
        threadLocalVariables = ((ApplicationUI) UI.getCurrent()).getThreadLocalVariables();
        addButton();
        userOfferList = new ArrayList<>();

        addUserOfferContainer();
        addBottomContainer();
    }

    private void addBottomContainer() {
        HorizontalLayout bottomContainer = new HorizontalLayout();

        Button addButton = new Button("+");
        addButton.addClickListener(clickEvent -> {
            addUserOffer();
        });

        Button saveButton = new Button("Save & Bump");
        saveButton.addClickListener(clickEvent -> {
            userOfferList.forEach(u -> {
            });
        });

        bottomContainer.addComponents(addButton, saveButton);
        this.addComponent(bottomContainer);
    }

    private void addUserOfferContainer() {
        userOfferContainer = new VerticalLayout();
        userOfferContainer.setMargin(false);
        this.addComponent(userOfferContainer);
    }

    public void addUserOffer(){
        TradePreviewLayout userOffer = new TradePreviewLayout();
        userOffer.setOnDeleteUserOfferListener(tradePreviewLayout -> {
            userOfferList.remove(tradePreviewLayout);
            userOfferContainer.removeComponent(tradePreviewLayout);
        });
        userOfferList.add(userOffer);
        userOfferContainer.addComponent(userOffer);
    }





















    private void addButton() {
        Button button = new Button("Test");
        button.addClickListener(clickEvent -> {
            try {

                String url = "http://currency.poe.trade/shop?league=Hardcore+Legacy";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String urlParameters = new PostTradesUrlParameterBuilder(threadLocalVariables.getSelectedLeague(), threadLocalVariables.getApikey())
                        .addTrade(Currency.CHAOS_ORB, 10, 4, Currency.CARTOGRAPHERS_CHISEL).build();

                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.addComponent(button);
    }
}




















