// File: Rider.java
public class Rider extends User {
    String preferredPaymentMethod;

    public Rider(String name, String phone, String password) {
        super(name, phone, password, UserType.RIDER);
    }

    public void setPreferredPaymentMethod(String method) {
        this.preferredPaymentMethod = method;
    }

    public String getPreferredPaymentMethod() {
        return preferredPaymentMethod;
    }
}