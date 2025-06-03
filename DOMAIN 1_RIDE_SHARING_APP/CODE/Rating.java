// File: Rating.java
import java.time.LocalDateTime;
import java.util.UUID;

public class Rating {
    String ratingId;
    String rideId;
    String ratedByUserId;
    String ratedUserId;
    int score;
    String comment;
    LocalDateTime timestamp;

    public Rating(String rideId, String byId, String forId, int score, String comment) {
        this.ratingId = "rate-" + UUID.randomUUID().toString().substring(0, 5);
        this.rideId = rideId;
        this.ratedByUserId = byId;
        this.ratedUserId = forId;
        this.score = score;
        this.comment = (comment == null || comment.isEmpty()) ? "No comment" : comment;
        this.timestamp = LocalDateTime.now();
    }

    public String getRideId() { return rideId; }
    public String getRatedByUserId() { return ratedByUserId; }
    public String getRatedUserId() { return ratedUserId; }


    @Override
    public String toString() {
        return "Rating for Ride " + rideId + ": " + score + " stars by User " + ratedByUserId +
               " for User " + ratedUserId + ". Comment: " + comment;
    }
}