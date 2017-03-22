package se.simjarr.ui;

import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import se.simjarr.global.Currency;
import se.simjarr.model.CurrencyTradeUrlBuilder;
import se.simjarr.model.HttpRequestHandler;
import se.simjarr.model.TradeOffer;

import java.math.BigDecimal;
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
        generateMatrixGrid(16);
        addEstimatedValuesSection();
    }

    private void addEstimatedValuesSection() {
        String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/WEB-INF/images/";
        FileResource first = new FileResource(new File(basePath + REFERENCE_CURRENCY.getImgPath()));
        for (int i = 0; i < this.getRows(); i++) {
            if (i != REFERENCE_CURRENCY.getIntValue() - 1) {
                FileResource second = new FileResource(new File(basePath + Currency.fromValue(i + 1).getImgPath()));
                this.getComponent(0, i).setIcon(first);

                calcEstimatedValue(Currency.fromValue(i + 1), 5, i);

                ((Label) this.getComponent(2, i)).setValue(" : ");
                this.getComponent(4, i).setIcon(second);
            }

        }
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
                } else {
                    this.setComponentAlignment(child, Alignment.MIDDLE_CENTER);

                }
            }
        }
    }

    //TODO: fetch estimated value for currency compared to selected reference_currency
    private void calcEstimatedValue(Currency currency, int sampleSize, int row) {
        List<TradeOffer> trades = fetchTrades(REFERENCE_CURRENCY, currency, sampleSize);
        trades.addAll(fetchTrades(currency, REFERENCE_CURRENCY, sampleSize));

        double estimatedValue = 0;
        for (TradeOffer tradeOffer : trades) {
            estimatedValue += tradeOffer.getReferenceRatio();
        }

        estimatedValue /= trades.size();
        ratioFixerAndPrinter(estimatedValue, row);
    }

    private List<TradeOffer> fetchTrades(Currency fromCurrency, Currency toCurrency, int size) {
        CurrencyTradeUrlBuilder urlBuilder = new CurrencyTradeUrlBuilder(HC_LEGACY, true);
        String requestUrl = urlBuilder.setHave(fromCurrency).setWant(toCurrency).build();

        return HttpRequestHandler.fetchTradesFromUrl(requestUrl, size);
    }

    private void ratioFixerAndPrinter(double value, int row) {


        if (value < 1) {
            ((Label) this.getComponent(1, row)).setValue(new BigDecimal(1 / value).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
            this.setComponentAlignment(this.getComponent(1, row), Alignment.MIDDLE_RIGHT);
            ((Label) this.getComponent(3, row)).setValue("1");
            this.setComponentAlignment(this.getComponent(3, row), Alignment.MIDDLE_LEFT);

        } else {
            ((Label) this.getComponent(1, row)).setValue("1");
            this.setComponentAlignment(this.getComponent(1, row), Alignment.MIDDLE_RIGHT);
            ((Label) this.getComponent(3, row)).setValue(new BigDecimal(value).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
            this.setComponentAlignment(this.getComponent(3, row), Alignment.MIDDLE_LEFT);
        }
    }
}
