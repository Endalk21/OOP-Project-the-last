package models;

import java.util.*;

public class Notification {
    private String message;
    private Date timestamp;
    private boolean isRead;

    public Notification(String message) {
        this.message = message;
        this.timestamp = new Date();
        this.isRead = false;
    }

    public void markAsRead() {
        isRead = true;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}