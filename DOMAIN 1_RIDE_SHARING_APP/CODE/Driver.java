// File: Driver.java
public class Driver extends User {
    String licenseNumber;
    Vehicle vehicle;
    DriverStatus status;
    LocationPin currentLocation;

    public Driver(String name, String phone, String password, String license) {
        super(name, phone, password, UserType.DRIVER);
        this.licenseNumber = license;
        this.status = DriverStatus.OFFLINE;
    }

    public void setVehicle(Vehicle v) { this.vehicle = v; }
    public Vehicle getVehicle() { return vehicle; }
    public DriverStatus getStatus() { return status; }
    public void setStatus(DriverStatus s) {
        this.status = s;
        System.out.println("Driver " + name + " status changed to: " + s);
    }
    public LocationPin getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(LocationPin loc) { this.currentLocation = loc; }
}