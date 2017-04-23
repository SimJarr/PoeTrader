package se.simjarr.model;

public class TradePreview {

    private int position;
    private int sellValue;
    private int buyValue;
    private double value;

    public TradePreview(int position, int sellValue, int buyValue, double value) {
        this.position = position;
        this.sellValue = sellValue;
        this.buyValue = buyValue;
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public int getSellValue() {
        return sellValue;
    }

    public int getBuyValue() {
        return buyValue;
    }

    public double getValue() {
        return value;
    }
}
