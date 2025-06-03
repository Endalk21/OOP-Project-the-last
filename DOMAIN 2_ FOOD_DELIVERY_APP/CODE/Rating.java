import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

// Rating Class
class Rating {
    private static AtomicInteger ratingCounter = new AtomicInteger(1);
    private String ratingID;
    private String userID;
    private String targetType; // e.g., "Restaurant", "MenuItem", "DeliveryPersonnel"
    private String targetID;
    private int score; // e.g., 1-5
    private String comment; // Optional comment
    private Date time;

    public Rating(String userID, String targetType, String targetID, int score, String comment) {
        this.ratingID = String.valueOf(ratingCounter.getAndIncrement());
        this.userID = userID;
        this.targetType = targetType;
        this.targetID = targetID;
        this.score = score;
        this.comment = comment;
        this.time = new Date();
    }
    // Constructor without comment
    public Rating(String userID, String targetType, String targetID, int score) {
        this(userID, targetType, targetID, score, null);
    }


    public String getRatingID() { return ratingID; }
    public String getUserID() { return userID; }
    public String getTargetType() { return targetType; }
    public String getTargetID() { return targetID; }
    public int getScore() { return score; }
    public String getComment() { return comment; }
    public Date getTime() { return time; }
}