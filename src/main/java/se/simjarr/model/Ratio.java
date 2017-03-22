package se.simjarr.model;

public class Ratio {

    private double ratio;
    private boolean flipped;
    private int row;

    public Ratio(double ratio, boolean flipped, int row) {
        this.ratio = ratio;
        this.flipped = flipped;
        this.row = row;
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
