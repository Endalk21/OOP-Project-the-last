package app;

import models.*;
import utils.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        User alice = new User("alice123", "Alice Smith", "alice@example.com", "pass123", Role.USER);
        User bob = new User("bob321", "Bob Jones", "bob@example.com", "bobpass", Role.CREATOR);

        alice.follow(bob);

        Post bobPost = new Post(bob, "Enjoying the sunshine today!");
        bobPost.addMedia(new Image("https://example.com/sun.jpg"));
        bob.createPost(bobPost);

        bobPost.like(alice);
        bobPost.comment(alice, "Looks beautiful!");

        System.out.println("\n--- Alice's Feed ---");
        for (Post post : alice.getFeed()) {
            System.out.println(post);
            System.out.println("Likes: " + post.getLikes().size());
            System.out.println("Comments: " + post.getComments().size());
        }

        System.out.println("\n--- Bob's Notifications ---");
        for (Notification notification : bob.getNotifications()) {
            System.out.println(notification.getMessage() + " at " + notification.getTimestamp());
        }
    }
}
