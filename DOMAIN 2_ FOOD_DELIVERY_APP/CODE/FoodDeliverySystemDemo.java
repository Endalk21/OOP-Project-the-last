import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
// No need for Date directly here

// FoodDeliverySystemDemo Class
class FoodDeliverySystemDemo {
    private FoodDeliveryPlatform platform;
    private Scanner scanner; // Make Scanner an instance variable

    public FoodDeliverySystemDemo() {
        this.platform = new FoodDeliveryPlatform();
        this.scanner = new Scanner(System.in); // Initialize scanner here
    }

    public void start() {
        // Scanner scanner = new Scanner(System.in); // Removed local scanner
        while (true) {
            if (platform.getLoggedInUser() == null) {
                showMainMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. View Restaurants & Menus");
        System.out.println("0. Exit");
        int choice = getInput("Enter your choice: ");

        switch (choice) {
            case 1:
                platform.registerUser(scanner);
                break;
            case 2:
                if (platform.loginUser(scanner)) {
                    System.out.println("Login successful! Welcome, " + platform.getLoggedInUser().getUsername());
                }
                break;
            case 3:
                viewRestaurantsAndMenus();
                break;
            case 0:
                System.out.println("Exiting...");
                scanner.close(); // Close scanner on exit
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void showUserMenu() {
        User user = platform.getLoggedInUser();
        System.out.println("\n--- Welcome " + user.getUsername() + " (" + user.getRole() + ") ---");
        if (user instanceof Customer) {
            showCustomerMenu();
        } else if (user instanceof RestaurantOwner) {
            showRestaurantOwnerMenu();
        } else if (user instanceof DeliveryPersonnel) {
            showDeliveryPersonnelMenu();
        }
    }

    private void showCustomerMenu() {
        System.out.println("--- Customer Menu ---");
        System.out.println("1. View Restaurants & Menus");
        System.out.println("2. Place Order");
        System.out.println("3. View My Orders");
        System.out.println("4. Logout");
        int choice = getInput("Enter your choice: ");

        switch (choice) {
            case 1:
                viewRestaurantsAndMenus();
                break;
            case 2:
                placeOrder();
                break;
            case 3:
                viewMyOrders();
                break;
            case 4:
                platform.logoutUser();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void showRestaurantOwnerMenu() {
        System.out.println("--- Restaurant Owner Menu ---");
        System.out.println("1. Add Menu Item to My Restaurant");
        System.out.println("2. View Orders for My Restaurant");
        // System.out.println("3. Manage My Restaurant Profile"); // Future
        // System.out.println("4. Create Restaurant (if none exists)"); // Future
        System.out.println("5. Logout");
        int choice = getInput("Enter your choice: ");

        RestaurantOwner owner = (RestaurantOwner) platform.getLoggedInUser();

        switch (choice) {
            case 1:
                addMenuItemToRestaurant(owner);
                break;
            case 2:
                viewRestaurantOrders(owner);
                break;
            case 5:
                platform.logoutUser();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }


    private void showDeliveryPersonnelMenu() {
        System.out.println("--- Delivery Personnel Menu ---");
        System.out.println("1. View Available Orders for Pickup");
        System.out.println("2. View My Assigned Deliveries");
        System.out.println("3. Update Delivery Status");
        System.out.println("4. Logout");
        int choice = getInput("Enter your choice: ");

        // DeliveryPersonnel dp = (DeliveryPersonnel) platform.getLoggedInUser();

        switch (choice) {
            case 1:
                System.out.println("View Available Orders: Not Implemented Yet");
                // viewAvailableOrdersForPickup(dp);
                break;
            case 2:
                System.out.println("View My Assigned Deliveries: Not Implemented Yet");
                // viewMyAssignedDeliveries(dp);
                break;
            case 3:
                System.out.println("Update Delivery Status: Not Implemented Yet");
                // updateDeliveryStatus(dp);
                break;
            case 4:
                platform.logoutUser();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void viewRestaurantsAndMenus() {
        System.out.println("\n--- Available Restaurants & Menus ---");
        List<Restaurant> restaurants = platform.getRestaurants();
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants available at the moment.");
            return;
        }
        for (Restaurant restaurant : restaurants) {
            System.out.println("\nRestaurant ID: " + restaurant.getRestaurantID() + " - " + restaurant.getName() + " (" + restaurant.getLocation() + ")");
            List<MenuItem> menuItems = restaurant.getMenuItems();
            if (menuItems.isEmpty()) {
                System.out.println("  No menu items available for this restaurant.");
            } else {
                System.out.println("  Menu Items:");
                for (MenuItem menuItem : menuItems) {
                    System.out.println("    ID: " + menuItem.getItemID() + " - " + menuItem.getName() + " ($" + String.format("%.2f", menuItem.getPrice()) + ")");
                    System.out.println("      " + menuItem.getDescription());
                }
            }
        }
    }

    private void placeOrder() {
        System.out.println("\n--- Place an Order ---");
        User customer = platform.getLoggedInUser();
        if (!(customer instanceof Customer)) {
            System.out.println("Only customers can place orders.");
            return;
        }

        viewRestaurantsAndMenus(); // Show restaurants first
        System.out.print("Enter Restaurant ID to order from: ");
        String restaurantID = scanner.nextLine();
        Restaurant selectedRestaurant = platform.findRestaurantById(restaurantID);

        if (selectedRestaurant == null) {
            System.out.println("Invalid Restaurant ID.");
            return;
        }

        System.out.println("Ordering from: " + selectedRestaurant.getName());
        List<MenuItem> itemsForOrder = new ArrayList<>();
        double totalAmount = 0.0;

        while (true) {
            System.out.println("\n--- " + selectedRestaurant.getName() + " Menu ---");
             for (MenuItem menuItem : selectedRestaurant.getMenuItems()) {
                    System.out.println("    ID: " + menuItem.getItemID() + " - " + menuItem.getName() + " ($" + String.format("%.2f", menuItem.getPrice()) + ")");
            }
            System.out.print("Enter Menu Item ID to add (or 'done' to finish selection): ");
            String menuItemID = scanner.nextLine();

            if (menuItemID.equalsIgnoreCase("done")) {
                break;
            }

            MenuItem item = platform.findMenuItemById(selectedRestaurant, menuItemID);
            if (item != null) {
                itemsForOrder.add(item);
                totalAmount += item.getPrice();
                System.out.println(item.getName() + " added. Current total: $" + String.format("%.2f", totalAmount));
            } else {
                System.out.println("Invalid Menu Item ID. Please try again.");
            }
        }

        if (itemsForOrder.isEmpty()) {
            System.out.println("No items selected. Order cancelled.");
            return;
        }

        // For simplicity, delivery personnel is not assigned at order placement yet.
        Order order = new Order(customer.getUserID(), selectedRestaurant.getRestaurantID(), null, totalAmount);
        platform.addOrder(order);
        System.out.println("Order placed successfully! Order ID: " + order.getOrderID() +
                           ", Total: $" + String.format("%.2f", order.getTotalAmount()));
        // Here you would typically proceed to payment.
    }

    private void viewMyOrders() {
        System.out.println("\n--- Your Orders ---");
        String currentUserID = platform.getLoggedInUser().getUserID();
        boolean hasOrders = false;
        List<Order> allOrders = platform.getOrders();

        for (Order order : allOrders) {
            if (order.getCustomerID().equals(currentUserID)) {
                Restaurant r = platform.findRestaurantById(order.getRestaurantID());
                String restaurantName = (r != null) ? r.getName() : "Unknown Restaurant";
                System.out.println(
                        "Order ID: " + order.getOrderID() +
                        " | Restaurant: " + restaurantName +
                        " | Total: $" + String.format("%.2f", order.getTotalAmount()) +
                        " | Status: " + order.getStatus() +
                        " | Ordered On: " + order.getOrderTime()
                );
                hasOrders = true;
            }
        }
        if (!hasOrders) {
            System.out.println("You have no orders.");
        }
    }

    private void addMenuItemToRestaurant(RestaurantOwner owner) {
        System.out.println("\n--- Add Menu Item ---");
        // This logic needs refinement. A restaurant owner should be linked to their specific restaurant(s).
        // For now, using the flawed original logic or a hardcoded one if "owner1" for demo
        Restaurant managedRestaurant = platform.getRestaurantByOwner(owner);

        if (managedRestaurant == null) {
            System.out.println("You do not seem to manage any restaurant or your restaurant could not be found.");
            System.out.println("DEBUG: Owner username: " + owner.getUsername());
            System.out.println("To create a restaurant, you might need to contact an admin or use a 'create restaurant' feature (not implemented).");
            // A temporary workaround for the "owner1" created in initializeData
            if (owner.getUsername().equals("owner1")) {
                System.out.println("Attempting to find 'Sunny Burger' for owner1...");
                managedRestaurant = platform.findRestaurantById("1"); // Assuming Sunny Burger is ID 1
                 if(managedRestaurant == null) { // If Sunny Burger is not ID 1
                    for(Restaurant r : platform.getRestaurants()){
                        if(r.getName().equals("Sunny Burger")) {
                            managedRestaurant = r;
                            break;
                        }
                    }
                }
            }
            if (managedRestaurant == null) return; // Still no restaurant found
            else System.out.println("Found restaurant: " + managedRestaurant.getName() + " for owner: " + owner.getUsername());
        }

        System.out.println("Adding item to: " + managedRestaurant.getName());
        System.out.print("Enter Menu Item Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Menu Item Description: ");
        String description = scanner.nextLine();
        double price = -1;
        while (price < 0) {
            price = getDoubleInput("Enter Menu Item Price: ");
            if (price < 0) System.out.println("Price cannot be negative.");
        }

        MenuItem newItem = new MenuItem(managedRestaurant.getRestaurantID(), name, description, price);
        managedRestaurant.addMenuItem(newItem);
        System.out.println("Menu item '" + name + "' added successfully to " + managedRestaurant.getName() + "!");
    }

    private void viewRestaurantOrders(RestaurantOwner owner) {
        System.out.println("\n--- Orders for Your Restaurant ---");
        Restaurant managedRestaurant = platform.getRestaurantByOwner(owner);

        if (managedRestaurant == null) {
            System.out.println("You do not seem to manage any restaurant or your restaurant could not be found.");
             if (owner.getUsername().equals("owner1")) { // Temp fix for demo
                managedRestaurant = platform.findRestaurantById("1");
                 if(managedRestaurant == null) {
                    for(Restaurant r : platform.getRestaurants()){
                        if(r.getName().equals("Sunny Burger")) {
                            managedRestaurant = r;
                            break;
                        }
                    }
                }
            }
            if (managedRestaurant == null) return;
        }

        System.out.println("Showing orders for: " + managedRestaurant.getName());
        boolean hasOrders = false;
        List<Order> allOrders = platform.getOrders();

        for (Order order : allOrders) {
            if (order.getRestaurantID().equals(managedRestaurant.getRestaurantID())) {
                System.out.println(
                        "Order ID: " + order.getOrderID() +
                        " | Customer ID: " + order.getCustomerID() +
                        " | Total: $" + String.format("%.2f", order.getTotalAmount()) +
                        " | Status: " + order.getStatus() +
                        " | Ordered On: " + order.getOrderTime()
                );
                hasOrders = true;
            }
        }
        if (!hasOrders) {
            System.out.println("No orders found for your restaurant.");
        }
    }


    private int getInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid price (e.g., 4.99): ");
            }
        }
    }
}