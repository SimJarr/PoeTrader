package se.simjarr.global;

import se.simjarr.model.CurrencyValue;

import java.util.HashSet;
import java.util.Set;

public abstract class GlobalVariables {

    public static final String HC_LEGACY = "Hardcore+Legacy";

    public static final Set<CurrencyValue> CURRENCY;

    static {
        CURRENCY = new HashSet<>();
        CURRENCY.add(new CurrencyValue("Chaos orb", "4"));
        CURRENCY.add(new CurrencyValue("Exalted orb", "6"));
        CURRENCY.add(new CurrencyValue("Vaal orb", "16"));
    }
}
