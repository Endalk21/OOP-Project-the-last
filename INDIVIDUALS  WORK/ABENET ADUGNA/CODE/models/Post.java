package models;

import java.util.*;

public class Post {
    private static int counter = 1;
    private int id;
    private User author;
    private String caption;
    private List<Media> mediaList = new ArrayList<>();
    private Date timestamp;
    private List<Comment> comments = new ArrayList<>();
    private Set<User> likes = new HashSet<>();
    private int shareCount;

    public Post(User author, String caption) {
        this.id = counter++;
        this.author = author;
        this.caption = caption;
        this.timestamp = new Date();
    }

    public void like(User user) {
        likes.add(user);
        author.receiveNotification(new Notification(user.getUsername() + " liked your post."));
    }

    public void comment(User user, String message) {
        comments.add(new Comment(user, message));
        author.receiveNotification(new Notification(user.getUsername() + " commented on your post."));
    }

    public void addMedia(Media media) {
        mediaList.add(media);
    }

    public void share(User user) {
        shareCount++;
        author.receiveNotification(new Notification(user.getUsername() + " shared your post."));
    }

    public int getShareCount() {
        return shareCount;
    }

    public User getAuthor() {
        return author;
    }

    public String getCaption() {
        return caption;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "Post by " + author.getUsername() + ": " + caption;
    }
}
