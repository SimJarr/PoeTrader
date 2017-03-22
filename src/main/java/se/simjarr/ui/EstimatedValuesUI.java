package se.simjarr.ui;

import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import se.simjarr.global.Currency;
import se.simjarr.model.CurrencyTradeUrlBuilder;
import se.simjarr.model.HttpRequestHandler;
import se.simjarr.model.TradeOffer;

import java.io.File;
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

    private void addEstimatedValuesSection(){
        String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/WEB-INF/images/";
        FileResource first = new FileResource(new File(basePath + REFERENCE_CURRENCY.getImgPath()));
        for (int i = 0; i <= 6; i++){
            FileResource second = new FileResource(new File(basePath + Currency.fromValue(i).getImgPath()));
            this.getComponent(0, i).setIcon(first);
            ((Label)this.getComponent(1, i)).setValue(String.valueOf(calcEstimatedValue(Currency.fromValue(i), 5)));
            this.getComponent(2, i).setIcon(second);
        }
    }

    private void generateMatrixGrid(final int columns, final int rows) {
        this.removeAllComponents();
        this.setRows(rows);
        this.setColumns(columns);

        for (int row = 0; row < this.getRows(); row++) {
            for (int col = 0; col < this.getColumns(); col++) {
                final Label child = new Label("", ContentMode.HTML);
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
        for(TradeOffer tradeOffer : trades) {
            estimatedValue += tradeOffer.getReferenceRatio();
        }

        estimatedValue /= trades.size();

        return estimatedValue;
    }

    private List<TradeOffer> fetchTrades(Currency fromCurrency, Currency toCurrency, int size) {
        CurrencyTradeUrlBuilder urlBuilder = new CurrencyTradeUrlBuilder(HC_LEGACY, true);
        String reguestUrl = urlBuilder.setHave(fromCurrency).setWant(toCurrency).build();

        return HttpRequestHandler.fetchTradesFromUrl(reguestUrl, size);
    }
}
