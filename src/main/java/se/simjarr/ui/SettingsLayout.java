package se.simjarr.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import se.simjarr.global.Currency;
import se.simjarr.global.GlobalVariables;
import se.simjarr.global.League;
import se.simjarr.global.ThreadLocalVariables;
import se.simjarr.model.InventoryData;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static se.simjarr.global.Cookies.*;
import static se.simjarr.global.GlobalVariables.*;

public class SettingsLayout extends VerticalLayout {

    private TradeFinderLayout tradeFinderLayout;
    private ThreadLocalVariables threadLocalVariables;

    public SettingsLayout() {
        threadLocalVariables = ((ApplicationUI) UI.getCurrent()).getThreadLocalVariables();
        addLeagueSelection();
        addInventoryLoadSection();
    }

    private void addInventoryLoadSection() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        TextField textField = new TextField("Account Name");
        Button button = new Button("Load Inventory");

        button.addClickListener(clickEvent -> {
            if (!textField.getValue().equals("")) {
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
                    threadLocalVariables.setInventory(new HashMap<>(inventoryData.toMap()));
                    setCurrencySection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        layout.addComponents(textField, button);
        this.addComponent(layout);
    }

    private void setCurrencySection() {
        tradeFinderLayout = ((TradeFinderLayout) ((TabSheet) this.getParent()).getTab(GlobalVariables.TRADEFINDERLAYOUT_INDEX).getComponent());
        HorizontalLayout currencySection = (HorizontalLayout) findComponentById(tradeFinderLayout, "currencySection");
        assert currencySection != null;
        currencySection.removeAllComponents();
        GridLayout inventoryLayout = new GridLayout();
        inventoryLayout.setRows(2);
        inventoryLayout.setColumns(16);
        int currentCurrency = 1;
        for (int i = 1; i <= inventoryLayout.getRows(); i++) {
            for (int j = 1; j <= inventoryLayout.getColumns(); j++) {
                Label child = new Label("", ContentMode.HTML);
                if (((i - 1) * 16 + j) % 2 == 1) {
                    child.setIcon(Currency.fromValue(currentCurrency).getFileResource());
                    currentCurrency++;
                    inventoryLayout.addComponent(child);
                    inventoryLayout.setComponentAlignment(child, Alignment.MIDDLE_LEFT);
                } else {
                    child.setValue("<b>" + threadLocalVariables.getInventory().get(Currency.fromValue(currentCurrency - 1)) + "</b>");
                    child.setWidth("50px");
                    inventoryLayout.addComponent(child);
                    inventoryLayout.setComponentAlignment(child, Alignment.MIDDLE_RIGHT);
                }
            }
        }
        currencySection.setId("currencySection");
        currencySection.addComponent(inventoryLayout);
        Button button = new Button("Reset Inventory");
        button.addClickListener(clickEvent -> {
            threadLocalVariables.setInventory(null);
            tradeFinderLayout = ((TradeFinderLayout) ((TabSheet) this.getParent()).getTab(GlobalVariables.TRADEFINDERLAYOUT_INDEX).getComponent());
            tradeFinderLayout.addCurrencySelection();
        });
        currencySection.addComponent(button);
    }

    private void addLeagueSelection() {
        List<String> leagues = new ArrayList<>();
        for(League league : League.values()) leagues.add(league.getDisplayName());
        NativeSelect<String> leagueSelection = new NativeSelect<>("Select League", leagues);
        leagueSelection.setEmptySelectionAllowed(false);

        Cookie leagueCookie = getCookieByName(LEAGUE_COOKIE);
        if(leagueCookie != null) leagueSelection.setSelectedItem(leagueCookie.getValue());

        leagueSelection.addValueChangeListener(valueChangeEvent -> {
            League selectedLeague = League.fromDisplayName(valueChangeEvent.getValue());
            if (selectedLeague != null) {
                threadLocalVariables.setSelectedLeague(selectedLeague);
                createCookie(LEAGUE_COOKIE, selectedLeague.getUrlName());
            }
        });

        this.addComponent(leagueSelection);
    }
}
