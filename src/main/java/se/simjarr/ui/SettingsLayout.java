package se.simjarr.ui;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import se.simjarr.global.Currency;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        TextField textField = new TextField("Character name");
        Button button = new Button("Load Inventory");

        button.addClickListener(clickEvent -> {
            try {
                Connection.Response response = Jsoup.connect("http://www.pathofexile.com/api/public-stash-tabs").ignoreContentType(true).maxBodySize(0).execute();
                String json = response.body();
                JSONObject jsonObject = new JSONObject(json);

                JSONArray stashes = jsonObject.getJSONArray("stashes");
                for(int i = 0; i < stashes.length(); i++) {
                    if(stashes.getJSONObject(i).get("").equals("Slaskis")) {
                        System.out.println(stashes.getJSONObject(i).toString());
                    }
                }
            } catch (IOException | JSONException e) {
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

    private void createCookie(String name, String value) {
        Cookie currencyCookie = new Cookie(name, value);
        currencyCookie.setPath("/");
        VaadinService.getCurrentResponse().addCookie(currencyCookie);
    }

    private Cookie getCookieByName(String name) {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}
