package se.simjarr.ui;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import se.simjarr.global.Currency;

import java.util.ArrayList;
import java.util.List;

public class TradePreviewLayout extends HorizontalLayout {

    private OnDeleteUserOfferListener onDeleteUserOfferListener;
    private NativeSelect<String> sellCurrency;
    private NativeSelect<String> buyCurrency;

    public TradePreviewLayout() {
        addDeleteButton();
        addCurrencySelection();
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
        buyCurrency = new NativeSelect<>("Buy Currency", allCurrencies);
        sellCurrency.setEmptySelectionAllowed(false);
        buyCurrency.setEmptySelectionAllowed(false);

        this.addComponents(sellCurrency, buyCurrency);
    }

    public Currency getSellCurrency() {
        return Currency.fromName(sellCurrency.getValue());
    }

    public Currency getBuyCurrency() {
        return Currency.fromName(buyCurrency.getValue());
    }

    public void setOnDeleteUserOfferListener(OnDeleteUserOfferListener onDeleteUserOfferListener) {
        this.onDeleteUserOfferListener = onDeleteUserOfferListener;
    }

    public interface OnDeleteUserOfferListener {
        void onDeleteUserOffer(TradePreviewLayout tradePreviewLayout);
    }
}
