package se.simjarr.ui;

import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.*;
import se.simjarr.global.Currency;
import se.simjarr.model.TradePreview;
import se.simjarr.model.TradePreviewHandler;

import java.util.ArrayList;
import java.util.List;

public class TradePreviewLayout extends HorizontalLayout {

    private OnDeleteUserOfferListener onDeleteUserOfferListener;
    private NativeSelect<String> sellCurrency;
    private NativeSelect<String> buyCurrency;
    private List<TradePreview> tradePreviews;
    private HorizontalLayout tradePreviewLayout;
    private Slider slider;

    public TradePreviewLayout() {
        addDeleteButton();
        addCurrencySelection();
    }

    private void addSlider() {
        if(slider != null) this.removeComponent(slider);
        slider = new Slider();
        slider.setMin(1);
        slider.setMax(20);
        slider.setWidth(100, Unit.PIXELS);
        slider.setOrientation(SliderOrientation.HORIZONTAL);
        slider.addValueChangeListener(valueChangeEvent -> {
            double position = slider.getValue();
            addTradePreview(tradePreviews.get((int)position-1));
        });

        this.addComponent(slider);
        this.setComponentAlignment(slider, Alignment.BOTTOM_CENTER);
    }

    private void addTradePreview(TradePreview tradePreview) {
        if(tradePreviewLayout != null) this.removeComponent(tradePreviewLayout);
        tradePreviewLayout = new HorizontalLayout();

        VerticalLayout positionLayout = new VerticalLayout();
        positionLayout.setMargin(false);
        Label positionHeader = new Label("Position");
        Label position = new Label(String.valueOf(tradePreview.getPosition()));
        positionLayout.addComponents(positionHeader, position);

        VerticalLayout valueLayout = new VerticalLayout();
        valueLayout.setMargin(false);
        Label valueHeader = new Label("Value");
        Label value = new Label(String.valueOf(tradePreview.getValue()));
        valueLayout.addComponents(valueHeader, value);

        VerticalLayout tradeDisplay = new VerticalLayout();
        tradeDisplay.setMargin(false);
        Label tradeDisplayHeader = new Label("Trade Display");
        HorizontalLayout tradeDisplayLayout = new HorizontalLayout();
        tradeDisplayLayout.setMargin(false);
        Label sellValue = new Label(String.valueOf(tradePreview.getSellValue()));
        Label colon = new Label(" : ");
        Label buyValue = new Label(String.valueOf(tradePreview.getBuyValue()));
        tradeDisplayLayout.addComponents(sellValue, colon, buyValue);
        tradeDisplay.addComponents(tradeDisplayHeader, tradeDisplayLayout);

        tradePreviewLayout.addComponents(positionLayout, valueLayout, tradeDisplay);
        this.addComponent(tradePreviewLayout);
        this.setComponentAlignment(tradePreviewLayout, Alignment.BOTTOM_CENTER);
    }

    private void addDeleteButton() {
        Button deleteButton = new Button("-");
        deleteButton.addClickListener(clickEvent -> {
            onDeleteUserOfferListener.onDeleteUserOffer(this);
        });
        this.addComponent(deleteButton);
        this.setComponentAlignment(deleteButton, Alignment.BOTTOM_CENTER);
    }

    private void addCurrencySelection() {
        List<String> allCurrencies = new ArrayList<>();
        for(Currency c : Currency.values()) {
            allCurrencies.add(c.name());
        }
        sellCurrency = new NativeSelect<>("Sell Currency", allCurrencies);
        sellCurrency.setEmptySelectionAllowed(false);
        sellCurrency.addValueChangeListener(valueChangeListener);

        buyCurrency = new NativeSelect<>("Buy Currency", allCurrencies);
        buyCurrency.setEmptySelectionAllowed(false);
        buyCurrency.addValueChangeListener(valueChangeListener);

        this.addComponents(sellCurrency, buyCurrency);
    }

    private HasValue.ValueChangeListener<String> valueChangeListener = new HasValue.ValueChangeListener<String>() {
        @Override
        public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
            if(buyCurrency.getValue() != null && sellCurrency.getValue() != null) {
                if(!buyCurrency.getValue().equals(sellCurrency.getValue())) {
                    TradePreviewHandler tradePreviewHandler = new TradePreviewHandler(getSellCurrency(), getBuyCurrency());
                    setTradePreviews(tradePreviewHandler.getTradePreviews());
                    addSlider();
                }
            }
        }
    };

    private void setTradePreviews(List<TradePreview> tradePreviews) {
        this.tradePreviews = tradePreviews;
    }

    private Currency getSellCurrency() {
        return Currency.fromName(sellCurrency.getValue());
    }

    private Currency getBuyCurrency() {
        return Currency.fromName(buyCurrency.getValue());
    }

    void setOnDeleteUserOfferListener(OnDeleteUserOfferListener onDeleteUserOfferListener) {
        this.onDeleteUserOfferListener = onDeleteUserOfferListener;
    }

    public interface OnDeleteUserOfferListener {
        void onDeleteUserOffer(TradePreviewLayout tradePreviewLayout);
    }
}
