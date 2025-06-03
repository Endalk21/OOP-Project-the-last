// File: User.java
import java.util.Objects;
import java.util.UUID;

public abstract class User {
    String userId;
    String name;
    String phoneNumber;
    String passwordHash;
    UserType userType;
    double averageRating;
    int ratingCount;

    public User(String name, String phone, String password, UserType type) {
        this.userId = type.name().toLowerCase() + "-" + UUID.randomUUID().toString().substring(0, 4);
        this.name = name;
        this.phoneNumber = phone;
        this.passwordHash = "hashed-" + password; // Simple hashing for example
        this.userType = type;
        this.averageRating = 0.0;
        this.ratingCount = 0;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public UserType getUserType() { return userType; }
    public String getPhoneNumber() { return phoneNumber; }

    public boolean checkPassword(String pass) {
        return ("hashed-" + pass).equals(this.passwordHash);
    }

    public void addRating(int score) {
        averageRating = (averageRating * ratingCount + score) / (double) (ratingCount + 1);
        ratingCount++;
    }

    public double getAverageRating() {
        return averageRating;
    }

    @Override
    public String toString() {
        return userType + ": " + name + " (ID: " + userId + ", Rating: " + String.format("%.1f", averageRating) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}