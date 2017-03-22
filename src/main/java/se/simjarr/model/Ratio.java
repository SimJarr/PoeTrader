package se.simjarr.model;

import se.simjarr.global.Currency;

public class Ratio {

    private Currency currency;
    private double ratio;
    private boolean flipped;
    private int row;

    public Ratio(Currency currency, double ratio, boolean flipped, int row) {
        this.currency = currency;
        this.ratio = ratio;
        this.flipped = flipped;
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
}
