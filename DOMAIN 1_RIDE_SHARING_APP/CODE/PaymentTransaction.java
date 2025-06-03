// File: PaymentTransaction.java
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentTransaction {
    String transactionId;
    String rideId;
    String userId; // Rider's ID
    double amount;
    LocalDateTime timestamp;
    PaymentStatus status;
    String paymentMethodDetails;

    public PaymentTransaction(String rideId, String userId, double amount, String method) {
        this.transactionId = "pay-" + UUID.randomUUID().toString().substring(0, 7);
        this.rideId = rideId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethodDetails = method;
        this.timestamp = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    public void setStatus(PaymentStatus s) { this.status = s; }
    public String getTransactionId() { return transactionId; } // Added getter

    @Override
    public String toString() {
        return "Payment ID: " + transactionId + " for Ride " + rideId + ", User: " + userId +
               ", Amount: $" + String.format("%.2f", amount) + ", Method: " + paymentMethodDetails + ", Status: " + status;
    }
}