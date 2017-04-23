package se.simjarr.model;

import se.simjarr.global.Currency;

import java.math.BigDecimal;

public class EstimatedValuesRatio {

    private Currency currency;
    private double ratio;
    private boolean flipped;
    private int row;

    public EstimatedValuesRatio(Currency currency, double ratio, boolean flipped, int row) {
        this.currency = currency;
        this.ratio = ratio;
        this.flipped = (ratio == -1) != flipped;
        this.row = row;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getRatio() {
        return ratio;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        if (ratio == -1) return "N/A";
        if (flipped) return new BigDecimal(1 / ratio).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
        return new BigDecimal(ratio).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
    }
}
