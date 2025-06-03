import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

// Order Class
class Order {
    private static AtomicInteger orderCounter = new AtomicInteger(1);
    private String orderID;
    private String customerID;
    private String restaurantID;
    private String deliveryPersonnelID; // Can be null initially
    private Date orderTime;
    private String status;
    private double totalAmount;

    public Order(String customerID, String restaurantID, String deliveryPersonnelID, double totalAmount) {
        this.orderID = String.valueOf(orderCounter.getAndIncrement());
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.deliveryPersonnelID = deliveryPersonnelID;
        this.orderTime = new Date();
        this.status = "Pending"; // Initial status
        this.totalAmount = totalAmount;
    }

    public String getOrderID() { return orderID; }
    public String getCustomerID() { return customerID; }
    public String getRestaurantID() { return restaurantID; }
    public String getDeliveryPersonnelID() { return deliveryPersonnelID; }
    public Date getOrderTime() { return orderTime; }
    public String getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }

    public void setStatus(String status) { this.status = status; } // Added setter for status
    public void setDeliveryPersonnelID(String deliveryPersonnelID) { this.deliveryPersonnelID = deliveryPersonnelID; } // Added setter
}