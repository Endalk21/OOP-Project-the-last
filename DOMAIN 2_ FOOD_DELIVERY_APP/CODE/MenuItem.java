import java.util.concurrent.atomic.AtomicInteger;

// MenuItem Class
class MenuItem {
    private static AtomicInteger menuItemCounter = new AtomicInteger(1);
    private String itemID;
    private String restaurantID;
    private String name;
    private String description;
    private double price;

    public MenuItem(String restaurantID, String name, String description, double price) {
        this.itemID = String.valueOf(menuItemCounter.getAndIncrement());
        this.restaurantID = restaurantID;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getItemID() { return itemID; }
    public String getRestaurantID() { return restaurantID; }
    public String getName() { return name; }
    public String getDescription() { return description; } // Added getter for description
    public double getPrice() { return price; }
}