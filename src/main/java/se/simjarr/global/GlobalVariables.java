package se.simjarr.global;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

import static se.simjarr.global.Currency.CHAOS_ORB;

public abstract class GlobalVariables {

    public static final Currency REFERENCE_CURRENCY = CHAOS_ORB;

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
