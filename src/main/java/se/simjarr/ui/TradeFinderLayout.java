package se.simjarr.ui;

import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.*;
import se.simjarr.global.Currency;
import se.simjarr.model.TradeFinder;
import se.simjarr.model.TradeOffer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TradeFinderLayout extends VerticalLayout {

    private Accordion tradeDisplayLayout;
    private TextField minProfitInput;
    private Map<Currency, String> currencyId;
    private double minProfitPerTrade;

    public TradeFinderLayout() {
        tradeDisplayLayout = new Accordion();
        currencyId = new HashMap<>();
        minProfitPerTrade = 0.1;

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

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        //TODO: fix alignment
        Label label = new Label();
        label.setValue("MIN PROFIT PER TRADE:");
        label.setDescription("profit in reference currency");

        minProfitInput = new TextField();
        minProfitInput.setWidth(50, Unit.PIXELS);
        minProfitInput.setDescription("default value 0.1");

        Button sendButton = new Button("Send");
        sendButton.addClickListener(clickEvent -> {
            Map<Currency, Integer> myCurrency = new HashMap<>();
            currencyId.forEach((k,v) -> {
                int sliderValue = ((Slider) findComponentById(this, v)).getValue().intValue();
                myCurrency.put(k, sliderValue);
            });
            TradeFinder tradeFinder = new TradeFinder();
            tradeFinder.setAvailableCurrency(myCurrency);
            minProfitPerTrade = Double.parseDouble(minProfitInput.getValue());
            addTradeChainDisplay(tradeFinder.tradeChainer(minProfitPerTrade, null));
        });

        horizontalLayout.addComponents(label, minProfitInput, sendButton);
        this.addComponents(formLayout, horizontalLayout);
    }

    private void addTradeChainDisplay(List<TradeOffer> trades) {
        tradeDisplayLayout.removeAllComponents();
        AtomicInteger counter = new AtomicInteger(1);
        trades.forEach(trade -> {
            TextArea textArea = new TextArea();
            textArea.setEnabled(false);
            textArea.setWidth(100, Unit.PERCENTAGE);
            textArea.setValue(tradeToString(trade));
            tradeDisplayLayout.addTab(textArea, "TRADE " + counter.getAndIncrement());
        });
        this.addComponent(tradeDisplayLayout);
    }

    private String tradeToString(TradeOffer trade) {
        StringBuilder sb = new StringBuilder();
        sb.append("Username: ").append(trade.getUsername());
        sb.append("\n").append("You get: ").append(Currency.fromValue(trade.getSellCurrency()).name()).append(" x ").append(trade.getSellValue());
        sb.append("\n").append("You pay: ").append(Currency.fromValue(trade.getBuyCurrency()).name()).append(" x ").append(trade.getBuyValue());
        return sb.toString();
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
