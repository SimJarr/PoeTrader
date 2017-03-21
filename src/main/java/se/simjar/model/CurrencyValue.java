package se.simjar.model;

public class CurrencyValue {

    private String displayVal;
    private String returnVal;

    public CurrencyValue(String displayVal, String returnVal) {
        this.displayVal = displayVal;
        this.returnVal = returnVal;
    }

    public String getReturnVal() {
        return returnVal;
    }

    @Override
    public String toString() {
        return displayVal;
    }
}
