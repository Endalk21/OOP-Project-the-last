package app;

import models.*;
import utils.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Create users
        System.out.print("Enter first user username: ");
        String username1 = sc.nextLine();
        System.out.print("Enter name: ");
        String name1 = sc.nextLine();
        System.out.print("Enter email: ");
        String email1 = sc.nextLine();
        System.out.print("Enter password: ");
        String password1 = sc.nextLine();

        User user1 = new User(username1, name1, email1, password1, Role.USER);

        System.out.print("Enter second user username: ");
        String username2 = sc.nextLine();
        System.out.print("Enter name: ");
        String name2 = sc.nextLine();
        System.out.print("Enter email: ");
        String email2 = sc.nextLine();
        System.out.print("Enter password: ");
        String password2 = sc.nextLine();

        User user2 = new User(username2, name2, email2, password2, Role.CREATOR);

        // Follow
        user1.follow(user2);

        // Create post
        System.out.print("Enter post caption: ");
        String caption = sc.nextLine();
        Post post = new Post(user2, caption);

        System.out.print("Enter image URL for the post: ");
        String imgUrl = sc.nextLine();
        post.addMedia(new Image(imgUrl));

        user2.createPost(post);

        // Like and comment
        post.like(user1);
        System.out.print("Enter comment: ");
        String commentMsg = sc.nextLine();
        post.comment(user1, commentMsg);

        // Display feed
        System.out.println("\n--- " + user1.getUsername() + "'s Feed ---");
        for (Post p : user1.getFeed()) {
            System.out.println(p);
            System.out.println("Likes: " + p.getLikes().size());
            System.out.println("Comments: " + p.getComments().size());
        }

        // Notifications
        System.out.println("\n--- " + user2.getUsername() + "'s Notifications ---");
        for (Notification n : user2.getNotifications()) {
            System.out.println(n.getMessage() + " at " + n.getTimestamp());
        }

        sc.close();
    }
}
