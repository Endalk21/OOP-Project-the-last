package models;

import utils.Role;
import java.util.*;

public class User {
    private String username;
    private String name;
    private String email;
    private String password;
    private String bio;
    private String profilePictureUrl;
    private Role role;
    private PrivacySettings privacySettings;
    private Set<User> followers = new HashSet<>();
    private Set<User> following = new HashSet<>();
    private List<Post> posts = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();

    public User(String username, String name, String email, String password, Role role) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.privacySettings = new PrivacySettings();
    }

    public void follow(User user) {
        following.add(user);
        user.followers.add(this);
        user.receiveNotification(new Notification(this.username + " followed you."));
    }

    public List<Post> getFeed() {
        List<Post> feed = new ArrayList<>();
        for (User user : following) {
            feed.addAll(user.posts);
        }
        feed.sort(Comparator.comparing(Post::getTimestamp).reversed());
        return feed;
    }

    public void createPost(Post post) {
        posts.add(post);
    }

    public void receiveNotification(Notification notification) {
        notifications.add(notification);
    }

    public String getUsername() {
        return username;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    @Override
    public String toString() {
        return username + " (" + name + ")";
    }
}
