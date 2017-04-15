package se.simjarr.global;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalVariables {

    public ThreadLocalVariables() {
        setDefault();
    }

    private Map<Currency, Integer> inventory;

    private Map<Currency, Double> estimatedValues;

    private League selectedLeague;

    public void setDefault() {
        inventory = null;
        estimatedValues = new HashMap<>();
        selectedLeague = League.HARDCORE_LEGACY;
    }

    public void setInventory(Map<Currency, Integer> inventory) {
        this.inventory = inventory;
    }

    public void setEstimatedValues(Map<Currency, Double> estimatedValues) {
        this.estimatedValues = estimatedValues;
    }

    public void setSelectedLeague(League selectedLeague) {
        this.selectedLeague = selectedLeague;
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
