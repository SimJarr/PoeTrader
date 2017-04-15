package se.simjarr.global;

public enum League {

    LEGACY("Legacy"),
    HARDCORE_LEGACY("Hardcore Legacy"),
    STANDARD("Standard"),
    HARDCORE("Harcore");

    private String urlName;
    private String displayName;

    League(String name) {
        this.displayName = name;
        this.urlName = name.replace(" ", "+");
    }

    public String getUrlName() {
        return urlName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
