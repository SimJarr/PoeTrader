package se.simjarr.global;

//TODO: Add images for each currency
public enum Currency {

    ORB_OF_ALTERATION(1),
    ORB_OF_FUSING(2),
    ORB_OF_ALCHEMY(3),
    CHAOS_ORB(4),
    GEMCUTTERS_PRISM(5),
    EXALTED_ORB(6),
    CHROMATIC_ORB(7),
    JEWELLERS_ORB(8),
    ORB_OF_CHANCE(9),
    CARTOGRAPHERS_CHISEL(10),
    ORB_OF_SCOURING(11),
    BLESSED_ORB(12),
    ORB_OF_REGRET(13),
    REGAL_ORB(14),
    DIVINE_ORB(15),
    VAAL_ORB(16);

    private int intValue;

    Currency(int intValue) {
        this.intValue = intValue;
    }

    public static Currency fromValue(int value) {
        return Currency.values()[value];
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return String.valueOf(intValue);
    }
}
