// File: Main.java
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static RideSharingPlatform platform = new RideSharingPlatform();
    private static Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null;

    public static void main(String[] args) {
        System.out.println("--- Welcome to the Ethiopian Ride-Hailing Platform ---");
        boolean running = true;
        while(running) {
            printMenu();
            int choice = getIntInput("Enter your choice (number):");

            if (loggedInUser == null && choice >= 3 && choice < 14) {
                 System.out.println("! Access denied. Please login or register first to use this feature.");
                 pressEnterToContinue();
                 continue;
            }

            switch(choice) {
                case 1: handleRegister(); break;
                case 2: handleLogin(); break;

                case 3: if(loggedInUser != null) handleLogout(); else System.out.println("! You are not logged in."); break;

                case 4: if(loggedInUser instanceof Rider) handleRequestRide(); else if(loggedInUser !=null) System.out.println("! This option is for Riders only."); break;
                case 5: if(loggedInUser instanceof Rider) handleViewMyRideHistoryRider(); else if(loggedInUser !=null) System.out.println("! This option is for Riders only."); break;
                case 6: if(loggedInUser instanceof Rider) handleRateDriver(); else if(loggedInUser !=null) System.out.println("! This option is for Riders only."); break;

                case 7: if(loggedInUser instanceof Driver) handleSetDriverStatus(); else if(loggedInUser !=null) System.out.println("! This option is for Drivers only."); break;
                case 8: if(loggedInUser instanceof Driver) handleAddVehicle(); else if(loggedInUser !=null) System.out.println("! This option is for Drivers only."); break;
                case 9: if(loggedInUser instanceof Driver) handleViewAssignedRideDriver(); else if(loggedInUser !=null) System.out.println("! This option is for Drivers only."); break;
                case 10: if(loggedInUser instanceof Driver) handleUpdateRideProgressDriver(); else if(loggedInUser !=null) System.out.println("! This option is for Drivers only."); break;
                case 11: if(loggedInUser instanceof Driver) handleCompleteRideDriver(); else if(loggedInUser !=null) System.out.println("! This option is for Drivers only."); break;
                case 12: if(loggedInUser instanceof Driver) handleViewMyRideHistoryDriver(); else if(loggedInUser !=null) System.out.println("! This option is for Drivers only."); break;
                case 13: if(loggedInUser instanceof Driver) handleRateRider(); else if(loggedInUser !=null) System.out.println("! This option is for Drivers only."); break;

                case 14: viewSystemState(); break;
                case 15: running = false; break;
                default: System.out.println("! Invalid choice. Please select a number from the menu.");
            }
            if(running && choice != 15) {
                 pressEnterToContinue();
            }
        }
        System.out.println("Thank you for using the Ethiopian Ride-Hailing Platform. Goodbye!");
        scanner.close();
    }

    private static void pressEnterToContinue() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }


    private static void printMenu() {
        System.out.println("\n========= Ride-Hailing Platform Menu =========");
        if (loggedInUser == null) {
            System.out.println("--- Account ---");
            System.out.println("1. Register (New Rider/Driver)");
            System.out.println("2. Login (Existing Rider/Driver)");
        } else {
            System.out.println("Logged in as: " + loggedInUser.getName() + " (" + loggedInUser.getUserType() +
                               ", Rating: " + String.format("%.1f", loggedInUser.getAverageRating()) + ")");
            System.out.println("--- Account ---");
            System.out.println("3. Logout");

            if (loggedInUser instanceof Rider) {
                System.out.println("--- Rider Menu ---");
                System.out.println("4. Request a Ride");
                System.out.println("5. View My Ride History");
                System.out.println("6. Rate a Driver (from a completed ride)");
            } else if (loggedInUser instanceof Driver) {
                Driver d = (Driver) loggedInUser;
                System.out.println("--- Driver Menu (Status: " + d.getStatus() +
                                   (d.getVehicle() == null ? " - NO VEHICLE ASSIGNED" : " | Vehicle: " + d.getVehicle().getLicensePlate()) + ") --");
                System.out.println("7. Set My Status & Location (Online/Offline)");
                if (d.getVehicle() == null) {
                     System.out.println("8. Add My Vehicle Details (Required to go Online)");
                } else {
                     System.out.println("8. Update My Vehicle Details");
                }
                System.out.println("9. View My Current Assigned Ride Details");
                System.out.println("10. Update Current Ride Progress (Arrived, Picked Up)");
                System.out.println("11. Complete Current Ride (End trip & enter details)");
                System.out.println("12. View My Driving History");
                System.out.println("13. Rate a Rider (from a completed ride)");
            }
        }
        System.out.println("--- System ---");
        System.out.println("14. View System State (Admin/Debug)");
        System.out.println("15. Exit Platform");
        System.out.println("============================================");
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine().trim();
    }
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("! Invalid input. Please enter a whole number.");
            }
        }
    }
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " ");
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("! Invalid input. Please enter a number (e.g., 10.5 or 25).");
            }
        }
    }

    private static void handleRegister() {
        System.out.println("\n--- New User Registration ---");
        String typeStr = getStringInput("Register as (RIDER or DRIVER):").toUpperCase();
        UserType type;
        try { type = UserType.valueOf(typeStr); }
        catch (IllegalArgumentException e) { System.out.println("! Invalid user type. Please enter RIDER or DRIVER."); return; }

        String name = getStringInput("Full Name (e.g., Haile Gebreselassie):");
        if (name.isEmpty()) { System.out.println("! Name cannot be empty."); return; }
        String phone = getStringInput("Phone Number (e.g., 09xxxxxxxx):");
        if (phone.isEmpty() || !phone.matches("09\\d{8}")) { System.out.println("! Invalid phone number format. Should be 09xxxxxxxx (10 digits)."); return; }
        String password = getStringInput("Create a Password:");
        if (password.isEmpty()) { System.out.println("! Password cannot be empty."); return; }

        String license = null;
        if (type == UserType.DRIVER) {
            license = getStringInput("Enter Driver's License Number:");
            if (license.isEmpty()) { System.out.println("! Driver's license number cannot be empty for drivers."); return; }
        }

        User newUser = platform.registerUser(name, phone, password, type, license);
        if (newUser != null) {
            loggedInUser = newUser;
            System.out.println("Registration successful! You are now logged in as " + loggedInUser.getName() + ".");
            if (newUser instanceof Driver && ((Driver)newUser).getVehicle() == null) {
                System.out.println("Welcome, Driver " + newUser.getName() + "! To start accepting rides, please add your vehicle details now.");
                handleAddVehicle();
            }
        }
    }

    private static void handleLogin() {
        System.out.println("\n--- User Login ---");
        String typeStr = getStringInput("Login as (RIDER or DRIVER):").toUpperCase();
        UserType type;
        try { type = UserType.valueOf(typeStr); }
        catch (IllegalArgumentException e) { System.out.println("! Invalid user type. Please enter RIDER or DRIVER."); return; }

        String phone = getStringInput("Enter Phone Number:");
        String password = getStringInput("Enter Password:");

        User user = platform.loginUser(phone, password, type);
        if (user != null) {
            loggedInUser = user;
        }
    }

    private static void handleLogout() {
        if(loggedInUser != null) {
            System.out.println(loggedInUser.getName() + " (" + loggedInUser.getUserType() + ") has been successfully logged out.");
            loggedInUser = null;
        } else {
            System.out.println("! No user is currently logged in to logout.");
        }
    }

    private static LocationPin getLocationPinInput(String promptSuffix) {
        System.out.println("Enter " + promptSuffix + " Location Details:");
        String name = getStringInput("  Location Landmark/Name (e.g., Bole Medhanealem, Arat Kilo, Merkato):");
        double lat = getDoubleInput("  Latitude (e.g., " + String.format("%.4f", 8.85 + Math.random()*0.25) +"):");
        double lon = getDoubleInput("  Longitude (e.g., " + String.format("%.4f", 38.65 + Math.random()*0.25) +"):");
        return new LocationPin(name, lat, lon);
    }

    private static void handleRequestRide() {
        System.out.println("\n--- Request a New Ride ---");
        LocationPin pickup = getLocationPinInput("Pickup");
        LocationPin dropoff = getLocationPinInput("Dropoff");

        Rider currentRider = (Rider)loggedInUser;
        if (currentRider.getPreferredPaymentMethod() == null || currentRider.getPreferredPaymentMethod().isEmpty() ) {
            System.out.println("You don't have a preferred payment method set up.");
            String paymentMethod = getStringInput("Enter Preferred Payment Method for this ride (e.g., TeleBirr, CBEBirr, Cash):");
            if (!paymentMethod.isEmpty()) {
                currentRider.setPreferredPaymentMethod(paymentMethod);
                 System.out.println("Preferred payment method set to: " + paymentMethod);
            } else {
                 System.out.println("No payment method entered. Default will be used if applicable.");
            }
        }
        platform.createRideRequest(loggedInUser.getUserId(), pickup, dropoff);
    }

    private static void handleViewMyRideHistoryRider() {
        System.out.println("\n--- Your Ride History (" + loggedInUser.getName() + ") ---");
        List<Ride> history = platform.getRiderHistory(loggedInUser.getUserId());
        if(history.isEmpty()) {
            System.out.println("You have no ride history yet.");
        } else {
            System.out.println("Displaying your past rides:");
            for(Ride ride : history) {
                System.out.println(ride);
            }
        }
    }

    private static void handleRateDriver() {
        System.out.println("\n--- Rate a Driver from a Past Ride ---");
        List<Ride> completedRidesToRate = platform.getRiderHistory(loggedInUser.getUserId()).stream()
                .filter(r -> r.getStatus() == RideStatus.COMPLETED)
                .filter(r -> !platform.getAllRatings().stream().anyMatch(
                                rt -> rt.getRideId().equals(r.getRideId()) &&
                                      rt.getRatedByUserId().equals(loggedInUser.getUserId()) &&
                                      rt.getRatedUserId().equals(r.getDriverId())))
                .collect(Collectors.toList());

        if(completedRidesToRate.isEmpty()){
            System.out.println("You have no completed rides that are pending your rating for the driver.");
            return;
        }

        System.out.println("Select a completed ride to rate the driver:");
        for (int i = 0; i < completedRidesToRate.size(); i++) {
            Ride r = completedRidesToRate.get(i);
            User driver = platform.getUserById(r.getDriverId());
            System.out.println((i + 1) + ". Ride ID: " + r.getRideId() +
                               " (Completed: " + (r.getEndTime() != null ? r.getEndTime().toLocalDate() : "N/A") + ")" +
                               " with Driver: " + (driver != null ? driver.getName() : "N/A"));
        }

        int rideChoice = getIntInput("Enter the number of the ride to rate (or 0 to cancel):");
        if (rideChoice <= 0 || rideChoice > completedRidesToRate.size()) {
            System.out.println("Rating cancelled or invalid selection.");
            return;
        }
        Ride rideToRate = completedRidesToRate.get(rideChoice - 1);
        User driverToRate = platform.getUserById(rideToRate.getDriverId());
        if (driverToRate == null) { System.out.println("! Driver for this ride not found. Cannot rate."); return; }

        System.out.println("You are rating Driver: " + driverToRate.getName() + " for Ride ID: " + rideToRate.getRideId());
        int score = getIntInput("Enter your rating for the driver (1=Poor, 5=Excellent):");
        if (score < 1 || score > 5) { System.out.println("! Invalid score. Rating must be between 1 and 5."); return; }
        String comment = getStringInput("Add a comment (optional, press Enter to skip):");

        platform.addRating(rideToRate.getRideId(), loggedInUser.getUserId(), rideToRate.getDriverId(), score, comment);
    }

    private static void handleSetDriverStatus() {
        System.out.println("\n--- Update Your Driving Status ---");
        Driver driver = (Driver)loggedInUser;

        if(driver.getVehicle() == null) {
            System.out.println("! CRITICAL: You must add your vehicle details first (Option 8) before you can go online.");
            return;
        }

        System.out.println("Current Status: " + driver.getStatus());
        String statusStr = getStringInput("Set new status to (ONLINE_AVAILABLE or OFFLINE):").toUpperCase();
        DriverStatus newStatus;
        try {
            newStatus = DriverStatus.valueOf(statusStr);
            if (newStatus != DriverStatus.ONLINE_AVAILABLE && newStatus != DriverStatus.OFFLINE) {
                System.out.println("! Invalid choice. You can only set your status to ONLINE_AVAILABLE or OFFLINE directly from this menu.");
                return;
            }
        }
        catch (IllegalArgumentException e) { System.out.println("! Invalid status entered. Please type ONLINE_AVAILABLE or OFFLINE."); return; }

        LocationPin loc = null;
        if (newStatus == DriverStatus.ONLINE_AVAILABLE) {
            System.out.println("To go ONLINE_AVAILABLE, please provide your current location.");
            loc = getLocationPinInput("your current");
        }
        platform.setDriverStatus(loggedInUser.getUserId(), newStatus, loc);
    }

    private static void handleAddVehicle() {
        System.out.println("\n--- Add or Update Your Vehicle Details ---");
        Driver driver = (Driver)loggedInUser;

        if(driver.getVehicle() != null) {
            System.out.println("Current Vehicle: " + driver.getVehicle());
            String confirm = getStringInput("You already have a vehicle registered. Do you want to update its details? (YES/NO):").toUpperCase();
            if(!confirm.equals("YES")) {
                System.out.println("Vehicle update cancelled.");
                return;
            }
        }

        System.out.println("Please enter your vehicle information:");
        String make = getStringInput("  Vehicle Make (e.g., Toyota, Lifan, Suzuki):");
        String model = getStringInput("  Vehicle Model (e.g., Vitz, 520, Swift, Lada):");
        String plate = getStringInput("  License Plate Number (e.g., A01-12345 or 2-B54321):");
        String color = getStringInput("  Vehicle Color (e.g., White, Blue, Silver):");
        String type = getStringInput("  Vehicle Type (e.g., Sedan, Minibus, Bajaj, Hatchback):");

        platform.addVehicleForDriver(loggedInUser.getUserId(), make, model, plate, color, type);
    }

    private static Ride getDriverCurrentRide(String driverId) {
        return platform.getOngoingRides().stream()
                .filter(r -> r.getDriverId().equals(driverId) &&
                             (r.getStatus() == RideStatus.EN_ROUTE_TO_PICKUP ||
                              r.getStatus() == RideStatus.ARRIVED_AT_PICKUP ||
                              r.getStatus() == RideStatus.IN_PROGRESS))
                .findFirst().orElse(null);
    }

    private static void handleViewAssignedRideDriver(){
        System.out.println("\n--- Your Current Assigned Ride Details ---");
        Ride currentRide = getDriverCurrentRide(loggedInUser.getUserId());
        if (currentRide != null) {
            System.out.println("You have an active ride assignment:");
            System.out.println("  " + currentRide);
            User rider = platform.getUserById(currentRide.getRiderId());
            if (rider != null) {
                System.out.println("  Rider Details: " + rider.getName() + ", Phone: " + rider.getPhoneNumber());
            }
            // Accessing location details via getters from Ride object
            if (currentRide.getPickupLocation() != null) { // Null check for safety
                System.out.println("  Pickup: " + currentRide.getPickupLocation());
            } else {
                System.out.println("  Pickup: N/A");
            }
            if (currentRide.getDropoffLocation() != null) { // Null check for safety
                System.out.println("  Dropoff: " + currentRide.getDropoffLocation());
            } else {
                System.out.println("  Dropoff: N/A");
            }
        } else {
            System.out.println("You do not have an active ride assigned to you at this moment.");
        }
    }

    private static void handleUpdateRideProgressDriver() {
        System.out.println("\n--- Update Progress of Your Current Ride ---");
        Ride currentRide = getDriverCurrentRide(loggedInUser.getUserId());
        if (currentRide == null) {
            System.out.println("! You don't have an active ride to update progress for.");
            return;
        }

        System.out.println("Current Ride Status: " + currentRide.getStatus() + " (ID: " + currentRide.getRideId() + ")");
        RideStatus currentActualStatus = currentRide.getStatus();
        Map<Integer, RideStatus> possibleNextStatusesMap = new HashMap<>();
        int optionCounter = 1;

        System.out.println("Select the next status for your ride:");
        if (currentActualStatus == RideStatus.EN_ROUTE_TO_PICKUP) {
            possibleNextStatusesMap.put(optionCounter++, RideStatus.ARRIVED_AT_PICKUP);
            possibleNextStatusesMap.put(optionCounter++, RideStatus.CANCELLED_BY_DRIVER);
        } else if (currentActualStatus == RideStatus.ARRIVED_AT_PICKUP) {
            possibleNextStatusesMap.put(optionCounter++, RideStatus.IN_PROGRESS);
            possibleNextStatusesMap.put(optionCounter++, RideStatus.CANCELLED_BY_DRIVER);
        } else if (currentActualStatus == RideStatus.IN_PROGRESS) {
            System.out.println("! To complete this ride, please use Option 11 'Complete Current Ride'.");
            possibleNextStatusesMap.put(optionCounter++, RideStatus.CANCELLED_BY_DRIVER);
        } else {
            System.out.println("! Ride is not in a state that allows for typical progress updates from this menu (Current: "+ currentActualStatus +").");
            return;
        }

        if(possibleNextStatusesMap.isEmpty()){
            System.out.println("! No standard status transitions available from " + currentActualStatus + " via this menu.");
            return;
        }

        for(Map.Entry<Integer, RideStatus> entry : possibleNextStatusesMap.entrySet()){
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
        int choice = getIntInput("Select new status number (or 0 to cancel):");

        if(choice == 0) { System.out.println("Update cancelled."); return; }

        RideStatus newStatus = possibleNextStatusesMap.get(choice);
        if(newStatus == null) {
            System.out.println("! Invalid selection for status update.");
            return;
        }

        platform.updateRideProgress(currentRide.getRideId(), newStatus, loggedInUser.getUserId());
    }

    private static void handleCompleteRideDriver(){
        System.out.println("\n--- Complete Your Current Ride ---");
        Ride currentRide = getDriverCurrentRide(loggedInUser.getUserId());

        if(currentRide == null) {
            System.out.println("! You do not have an active ride to complete at this moment.");
            return;
        }
        if(currentRide.getStatus() != RideStatus.IN_PROGRESS) {
            System.out.println("! Ride must be 'IN_PROGRESS' to be eligible for completion. Current status: " + currentRide.getStatus());
            System.out.println("If the rider is on board, please update ride status to IN_PROGRESS first (Option 10).");
            return;
        }

        System.out.println("You are about to complete Ride ID: " + currentRide.getRideId());
        // Accessing location details via getters
        String pickupAddress = currentRide.getPickupLocation() != null ? currentRide.getPickupLocation().addressName : "N/A";
        String dropoffAddress = currentRide.getDropoffLocation() != null ? currentRide.getDropoffLocation().addressName : "N/A";
        System.out.println("  From: " + pickupAddress + " To: " + dropoffAddress);


        double distance = getDoubleInput("Enter Actual Distance Traveled for this trip (in km, e.g., 12.5):");
        if (distance <= 0) { System.out.println("! Distance must be a positive value."); return; }
        double duration = getDoubleInput("Enter Actual Duration of this Trip (in minutes, e.g., 35):");
        if (duration <= 0) { System.out.println("! Duration must be a positive value."); return; }

        platform.completeRide(currentRide.getRideId(), distance, duration, loggedInUser.getUserId());
    }

    private static void handleViewMyRideHistoryDriver() {
        System.out.println("\n--- Your Driving History (" + loggedInUser.getName() + ") ---");
        List<Ride> history = platform.getDriverHistory(loggedInUser.getUserId());
        if(history.isEmpty()) {
            System.out.println("You have no driving history recorded yet.");
        } else {
            System.out.println("Displaying your past drives:");
            for(Ride ride : history) {
                System.out.println(ride);
            }
        }
    }

    private static void handleRateRider() {
        System.out.println("\n--- Rate a Rider from a Past Ride ---");
        List<Ride> completedRidesToRate = platform.getDriverHistory(loggedInUser.getUserId()).stream()
                .filter(r -> r.getStatus() == RideStatus.COMPLETED)
                .filter(r -> !platform.getAllRatings().stream().anyMatch(
                                rt -> rt.getRideId().equals(r.getRideId()) &&
                                      rt.getRatedByUserId().equals(loggedInUser.getUserId()) &&
                                      rt.getRatedUserId().equals(r.getRiderId())))
                .collect(Collectors.toList());

        if(completedRidesToRate.isEmpty()){
            System.out.println("You have no completed rides that are pending your rating for the rider.");
            return;
        }

        System.out.println("Select a completed ride to rate the rider:");
        for (int i = 0; i < completedRidesToRate.size(); i++) {
            Ride r = completedRidesToRate.get(i);
            User rider = platform.getUserById(r.getRiderId());
            System.out.println((i + 1) + ". Ride ID: " + r.getRideId() +
                               " (Completed: " + (r.getEndTime() != null ? r.getEndTime().toLocalDate() : "N/A") + ")" +
                               " with Rider: " + (rider != null ? rider.getName() : "N/A"));
        }

        int rideChoice = getIntInput("Enter the number of the ride to rate (or 0 to cancel):");
        if (rideChoice <= 0 || rideChoice > completedRidesToRate.size()) {
            System.out.println("Rating cancelled or invalid selection.");
            return;
        }
        Ride rideToRate = completedRidesToRate.get(rideChoice - 1);
        User riderToRate = platform.getUserById(rideToRate.getRiderId());
        if (riderToRate == null) { System.out.println("! Rider for this ride not found. Cannot rate."); return; }

        System.out.println("You are rating Rider: " + riderToRate.getName() + " for Ride ID: " + rideToRate.getRideId());
        int score = getIntInput("Enter your rating for the rider (1=Poor, 5=Excellent):");
        if (score < 1 || score > 5) { System.out.println("! Invalid score. Rating must be between 1 and 5."); return; }
        String comment = getStringInput("Add a comment (optional, press Enter to skip):");

        platform.addRating(rideToRate.getRideId(), loggedInUser.getUserId(), rideToRate.getRiderId(), score, comment);
    }

    private static void viewSystemState() {
        System.out.println("\n--- Current System State (Admin/Debug View) ---");
        System.out.println("\n[Users (" + platform.getAllUsers().size() + ")]");
        if (platform.getAllUsers().isEmpty()) System.out.println("  No users registered in the system.");
        else platform.getAllUsers().forEach(u -> {
            String extraInfo = "";
            if (u instanceof Driver) {
                Driver d = (Driver)u;
                extraInfo = " | Status: "+ d.getStatus() +
                            (d.getCurrentLocation() != null ? " | Loc: " + d.getCurrentLocation() : "") +
                            " | Vehicle: " + (d.getVehicle() != null ? d.getVehicle().getLicensePlate() : "None");
            } else if (u instanceof Rider) {
                Rider r = (Rider)u;
                extraInfo = " | Pref. Payment: " + (r.getPreferredPaymentMethod() !=null && !r.getPreferredPaymentMethod().isEmpty() ? r.getPreferredPaymentMethod() : "Not set");
            }
            System.out.println("  " + u + extraInfo);
        });

        System.out.println("\n[Vehicles (" + platform.getAllVehicles().size() + ")]");
        if (platform.getAllVehicles().isEmpty()) System.out.println("  No vehicles registered in the system.");
        else platform.getAllVehicles().forEach(v -> {
            // Assuming Vehicle has a driverId field
            String driverId = "Unknown"; // Default if driverId field doesn't exist or is not accessible
            // If your Vehicle class has a getDriverId() method or public driverId field:
            // driverId = v.driverId; // or v.getDriverId();
            System.out.println("  " + v + " (DriverID: " + v.driverId + ")"); // Corrected to use the actual field if it exists
        });


        System.out.println("\n[Active Ride Requests (" + platform.getActiveRideRequests().size() + ") - Searching or Pending]");
        if (platform.getActiveRideRequests().isEmpty()) System.out.println("  No ride requests currently active (searching/pending).");
        else platform.getActiveRideRequests().forEach(r -> System.out.println("  " + r));

        System.out.println("\n[Ongoing Rides (" + platform.getOngoingRides().size() + ") - Not Completed/Cancelled]");
        if (platform.getOngoingRides().isEmpty()) System.out.println("  No rides currently ongoing.");
        else platform.getOngoingRides().forEach(r -> System.out.println("  " + r));

        System.out.println("\n[All Rides (Incl. Completed/Cancelled) (" + platform.getOngoingAndCompletedRides().size() + ")]");
        if (platform.getOngoingAndCompletedRides().isEmpty()) System.out.println("  No rides recorded in the system at all.");
        else platform.getOngoingAndCompletedRides().forEach(r -> System.out.println("  " + r));

        System.out.println("\n[Payment Transactions (" + platform.getAllPaymentTransactions().size() + ")]");
        if (platform.getAllPaymentTransactions().isEmpty()) System.out.println("  No payment transactions recorded.");
        else platform.getAllPaymentTransactions().forEach(pt -> System.out.println("  " + pt));

        System.out.println("\n[Ratings (" + platform.getAllRatings().size() + ")]");
        if (platform.getAllRatings().isEmpty()) System.out.println("  No ratings submitted in the system.");
        else platform.getAllRatings().forEach(r -> System.out.println("  " + r));
        System.out.println("--- End of System State ---");
    }
}