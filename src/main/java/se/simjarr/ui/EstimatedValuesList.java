package se.simjarr.ui;

import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import se.simjarr.global.Currency;
import se.simjarr.global.ThreadLocalVariables;
import se.simjarr.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static se.simjarr.global.GlobalVariables.*;


public class EstimatedValuesList extends GridLayout {

    private ThreadLocalVariables threadLocalVariables;

    public EstimatedValuesList() {
        threadLocalVariables = ((ApplicationUI) UI.getCurrent()).getThreadLocalVariables();
        this.addStyleName("outlined");
        this.setWidth("220px");
        this.setHeight("320px");
        this.setMargin(true);
        generateMatrixGrid(16);
        addEstimatedValuesStructure();
        fetchRatios();
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
        TradeBank.refreshTrades(HC_LEGACY, true);

        for (int i = 0; i < this.getRows(); i++) {
            if (i != REFERENCE_CURRENCY.getIntValue() - 1) {
                ratios.add(createRatio(Currency.fromValue(i + 1), 5, i));
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

    private Ratio createRatio(Currency currency, int sampleSize, int row) {
        double estimatedValue = TradeBank.estimateValue(currency, sampleSize);

        if (estimatedValue < 1) return new Ratio(currency, estimatedValue, true, row);
        else return new Ratio(currency, estimatedValue, false, row);
    }

    private void updateRatios(List<Ratio> ratios) {

        for (Ratio r : ratios) {

            String color = "#000000"; //black
            if (threadLocalVariables.getEstimatedValues().get(r.getCurrency()) != null && threadLocalVariables.getEstimatedValues().get(r.getCurrency()) > (1 / r.getRatio())) {
                if (r.isFlipped()){
                    color = "#00aa00"; //green
                } else {
                    color = "#aa0000"; //red
                }
            } else if (threadLocalVariables.getEstimatedValues().get(r.getCurrency()) != null && threadLocalVariables.getEstimatedValues().get(r.getCurrency()) < (1 / r.getRatio())){
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

            threadLocalVariables.getEstimatedValues().put(r.getCurrency(), 1 / r.getRatio());
        }
        threadLocalVariables.getEstimatedValues().put(REFERENCE_CURRENCY, 1.0);
    }
}
