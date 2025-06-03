// File: LocationPin.java
public class LocationPin {
    double latitude;
    double longitude;
    String addressName;

    public LocationPin(String addressName, double lat, double lon) {
        this.addressName = addressName;
        this.latitude = lat;
        this.longitude = lon;
    }

    @Override
    public String toString() {
        return addressName + " (" + String.format("%.2f, %.2f", latitude, longitude) + ")";
    }

    //Distance calculation: 1 degree of latitude or longitude is approximately 111km. This is a simplification.
    public double distanceTo(LocationPin other) {
        return Math.sqrt(Math.pow(this.latitude - other.latitude, 2) + Math.pow(this.longitude - other.longitude, 2)) * 111;
    }
}