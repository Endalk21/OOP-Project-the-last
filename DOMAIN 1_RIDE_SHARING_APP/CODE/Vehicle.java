// File: Vehicle.java
import java.util.UUID;

public class Vehicle {
    String vehicleId;
    String driverId;
    String make;
    String model;
    String licensePlate;
    String color;
    String type;

    public Vehicle(String driverId, String make, String model, String plate, String color, String type) {
        this.vehicleId = "veh-" + UUID.randomUUID().toString().substring(0, 4);
        this.driverId = driverId;
        this.make = make;
        this.model = model;
        this.licensePlate = plate;
        this.color = color;
        this.type = type;
    }

    public String getVehicleId() { return vehicleId; }
    public String getLicensePlate() { return licensePlate; }

    @Override
    public String toString() {
        return type + ": " + make + " " + model + " (Plate: " + licensePlate + ", Color: " + color +")";
    }
}