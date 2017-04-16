package se.simjarr.global;

public enum League {

    LEGACY("Legacy"),
    HARDCORE_LEGACY("Hardcore Legacy"),
    STANDARD("Standard"),
    HARDCORE("Hardcore");

    private String urlName;
    private String displayName;

    League(String name) {
        this.displayName = name;
        this.urlName = name.replace(" ", "+");
    }

    public static League fromDisplayName(String displayName) {
        for(League league : League.values()) {
            if(league.getDisplayName().equals(displayName)) return league;
        }
        return null;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
