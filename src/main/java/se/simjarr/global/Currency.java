package se.simjarr.global;

import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;

import java.io.File;

public enum Currency {

    ORB_OF_ALTERATION(1, "Orb_of_Alteration.png", "alteration"),
    ORB_OF_FUSING(2, "Orb_of_Fusing.png", "fusing"),
    ORB_OF_ALCHEMY(3, "Orb_of_Alchemy.png", "alchemy"),
    CHAOS_ORB(4, "Chaos_Orb.png", "chaos"),
    GEMCUTTERS_PRISM(5, "Gemcutter's_Prism.png", "gcp"),
    EXALTED_ORB(6, "Exalted_Orb.png", "exalted"),
    CHROMATIC_ORB(7, "Chromatic_Orb.png", "chrome"),
    JEWELLERS_ORB(8, "Jeweller's_Orb.png", "jeweller\u0027s"),
    ORB_OF_CHANCE(9, "Orb_of_Chance.png", "chance"),
    CARTOGRAPHERS_CHISEL(10, "Cartographer's_Chisel.png", "chisel"),
    ORB_OF_SCOURING(11, "Orb_of_Scouring.png", "scouring"),
    BLESSED_ORB(12, "Blessed_Orb.png", "blessed"),
    ORB_OF_REGRET(13, "Orb_of_Regret.png", "regret"),
    REGAL_ORB(14, "Regal_Orb.png", "regal"),
    DIVINE_ORB(15, "Divine_Orb.png", "divine"),
    VAAL_ORB(16, "Vaal_Orb.png", "vaal");

    private int intValue;
    private String imgPath;
    private String lowerCaseName;

    Currency(int intValue, String imgPath, String lowerCaseName) {
        this.intValue = intValue;
        this.imgPath = imgPath;
        this.lowerCaseName = lowerCaseName;
    }

    public static Currency fromName(String name) {
        for(Currency currency : Currency.values()) {
            if(currency.name().equals(name)) return currency;
        }
        return null;
    }

    public static Currency fromValue(int value) {
        return Currency.values()[value-1];
    }

    public FileResource getFileResource() {
        String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/WEB-INF/images/";
        return new FileResource(new File(basePath + getImgPath()));
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getLowerCaseName() {
        return lowerCaseName;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return String.valueOf(intValue);
    }
}
