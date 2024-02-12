package kg.kadyrbekov.model.enums;

public enum City {

    BISHKEK("Bishkek"),

    OSH("Osh"),

    BATKEN("Batken"),

    CHUI("Chui"),

    JALALABAD("Jalal-Abad"),

    NARYN("Naryn"),

    YSYKKOL("Ysyk-kol"),

    TALAS("Talas");

    private final String displayName;

    City(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }


}
