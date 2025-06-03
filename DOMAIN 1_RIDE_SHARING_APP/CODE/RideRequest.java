// File: RideRequest.java
import java.time.LocalDateTime;
import java.util.UUID;

public class RideRequest {
    String requestId;
    String riderId;
    LocationPin pickupLocation;
    LocationPin dropoffLocation;
    LocalDateTime requestTime;
    RideRequestStatus status;
    double estimatedFare;

    public RideRequest(String riderId, LocationPin pickup, LocationPin dropoff, double fare) {
        this.requestId = "req-" + UUID.randomUUID().toString().substring(0, 5);
        this.riderId = riderId;
        this.pickupLocation = pickup;
        this.dropoffLocation = dropoff;
        this.requestTime = LocalDateTime.now();
        this.status = RideRequestStatus.PENDING;
        this.estimatedFare = fare;
    }

    public String getRequestId() { return requestId; }
    public String getRiderId() { return riderId; }
    public LocationPin getPickupLocation() { return pickupLocation; }
    public LocationPin getDropoffLocation() { return dropoffLocation; }
    public RideRequestStatus getStatus() { return status; }
    public void setStatus(RideRequestStatus s) { this.status = s; }
    public double getEstimatedFare() { return estimatedFare; }

    @Override
    public String toString() {
        return "RideRequest ID: " + requestId + " from " + pickupLocation.addressName + " to " + dropoffLocation.addressName +
               ", FareEst: $" + String.format("%.2f", estimatedFare) + ", Status: " + status;
    }
}