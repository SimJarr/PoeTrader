package se.simjarr.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import se.simjarr.global.Currency;
import se.simjarr.model.TradeFinder;
import se.simjarr.model.TradeOffer;

import java.util.List;

public class TestLayout extends VerticalLayout {

    public TestLayout() {
        Button button = new Button();
        button.addClickListener(clickEvent -> {
            testMethod();
        });
        this.addComponent(button);
    }

    private void testMethod() {
        TradeFinder tradeFinder = new TradeFinder();
        tradeFinder.setTestCurrencies();

        List<List<TradeOffer>> listList = tradeFinder.tradeChainer(0.1);

        listList.forEach(list -> {
            System.out.println("--------");
            for (TradeOffer offer : list) {
                System.out.println("Buy: " + offer.getBuyValue() + " " + Currency.fromValue(offer.getBuyCurrency()));
                System.out.println("Sell: " + offer.getSellValue() + " " + Currency.fromValue(offer.getSellCurrency()));
                System.out.println("========");
            }
            System.out.println("--------");
        });

        System.out.println(listList.size());



        /*for (TradeOffer offer : tradeFinder.generateTradeChain(0.1)){
            System.out.println("Buy: " + offer.getBuyValue() + " " + Currency.fromValue(offer.getBuyCurrency()));
            System.out.println("Sell: " + offer.getSellValue() + " " + Currency.fromValue(offer.getSellCurrency()));
            System.out.println("========");
        }*/
    }
}
