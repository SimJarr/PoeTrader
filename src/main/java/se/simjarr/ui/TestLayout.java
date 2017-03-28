package se.simjarr.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import se.simjarr.global.Currency;
import se.simjarr.model.TradeBank;

public class TestLayout extends VerticalLayout {

    public TestLayout() {
        Button button = new Button();
        button.addClickListener(clickEvent -> {
            testMethod();
        });
        this.addComponent(button);
    }

    private void testMethod() {
        System.out.println(TradeBank.estimateValue(Currency.JEWELLERS_ORB, 5));
    }
}
