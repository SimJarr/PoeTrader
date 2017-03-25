package se.simjarr.global;

import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;

import java.io.File;

public enum Currency {

    ORB_OF_ALTERATION(1, "Orb_of_Alteration.png"),
    ORB_OF_FUSING(2, "Orb_of_Fusing.png"),
    ORB_OF_ALCHEMY(3, "Orb_of_Alchemy.png"),
    CHAOS_ORB(4, "Chaos_Orb.png"),
    GEMCUTTERS_PRISM(5, "Gemcutter's_Prism.png"),
    EXALTED_ORB(6, "Exalted_Orb.png"),
    CHROMATIC_ORB(7, "Chromatic_Orb.png"),
    JEWELLERS_ORB(8, "Jeweller's_Orb.png"),
    ORB_OF_CHANCE(9, "Orb_of_Chance.png"),
    CARTOGRAPHERS_CHISEL(10, "Cartographer's_Chisel.png"),
    ORB_OF_SCOURING(11, "Orb_of_Scouring.png"),
    BLESSED_ORB(12, "Blessed_Orb.png"),
    ORB_OF_REGRET(13, "Orb_of_Regret.png"),
    REGAL_ORB(14, "Regal_Orb.png"),
    DIVINE_ORB(15, "Divine_Orb.png"),
    VAAL_ORB(16, "Vaal_Orb.png");

    private int intValue;
    private String imgPath;

    Currency(int intValue, String imgPath) {
        this.intValue = intValue;
        this.imgPath = imgPath;
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

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return String.valueOf(intValue);
    }
}
