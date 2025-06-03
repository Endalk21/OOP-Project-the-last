import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// Restaurant Class
class Restaurant {
    private static AtomicInteger restaurantCounter = new AtomicInteger(1);
    private String restaurantID;
    private String name;
    private String location;
    private String cuisineType;
    private String contact;
    private String workingHours;
    private List<MenuItem> menuItems;

    public Restaurant(String name, String location, String cuisineType, String contact, String workingHours) {
        this.restaurantID = String.valueOf(restaurantCounter.getAndIncrement());
        this.name = name;
        this.location = location;
        this.cuisineType = cuisineType;
        this.contact = contact;
        this.workingHours = workingHours;
        this.menuItems = new ArrayList<>();
    }

    public void addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
    }

    public String getRestaurantID() { return restaurantID; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public List<MenuItem> getMenuItems() { return menuItems; }
    // Potentially add: public String getCuisineType(), getContact(), getWorkingHours()
}
