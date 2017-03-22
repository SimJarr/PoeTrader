package se.simjarr.ui;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import se.simjarr.global.Currency;
import se.simjarr.model.CurrencyTradeUrlBuilder;
import se.simjarr.model.HttpRequestHandler;
import se.simjarr.model.TradeOffer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import static se.simjarr.global.GlobalVariables.HC_LEGACY;
import static se.simjarr.global.GlobalVariables.REFERENCE_CURRENCY;


public class EstimatedValuesUI extends GridLayout {


    public EstimatedValuesUI() {
        this.addStyleName("outlined");
        this.setWidth("220px");
        this.setHeight("320px");
        this.setMargin(true);
        generateMatrixGrid(3, 7);

        addEstimatedValuesSection();
    }

    private void addEstimatedValuesSection() {
        for (int i = 0; i <= 6; i++) {
            ((Label) this.getComponent(0, i)).setValue("<img style='width:30px; height:30px;'src='http://currency.poe.trade/static/currency/Chaos_Orb.png'/>");
            if (i != REFERENCE_CURRENCY.getIntValue() - 1) {
                ((Label) this.getComponent(1, i)).setValue(String.valueOf(calcEstimatedValue(Currency.fromValue(i), 5)));
            }
            ((Label) this.getComponent(2, i)).setValue("<img style='width:30px; height:30px;'src='http://currency.poe.trade/static/currency/Orb_of_Alteration.png'/>");
        }
    }

    private void generateMatrixGrid(final int columns, final int rows) {
        this.removeAllComponents();
        this.setRows(rows);
        this.setColumns(columns);

        for (int row = 0; row < this.getRows(); row++) {
            for (int col = 0; col < this.getColumns(); col++) {
                final Label child = new Label("NOP", ContentMode.HTML);
                this.addComponent(child);
                this.setComponentAlignment(child, Alignment.MIDDLE_CENTER);
                this.setRowExpandRatio(row, 0.0f);
                this.setColumnExpandRatio(col, 0.0f);
            }
        }
    }

    //TODO: fetch estimated value for currency compared to selected reference_currency
    private double calcEstimatedValue(Currency currency, int size) {
        List<TradeOffer> trades = fetchTrades(REFERENCE_CURRENCY, currency, size);
        trades.addAll(fetchTrades(currency, REFERENCE_CURRENCY, size));

        double estimatedValue = 0;
        for (TradeOffer tradeOffer : trades) {
            estimatedValue += tradeOffer.getReferenceRatio();
        }

        estimatedValue /= trades.size();
        estimatedValue = new BigDecimal(estimatedValue).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

        return estimatedValue;
    }

    private List<TradeOffer> fetchTrades(Currency fromCurrency, Currency toCurrency, int size) {
        CurrencyTradeUrlBuilder urlBuilder = new CurrencyTradeUrlBuilder(HC_LEGACY, true);
        String requestUrl = urlBuilder.setHave(fromCurrency).setWant(toCurrency).build();

        return HttpRequestHandler.fetchTradesFromUrl(requestUrl, size);
    }
}
