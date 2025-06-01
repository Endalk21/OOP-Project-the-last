package models;

import java.util.*;

public class Comment {
    private User commenter;
    private String message;
    private Date timestamp;
    private List<Comment> replies = new ArrayList<>();

    public Comment(User commenter, String message) {
        this.commenter = commenter;
        this.message = message;
        this.timestamp = new Date();
    }

    public void reply(Comment comment) {
        replies.add(comment);
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public User getCommenter() {
        return commenter;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
