import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

// Payment Class
class Payment {
    private static AtomicInteger paymentCounter = new AtomicInteger(1);
    private String paymentID;
    private String orderID;
    private double amount;
    private String method;
    private String status;
    private Date timestamp;

    public Payment(String orderID, double amount, String method) {
        this.paymentID = String.valueOf(paymentCounter.getAndIncrement());
        this.orderID = orderID;
        this.amount = amount;
        this.method = method;
        this.status = "Pending"; // Initial status
        this.timestamp = new Date();
    }

    public String getPaymentID() { return paymentID; }
    public String getOrderID() { return orderID; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public Date getTimestamp() { return timestamp; } // Added getter for timestamp

    public void setStatus(String status) { this.status = status; } // Added setter for status
}