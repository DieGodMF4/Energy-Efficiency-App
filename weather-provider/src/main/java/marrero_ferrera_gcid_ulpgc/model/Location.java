package marrero_ferrera_gcid_ulpgc.model;

public class Location {
    private final float latitude;
    private final float longitude;
    private final String name;

    public Location(float latitude, float longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
