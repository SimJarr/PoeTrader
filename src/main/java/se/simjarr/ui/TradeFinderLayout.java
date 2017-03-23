package se.simjarr.ui;

import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.*;
import se.simjarr.global.Currency;
import se.simjarr.model.TradeFinder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradeFinderLayout extends VerticalLayout {

    private Map<Currency, String> currencyId;

    public TradeFinderLayout() {
        currencyId = new HashMap<>();

        addHeader();
        addCurrencySelection();
    }

    private void addHeader() {
        Label header = new Label("find multiple trades based on your available currency");
        this.addComponent(header);
    }

    private void addCurrencySelection() {
        HorizontalLayout formLayout = new HorizontalLayout();
        for(int i = 0; i < Currency.values().length; i++) {
            Currency current = Currency.fromValue(i+1);
            FileResource icon = current.getFileResource();
            Slider slider = new Slider();
            slider.setOrientation(SliderOrientation.VERTICAL);
            slider.setIcon(icon);
            slider.setWidth(40, Unit.PIXELS);
            slider.setId(UUID.randomUUID().toString());
            currencyId.put(current, slider.getId());
            formLayout.addComponent(slider);
        }
        Button sendButton = new Button("Send");
        sendButton.addClickListener(clickEvent -> {
            Map<Currency, Integer> myCurrency = new HashMap<>();
            currencyId.forEach((k,v) -> {
                int sliderValue = ((Slider) findComponentById(this, v)).getValue().intValue();
                myCurrency.put(k, sliderValue);
            });
            TradeFinder tradeFinder = new TradeFinder();
            tradeFinder.setAvailableCurrency(myCurrency);
            tradeFinder.tradeChainChainerOfHell(1, null);
        });
        this.addComponents(formLayout, sendButton);
    }

    private static Component findComponentById(HasComponents root, String id) {
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
