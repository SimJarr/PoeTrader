package se.simjarr.ui;

import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import se.simjarr.global.Currency;
import se.simjarr.model.CurrencyTradeUrlBuilder;
import se.simjarr.model.HttpRequestHandler;
import se.simjarr.model.Ratio;
import se.simjarr.model.TradeOffer;

import java.math.BigDecimal;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static se.simjarr.global.GlobalVariables.*;


public class EstimatedValuesList extends GridLayout {

    public EstimatedValuesList() {
        this.addStyleName("outlined");
        this.setWidth("220px");
        this.setHeight("320px");
        this.setMargin(true);
        generateMatrixGrid(16);
        addEstimatedValuesStructure();
    }

    private void addEstimatedValuesStructure() {
        FileResource first = REFERENCE_CURRENCY.getFileResource();
        for (int i = 0; i < this.getRows(); i++) {
            if (i != REFERENCE_CURRENCY.getIntValue() - 1) {
                FileResource second = Currency.fromValue(i + 1).getFileResource();
                this.getComponent(0, i).setIcon(first);

                ((Label) this.getComponent(2, i)).setValue(" <b>:</b> ");

                this.getComponent(4, i).setIcon(second);
            }
        }
    }

    void fetchRatios() {
        List<Ratio> ratios = new ArrayList<>();

        for (int i = 0; i < this.getRows(); i++) {
            if (i != REFERENCE_CURRENCY.getIntValue() - 1) {
                ratios.add(calcEstimatedValue(Currency.fromValue(i + 1), 5, i));
            }
        }
        updateRatios(ratios);
    }

    private void generateMatrixGrid(final int rows) {
        this.removeAllComponents();
        this.setRows(rows);
        this.setColumns(5);
        for (int row = 0; row < this.getRows(); row++) {
            for (int col = 0; col < this.getColumns(); col++) {
                final Label child = new Label("", ContentMode.HTML);
                this.addComponent(child);
                if (col == 0 || col == 4) {
                    this.setComponentAlignment(child, Alignment.MIDDLE_LEFT);
                } else if (col == 1 || col == 3) {
                    this.getComponent(col, row).setWidth("50px");
                } else {
                    this.setComponentAlignment(child, Alignment.MIDDLE_CENTER);
                }
            }
        }
    }

    //TODO: fetch estimated value for currency compared to selected reference_currency
    private Ratio calcEstimatedValue(Currency currency, int sampleSize, int row) {
        List<TradeOffer> trades = fetchTrades(REFERENCE_CURRENCY, currency, sampleSize);
        trades.addAll(fetchTrades(currency, REFERENCE_CURRENCY, sampleSize));

        double estimatedValue = 0;
        for (TradeOffer tradeOffer : trades) {
            estimatedValue += tradeOffer.getReferenceRatio();
        }

        estimatedValue /= trades.size();
        if (estimatedValue < 1) return new Ratio(currency, estimatedValue, true, row);
        else return new Ratio(currency, estimatedValue, false, row);
    }

    private List<TradeOffer> fetchTrades(Currency fromCurrency, Currency toCurrency, int size) {
        CurrencyTradeUrlBuilder urlBuilder = new CurrencyTradeUrlBuilder(HC_LEGACY, true);
        String requestUrl = urlBuilder.setHave(fromCurrency).setWant(toCurrency).build();

        return HttpRequestHandler.fetchTradesFromUrl(requestUrl, size);
    }

    private void updateRatios(List<Ratio> ratios) {

        for (Ratio r : ratios) {

            String color = "#000000"; //black
            if (ESTIMATED_VALUES.get(r.getCurrency()) != null && ESTIMATED_VALUES.get(r.getCurrency()) > (1 / r.getRatio())) {
                if (r.isFlipped()){
                    color = "#00aa00"; //green
                } else {
                    color = "#aa0000"; //red
                }
            } else if (ESTIMATED_VALUES.get(r.getCurrency()) != null && ESTIMATED_VALUES.get(r.getCurrency()) < (1 / r.getRatio())){
                if (r.isFlipped()){
                    color = "#aa0000"; //red
                } else {
                    color = "#00aa00"; //green
                }
            }

            if (r.isFlipped()) {
                ((Label) this.getComponent(1, r.getRow())).setValue("<font color=" + color + "><b>" + new BigDecimal(1 / r.getRatio()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + "</b></font>");
                ((Label) this.getComponent(3, r.getRow())).setValue("<b>1</b>");
            } else {
                ((Label) this.getComponent(1, r.getRow())).setValue("<b>1</b>");
                ((Label) this.getComponent(3, r.getRow())).setValue("<font color=" + color + "><b>" + new BigDecimal(r.getRatio()).setScale(3, BigDecimal.ROUND_HALF_UP).toString() + "</b></font>");
            }
            this.setComponentAlignment(this.getComponent(1, r.getRow()), Alignment.MIDDLE_RIGHT);
            this.setComponentAlignment(this.getComponent(3, r.getRow()), Alignment.MIDDLE_LEFT);

            ESTIMATED_VALUES.put(r.getCurrency(), 1 / r.getRatio());
        }
        ESTIMATED_VALUES.put(REFERENCE_CURRENCY, 1.0);
    }
}
