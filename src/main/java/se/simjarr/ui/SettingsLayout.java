package se.simjarr.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import se.simjarr.global.Currency;
import se.simjarr.global.GlobalVariables;
import se.simjarr.model.InventoryData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static se.simjarr.global.Cookies.createCookie;
import static se.simjarr.global.Cookies.getCookieByName;
import static se.simjarr.global.GlobalVariables.REFERENCE_CURRENCY;

public class SettingsLayout extends VerticalLayout {

    public SettingsLayout() {
        createCookies();
        addReferenceCurrencySelection();
        addLeagueSelection();
        addInventoryLoadSection();
    }

    private void addInventoryLoadSection() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        TextField textField = new TextField("Account Name");
        Button button = new Button("Load Inventory");

        button.addClickListener(clickEvent -> {
            try {
                Connection.Response response = Jsoup.connect("https://poe-api.herokuapp.com/currency-stash/user/" + textField.getValue()).ignoreContentType(true).maxBodySize(0).execute();
                String json = response.body();

                JsonParser jsonParser = new JsonParser();
                JsonArray jsonArray = jsonParser.parse(json).getAsJsonArray();
                InventoryData inventoryData = new InventoryData();

                jsonArray.forEach(jsonElement -> {
                    InventoryData id = new InventoryData((JsonObject) jsonElement);
                    inventoryData.merge(id);
                });
                GlobalVariables.INVENTORY = new HashMap<>(inventoryData.toMap());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        layout.addComponents(textField, button);
        this.addComponent(layout);
    }

    private void addReferenceCurrencySelection() {
        List<String> currencies = new ArrayList<>();
        for(Currency currency : Currency.values()) {
            currencies.add(currency.name());
        }
        NativeSelect<String> currencySelection = new NativeSelect<>("Select Reference Currency", currencies);
        currencySelection.setEmptySelectionAllowed(false);
        currencySelection.setSelectedItem("CHAOS_ORB");
        currencySelection.addSelectionListener(event -> {
            System.out.println(event.getValue());
            if(getCookieByName("REFERENCE_CURRENCY") == null) createCookie("REFERENCE_CURRENCY", "CHAOS_ORB");
            getCookieByName("REFERENCE_CURRENCY").setValue(event.getValue());
            REFERENCE_CURRENCY = Currency.fromName(event.getValue());
        });
        this.addComponent(currencySelection);
    }

    private void addLeagueSelection() {
        List<String> leagues = new ArrayList<>();
        leagues.add("Legacy");
        leagues.add("Hardcore Legacy");
        leagues.add("Standard");
        leagues.add("Hardcore");
        NativeSelect<String> leagueSelection = new NativeSelect<>("Select League", leagues);
        leagueSelection.setEmptySelectionAllowed(false);
        leagueSelection.setSelectedItem("Hardcore Legacy");
        this.addComponent(leagueSelection);
    }

    private void createCookies() {
        if(getCookieByName("REFERENCE_CURRENCY") == null)
            createCookie("REFERENCE_CURRENCY", "CHAOS_ORB");

        if(getCookieByName("LEAGUE") == null)
            createCookie("LEAGUE", "Hardcore+Legacy");
    }
}
