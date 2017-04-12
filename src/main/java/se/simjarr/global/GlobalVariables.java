package se.simjarr.global;
import java.util.HashMap;
import java.util.Map;

import static se.simjarr.global.Currency.CHAOS_ORB;

public abstract class GlobalVariables {

    //TODO: create enum for leagues
    public static final String HC_LEGACY = "Hardcore+Legacy";

    public static Map<Currency, Integer> INVENTORY;

    public static Currency REFERENCE_CURRENCY;

    public static Map<Currency, Double> ESTIMATED_VALUES;

    public static void reset() {
        INVENTORY = null;
        REFERENCE_CURRENCY = CHAOS_ORB;
        ESTIMATED_VALUES = new HashMap<>();
    }
}
