package se.simjarr.global;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

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

    public static final int TESTPANELAYOUT_INDEX = 0;
    public static final int TRADEFINDERLAYOUT_INDEX = 1;
    public static final int SETTINGSLAYOUT_INDEX = 2;

    public static Component findComponentById(HasComponents root, String id) {
        for (Component child : root) {
            if (id.equals(child.getId())) {
                return child;
            } else if (child instanceof HasComponents) {
                Component result = findComponentById((HasComponents) child, id);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
