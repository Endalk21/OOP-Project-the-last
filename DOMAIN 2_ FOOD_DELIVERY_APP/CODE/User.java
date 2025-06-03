import java.util.concurrent.atomic.AtomicInteger;

// User Class
class User {
    private static AtomicInteger idCounter = new AtomicInteger(1);
    private String userID;
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.userID = String.valueOf(idCounter.getAndIncrement());
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getPassword() { return password; } // Note: Storing passwords in plaintext is insecure
    public String getRole() { return role; }
}