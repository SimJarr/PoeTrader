package se.simjarr.global;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;
import static se.simjarr.global.Cookies.*;

public class ThreadLocalVariables {

    private Map<Currency, Integer> inventory;
    private Map<Currency, Double> estimatedValues;
    private League selectedLeague;
    private String apikey;

    public ThreadLocalVariables() {
        setDefault();
        Cookie leagueCookie = getCookieByName(LEAGUE_COOKIE);
        if(leagueCookie != null) setSelectedLeague(League.fromName(leagueCookie.getValue()));
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public void setDefault() {
        inventory = null;
        estimatedValues = new HashMap<>();
        selectedLeague = League.HARDCORE_LEGACY;
        apikey = "unitegasasiona";
    }

    public void setInventory(Map<Currency, Integer> inventory) {
        this.inventory = inventory;
    }

    public void setSelectedLeague(League selectedLeague) {
        this.selectedLeague = selectedLeague;
    }

    public String getApikey() {
        return apikey;
    }

    public Map<Currency, Integer> getInventory() {
        return inventory;
    }

    public Map<Currency, Double> getEstimatedValues() {
        return estimatedValues;
    }

    public League getSelectedLeague() {
        return selectedLeague;
    }
}
