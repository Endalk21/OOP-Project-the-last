import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// No need for Date or AtomicInteger directly here if not creating new instances of them.

// FoodDeliveryPlatform Class
class FoodDeliveryPlatform {
    private List<User> users;
    private List<Restaurant> restaurants;
    private List<Order> orders;
    // private List<Payment> payments; // Consider adding for tracking payments
    // private List<Rating> ratings;   // Consider adding for tracking ratings
    private User loggedInUser;

    public FoodDeliveryPlatform() {
        this.users = new ArrayList<>();
        this.restaurants = new ArrayList<>();
        this.orders = new ArrayList<>();
        // this.payments = new ArrayList<>();
        // this.ratings = new ArrayList<>();
        initializeData();
    }

    public void initializeData() {
        // Sample restaurants and items
        Restaurant restaurant1 = new Restaurant("Sunny Burger", "Hawassa", "Ethiopian", "0911458694", "8AM-12AM");
        restaurant1.addMenuItem(new MenuItem(restaurant1.getRestaurantID(), "Special Burger", "Unforgettable taste of burger", 5.99));
        restaurant1.addMenuItem(new MenuItem(restaurant1.getRestaurantID(), "Beef Burger", "The best beef burger in the entire city", 3.45));
        restaurant1.addMenuItem(new MenuItem(restaurant1.getRestaurantID(), "Chicken Burger", "Try our juicy grilled chicken burger", 4.55));
        this.restaurants.add(restaurant1);

        Restaurant restaurant2 = new Restaurant("Rome Pizza house", "Hawassa", "Ethiopian", "0911458694", "8AM-10PM");
        restaurant2.addMenuItem(new MenuItem(restaurant2.getRestaurantID(), "Special Pizza with extra Cheese", "Yummy and Sweet Pizzas with cheese and tomatoes", 4.50));
        restaurant2.addMenuItem(new MenuItem(restaurant2.getRestaurantID(), "Pepperoni Pizza", "Pizza with spicy pepperoni", 3.25));
        restaurant2.addMenuItem(new MenuItem(restaurant2.getRestaurantID(), "Veggie Pizza", "Made with fresh garden veggies", 2.80));
        this.restaurants.add(restaurant2);

        // Sample users
        users.add(new Customer("customer1", "pass123"));
        users.add(new RestaurantOwner("owner1", "pass123")); // Changed username for clarity
        users.add(new DeliveryPersonnel("delivery1", "pass123"));

        // To link owner1 to a restaurant (example, if you add ownerID to Restaurant)
        // For now, the addMenuItem logic for restaurant owner uses username matching restaurant name which is flawed.
        // A better approach:
        // RestaurantOwner owner1 = new RestaurantOwner("owner1", "pass123");
        // users.add(owner1);
        // Restaurant myRestaurant = new Restaurant("Owner1's Eatery", "Some Location", "Various", "contact", "hours", owner1.getUserID());
        // restaurants.add(myRestaurant);
        // This would require Restaurant class to store ownerID.
    }

    public void registerUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (Customer/RestaurantOwner/Delivery): ");
        String role = scanner.nextLine();

        // Check if username already exists
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(username)) {
                System.out.println("Username already exists. Please choose a different one.");
                return;
            }
        }

        User newUser = null;
        switch (role.toLowerCase()) {
            case "customer":
                newUser = new Customer(username, password);
                break;
            case "restaurantowner":
                newUser = new RestaurantOwner(username, password);
                // Optionally, prompt to create a restaurant here or later
                break;
            case "delivery":
                newUser = new DeliveryPersonnel(username, password);
                break;
            default:
                System.out.println("Invalid role. Registration failed.");
                return;
        }
        users.add(newUser);
        System.out.println(role + " registered successfully as " + username + "!");
    }

    public boolean loginUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                return true;
            }
        }
        System.out.println("Invalid username or password.");
        return false;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void logoutUser() {
        loggedInUser = null;
        System.out.println("Logged out successfully.");
    }

    // Method to find a restaurant by its ID
    public Restaurant findRestaurantById(String id) {
        for (Restaurant r : restaurants) {
            if (r.getRestaurantID().equals(id)) {
                return r;
            }
        }
        return null;
    }

    // Method to find a menu item by its ID within a specific restaurant
    public MenuItem findMenuItemById(Restaurant restaurant, String itemId) {
        if (restaurant != null) {
            for (MenuItem mi : restaurant.getMenuItems()) {
                if (mi.getItemID().equals(itemId)) {
                    return mi;
                }
            }
        }
        return null;
    }

     // New method to add a restaurant
    public void addRestaurant(Restaurant restaurant) {
        this.restaurants.add(restaurant);
    }

    // New method to get restaurants by owner (requires ownerID in Restaurant)
    // For now, we'll assume a RestaurantOwner can only manage one restaurant
    // and its name is tied to their username for simplicity (as per original addMenuItem logic flaw)
    public Restaurant getRestaurantByOwner(RestaurantOwner owner) {
        // This is a simplified and potentially flawed way to link.
        // Ideally, Restaurant class should have an ownerID field.
        for (Restaurant r : restaurants) {
            // Example: If restaurant name was set to owner's username during creation
            // Or if you add an ownerID field to Restaurant and match owner.getUserID()
            if (r.getName().toLowerCase().contains(owner.getUsername().toLowerCase())) { // A very loose match
                return r;
            }
        }
        // A better temporary fix for the demo if owner1 owns "Sunny Burger"
        if (owner.getUsername().equals("owner1") && !restaurants.isEmpty()) {
             for (Restaurant r : restaurants) {
                if (r.getName().equals("Sunny Burger")) return r; // Hardcoding for demo
            }
        }
        return null;
    }
}