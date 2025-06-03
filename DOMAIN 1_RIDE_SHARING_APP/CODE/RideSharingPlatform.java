// File: RideSharingPlatform.java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RideSharingPlatform {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Vehicle> vehicles = new HashMap<>();
    private List<RideRequest> activeRideRequests = new ArrayList<>();
    private List<Ride> ongoingAndCompletedRides = new ArrayList<>();
    private List<PaymentTransaction> paymentTransactions = new ArrayList<>();
    private List<Rating> ratings = new ArrayList<>();

    public User registerUser(String name, String phone, String password, UserType type, String licenseIfDriver) {
        User existing = users.values().stream().filter(u -> u.getPhoneNumber().equals(phone) && u.getUserType() == type).findFirst().orElse(null);
        if (existing != null) {
            System.out.println("! User with this phone number and type already exists: " + existing.getUserId());
            return existing;
        }
        User user;
        if (type == UserType.RIDER) {
            user = new Rider(name, phone, password);
        } else {
            user = new Driver(name, phone, password, licenseIfDriver);
        }
        users.put(user.getUserId(), user);
        System.out.println(type + " registered: " + user.getName() + " (ID: " + user.getUserId() + ")");
        return user;
    }

    public User loginUser(String phone, String password, UserType type) {
        User user = users.values().stream()
                .filter(u -> u.getPhoneNumber().equals(phone) && u.getUserType() == type && u.checkPassword(password))
                .findFirst().orElse(null);
        if (user != null) {
            System.out.println(type + " " + user.getName() + " logged in successfully.");
        } else {
            System.out.println("! Login failed. Invalid phone number, password, or user type combination.");
        }
        return user;
    }

    public Vehicle addVehicleForDriver(String driverId, String make, String model, String plate, String color, String type) {
        User user = users.get(driverId);
        if (!(user instanceof Driver)) {
            System.out.println("! User " + driverId + " is not a driver.");
            return null;
        }
        Vehicle vehicle = new Vehicle(driverId, make, model, plate, color, type);
        vehicles.put(vehicle.getVehicleId(), vehicle);
        ((Driver) user).setVehicle(vehicle);
        System.out.println("Vehicle " + vehicle + " added for driver " + user.getName());
        return vehicle;
    }

    public void setDriverStatus(String driverId, DriverStatus status, LocationPin locationIfOnline) {
        User user = users.get(driverId);
        if (user instanceof Driver) {
            Driver driver = (Driver) user;
            if (status == DriverStatus.ONLINE_AVAILABLE && driver.getVehicle() == null) {
                System.out.println("! Driver " + driver.getName() + " cannot go online without a registered vehicle.");
                return;
            }
            driver.setStatus(status);
            if (status == DriverStatus.ONLINE_AVAILABLE) {
                driver.setCurrentLocation(locationIfOnline);
            } else if (status == DriverStatus.OFFLINE) {
                driver.setCurrentLocation(null);
            }
        } else {
            System.out.println("! User " + driverId + " is not a driver or not found.");
        }
    }

    public RideRequest createRideRequest(String riderId, LocationPin pickup, LocationPin dropoff) {
        if (!users.containsKey(riderId) || users.get(riderId).getUserType() != UserType.RIDER) {
            System.out.println("! Invalid rider ID.");
            return null;
        }
        double distance = pickup.distanceTo(dropoff);
        double estimatedFare = 2.50 + (distance * 1.20); // Example: Base fare + per km rate
        RideRequest request = new RideRequest(riderId, pickup, dropoff, estimatedFare);
        activeRideRequests.add(request);
        System.out.println("Ride request created: " + request.getRequestId() + " from " + pickup.addressName + " to " + dropoff.addressName + ". Estimated fare: $" + String.format("%.2f", estimatedFare));
        assignDriverToRequest(request);
        return request;
    }

    private void assignDriverToRequest(RideRequest request) {
        request.setStatus(RideRequestStatus.SEARCHING_FOR_DRIVER);
        System.out.println("Searching for an available driver for request " + request.getRequestId() + "...");

        Optional<Driver> foundDriver = users.values().stream()
                .filter(u -> u instanceof Driver)
                .map(u -> (Driver) u)
                .filter(d -> d.getStatus() == DriverStatus.ONLINE_AVAILABLE && d.getVehicle() != null && d.getCurrentLocation() != null)
                .min((d1, d2) -> Double.compare(
                        d1.getCurrentLocation().distanceTo(request.getPickupLocation()),
                        d2.getCurrentLocation().distanceTo(request.getPickupLocation())
                ));

        if (foundDriver.isPresent()) {
            Driver driver = foundDriver.get();
            System.out.println("Driver " + driver.getName() + " (ID: " + driver.getUserId() + ") found for request " + request.getRequestId() +
                               ". Approx. distance to pickup: " + String.format("%.1f", driver.getCurrentLocation().distanceTo(request.getPickupLocation())) + "km.");
            acceptRideByDriver(request.getRequestId(), driver.getUserId());
        } else {
            request.setStatus(RideRequestStatus.NO_DRIVERS_AVAILABLE);
            System.out.println("! Unfortunately, no available drivers were found for your request " + request.getRequestId() + " at this time.");
        }
    }

    public Ride acceptRideByDriver(String requestId, String driverId) {
        RideRequest request = activeRideRequests.stream()
            .filter(r -> r.getRequestId().equals(requestId) && r.getStatus() == RideRequestStatus.SEARCHING_FOR_DRIVER)
            .findFirst().orElse(null);

        User driverUser = users.get(driverId);

        if (request == null) {
            System.out.println("! Ride request " + requestId + " not found or not in a state to be accepted.");
            return null;
        }
        if (!(driverUser instanceof Driver)) {
            System.out.println("! User " + driverId + " is not a valid driver.");
            return null;
        }

        Driver driver = (Driver) driverUser;
        if (driver.getVehicle() == null) {
            System.out.println("! Driver " + driver.getName() + " has no vehicle assigned. Cannot accept ride.");
            return null;
        }
        if (driver.getStatus() != DriverStatus.ONLINE_AVAILABLE) {
            System.out.println("! Driver " + driver.getName() + " is not currently available (Status: " + driver.getStatus() + ").");
            return null;
        }

        request.setStatus(RideRequestStatus.DRIVER_ASSIGNED);
        activeRideRequests.remove(request); 

        Ride ride = new Ride(request, driverId, driver.getVehicle().getVehicleId());
        ongoingAndCompletedRides.add(ride);

        driver.setStatus(DriverStatus.EN_ROUTE_TO_PICKUP);
        ride.setStatus(RideStatus.EN_ROUTE_TO_PICKUP);

        System.out.println("Ride " + ride.getRideId() + " (Request: " + requestId + ") has been assigned to and accepted by driver " + driver.getName() + ". Driver is en route to pickup at " + request.getPickupLocation().addressName + ".");
        return ride;
    }

    public void updateRideProgress(String rideId, RideStatus newStatus, String actorId) {
        Ride ride = ongoingAndCompletedRides.stream().filter(r -> r.getRideId().equals(rideId)).findFirst().orElse(null);
        if (ride == null) {
            System.out.println("! Ride " + rideId + " not found. Cannot update progress.");
            return;
        }

        User actor = users.get(actorId);
        if (actor == null) {
            System.out.println("! Actor " + actorId + " performing update not found.");
            return;
        }

        Driver driver = null;
        if (users.get(ride.getDriverId()) instanceof Driver) {
            driver = (Driver) users.get(ride.getDriverId());
        }

        RideStatus oldStatus = ride.getStatus();
        ride.setStatus(newStatus);
        System.out.println("Ride " + rideId + " status updated from " + oldStatus + " to: " + newStatus + " (Action by: " + actor.getName() + ")");

        if (driver != null) {
            if (newStatus == RideStatus.ARRIVED_AT_PICKUP) {
                 System.out.println("Driver " + driver.getName() + " has arrived at pickup for ride " + rideId + ".");
            } else if (newStatus == RideStatus.IN_PROGRESS) {
                driver.setStatus(DriverStatus.ON_TRIP_WITH_RIDER);
                if(ride.getStartTime() == null) ride.startRide(); // Use getter
                System.out.println("Ride " + rideId + " is now in progress with rider.");
            } else if (newStatus == RideStatus.COMPLETED) {
                driver.setStatus(DriverStatus.ONLINE_AVAILABLE);
                System.out.println("Ride " + rideId + " is completed. Driver " + driver.getName() + " is now " + driver.getStatus());
            } else if (newStatus == RideStatus.CANCELLED_BY_DRIVER || newStatus == RideStatus.CANCELLED_BY_RIDER) {
                driver.setStatus(DriverStatus.ONLINE_AVAILABLE);
                 System.out.println("Ride " + rideId + " cancelled. Driver " + driver.getName() + " is now " + driver.getStatus());
            }
        } else if (ride.getDriverId() != null) {
             System.out.println("! Critical: Driver " + ride.getDriverId() + " for ride " + rideId + " not found in system, cannot update their status.");
        }
    }

    public void completeRide(String rideId, double actualDistance, double actualDuration, String driverIdCallingCompletion) {
        Ride ride = ongoingAndCompletedRides.stream()
            .filter(r -> r.getRideId().equals(rideId) && r.getDriverId().equals(driverIdCallingCompletion))
            .findFirst().orElse(null);

        if (ride == null) {
            System.out.println("! Ride " + rideId + " not found for you or does not exist.");
            return;
        }
        if (ride.getStatus() == RideStatus.COMPLETED) {
            System.out.println("! Ride " + rideId + " is already marked as completed.");
            return;
        }
        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            System.out.println("! Ride " + rideId + " must be IN_PROGRESS to be completed. Current status: " + ride.getStatus());
            return;
        }

        ride.endRide(actualDistance, actualDuration);
        double finalFare = 2.50 + (actualDistance * 1.30) + (actualDuration * 0.20);
        ride.setFinalFare(finalFare);

        User driver = users.get(driverIdCallingCompletion);
        System.out.println("Ride " + rideId + " completed by driver " + (driver != null ? driver.getName() : driverIdCallingCompletion) +
                           ". Actual Distance: " + String.format("%.1f", actualDistance) + "km, Duration: " +
                           String.format("%.0f", actualDuration) + " mins. Final Fare: $" + String.format("%.2f", finalFare));

        updateRideProgress(rideId, RideStatus.COMPLETED, driverIdCallingCompletion);
        processPayment(ride.getRideId(), ride.getRiderId(), ride.getFinalFare());
    }

    public PaymentTransaction processPayment(String rideId, String riderId, double amount) {
        User user = users.get(riderId);
        if (!(user instanceof Rider)) {
            System.out.println("! Rider " + riderId + " for payment processing not found.");
            return null;
        }

        Rider rider = (Rider) user;
        String paymentMethod = rider.getPreferredPaymentMethod();
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            paymentMethod = "Cash/Default";
            System.out.println("No preferred payment method set for rider " + rider.getName() + ". Using " + paymentMethod + ".");
        }

        PaymentTransaction tx = new PaymentTransaction(rideId, riderId, amount, paymentMethod);
        tx.setStatus(PaymentStatus.COMPLETED);
        paymentTransactions.add(tx);
        System.out.println("Payment processed successfully: " + tx);

        Ride ride = ongoingAndCompletedRides.stream().filter(r -> r.getRideId().equals(rideId)).findFirst().orElse(null);
        if (ride != null) {
            ride.paymentTransactionId = tx.getTransactionId();
        }
        return tx;
    }

    public void addRating(String rideId, String ratedByUserId, String ratedUserId, int score, String comment) {
        User rater = users.get(ratedByUserId);
        User rated = users.get(ratedUserId);
        Ride ride = ongoingAndCompletedRides.stream().filter(r -> r.getRideId().equals(rideId)).findFirst().orElse(null);

        if (rater == null || rated == null || ride == null) {
            System.out.println("! Invalid details for rating: User or Ride not found.");
            return;
        }
        if (ride.getStatus() != RideStatus.COMPLETED) {
            System.out.println("! Ride " + rideId + " has not been completed. Cannot rate yet.");
            return;
        }

        boolean alreadyRated = ratings.stream().anyMatch(rt -> rt.getRideId().equals(rideId) &&
                                                               rt.getRatedByUserId().equals(ratedByUserId) &&
                                                               rt.getRatedUserId().equals(ratedUserId));
        if (alreadyRated) {
            System.out.println("! You have already submitted a rating for " + rated.getName() + " for ride " + rideId + ".");
            return;
        }

        boolean isValidParticipantPair = (ride.getRiderId().equals(ratedByUserId) && ride.getDriverId().equals(ratedUserId)) ||
                                         (ride.getDriverId().equals(ratedByUserId) && ride.getRiderId().equals(ratedUserId));
        if (!isValidParticipantPair) {
            System.out.println("! Rating error: The rater (" + rater.getName() + ") or rated (" + rated.getName() + ") were not the primary participants of ride " + rideId + ".");
            return;
        }

        Rating rating = new Rating(rideId, ratedByUserId, ratedUserId, score, comment);
        ratings.add(rating);
        rated.addRating(score);

        System.out.println("Rating successfully submitted: " + rating);
        System.out.println(rated.getName() + "'s new average rating is now: " + String.format("%.1f", rated.getAverageRating()));
    }

    // Getters
    public User getUserById(String userId) { return users.get(userId); }
    public List<Ride> getRiderHistory(String riderId) { return ongoingAndCompletedRides.stream().filter(r -> r.getRiderId().equals(riderId)).collect(Collectors.toList()); }
    public List<Ride> getDriverHistory(String driverId) { return ongoingAndCompletedRides.stream().filter(r -> r.getDriverId().equals(driverId)).collect(Collectors.toList()); }
    public List<User> getAllUsers() { return new ArrayList<>(users.values()); }
    public List<Vehicle> getAllVehicles() { return new ArrayList<>(vehicles.values()); }
    public List<RideRequest> getActiveRideRequests() {
        activeRideRequests.removeIf(r -> r.getStatus() == RideRequestStatus.NO_DRIVERS_AVAILABLE ||
                                         r.getStatus() == RideRequestStatus.CANCELLED_BY_RIDER ||
                                         r.getStatus() == RideRequestStatus.DRIVER_ASSIGNED);
        return activeRideRequests.stream()
                                 .filter(r -> r.getStatus() == RideRequestStatus.PENDING || r.getStatus() == RideRequestStatus.SEARCHING_FOR_DRIVER)
                                 .collect(Collectors.toList());
    }
    public List<Ride> getOngoingRides() { return ongoingAndCompletedRides.stream().filter(r -> r.getStatus() != RideStatus.COMPLETED && r.getStatus() != RideStatus.CANCELLED_BY_DRIVER && r.getStatus() != RideStatus.CANCELLED_BY_RIDER).collect(Collectors.toList());}
    public List<Ride> getOngoingAndCompletedRides() { return new ArrayList<>(ongoingAndCompletedRides); }
    public List<PaymentTransaction> getAllPaymentTransactions() { return new ArrayList<>(paymentTransactions); }
    public List<Rating> getAllRatings() { return new ArrayList<>(ratings); }
}