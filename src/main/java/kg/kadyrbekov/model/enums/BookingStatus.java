package kg.kadyrbekov.model.enums;

public enum BookingStatus {
    FREE("Free"),
    BUSY("Busy");

    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }


}
