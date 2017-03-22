package se.simjarr.global;
import java.util.HashMap;
import java.util.Map;

import static se.simjarr.global.Currency.*;

public abstract class GlobalVariables {

    //TODO: create enum for leagues
    public static final String HC_LEGACY = "Hardcore+Legacy";

    public static Currency REFERENCE_CURRENCY = CHAOS_ORB;

    public static Map<Currency, Double> ESTIMATED_VALUES = new HashMap<>();
}
