package se.simjarr.global;

import java.util.HashMap;
import java.util.Map;

import static se.simjarr.global.Currency.CHAOS_ORB;

public class ThreadLocalVariables {

    public ThreadLocalVariables() {
        setDefault();
    }

    private Map<Currency, Integer> inventory;

    private Currency referenceCurrency;

    private Map<Currency, Double> estimatedValues;

    public void setDefault() {
        inventory = null;
        referenceCurrency = CHAOS_ORB;
        estimatedValues = new HashMap<>();
    }

    public void setInventory(Map<Currency, Integer> inventory) {
        this.inventory = inventory;
    }

    public void setReferenceCurrency(Currency referenceCurrency) {
        this.referenceCurrency = referenceCurrency;
    }

    public void setEstimatedValues(Map<Currency, Double> estimatedValues) {
        this.estimatedValues = estimatedValues;
    }

    public Map<Currency, Integer> getInventory() {
        return inventory;
    }

    public Currency getReferenceCurrency() {
        return referenceCurrency;
    }

    public Map<Currency, Double> getEstimatedValues() {
        return estimatedValues;
    }
}
