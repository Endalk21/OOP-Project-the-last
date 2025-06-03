// File: Ride.java
import java.time.LocalDateTime;
import java.util.UUID;

public class Ride {
    String rideId;
    String rideRequestId;
    String riderId;
    String driverId;
    String vehicleId;
    LocationPin pickupLocation; // Field
    LocationPin dropoffLocation; // Field
    LocalDateTime startTime, endTime;
    double actualDistance, actualDurationMinutes;
    double finalFare;
    RideStatus status;
    String paymentTransactionId;

    public Ride(RideRequest request, String driverId, String vehicleId) {
        this.rideId = "ride-" + UUID.randomUUID().toString().substring(0, 6);
        this.rideRequestId = request.getRequestId();
        this.riderId = request.getRiderId();
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.pickupLocation = request.getPickupLocation();     // Initialize field
        this.dropoffLocation = request.getDropoffLocation();   // Initialize field
        this.status = RideStatus.PENDING_PICKUP;
        this.finalFare = request.getEstimatedFare();
    }

    // --- GETTERS ---
    public String getRideId() { return rideId; }
    public String getRiderId() { return riderId; }
    public String getDriverId() { return driverId; }
    public RideStatus getStatus() { return status; }
    public double getFinalFare() { return finalFare; }
    public LocalDateTime getEndTime() { return endTime; }
    public LocationPin getPickupLocation() { return pickupLocation; }   // ADDED GETTER
    public LocationPin getDropoffLocation() { return dropoffLocation; } // ADDED GETTER
    public LocalDateTime getStartTime() {return startTime;} // Added getter for completeness

    // --- SETTERS & MODIFIERS ---
    public void setStatus(RideStatus s) { this.status = s; }
    public void setFinalFare(double f) { this.finalFare = f; }

    public void startRide() {
        this.startTime = LocalDateTime.now();
        this.status = RideStatus.IN_PROGRESS;
    }

    public void endRide(double distance, double duration) {
        this.endTime = LocalDateTime.now();
        this.actualDistance = distance;
        this.actualDurationMinutes = duration;
        this.status = RideStatus.COMPLETED;
    }

    @Override
    public String toString() {
        return "Ride ID: " + rideId + ", Rider: " + riderId + ", Driver: " + driverId +
               ", From: " + (pickupLocation != null ? pickupLocation.addressName : "N/A") + // Null check for safety
               " To: " + (dropoffLocation != null ? dropoffLocation.addressName : "N/A") +   // Null check for safety
               ", Fare: $" + String.format("%.2f", finalFare) + ", Status: " + status;
    }
}