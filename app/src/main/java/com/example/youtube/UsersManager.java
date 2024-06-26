package com.example.youtube;

import java.util.ArrayList;
import java.util.List;
import com.example.youtube.entities.User;

public class UsersManager {
    private static UsersManager instance;
    private static List<User> users;
    private User loggedInUser = null;

    private UsersManager() {
        // Initialize the user list (simulate database)
        users = new ArrayList<>();
        // Example users with raw resource URIs
        users.add(new User("sagi", "sasa", "123789456", "android.resource://com.example.youtube/" + R.raw.user1));
        users.add(new User("chen", "che", "123123123", "android.resource://com.example.youtube/" + R.raw.user2));
        users.add(new User("amit", "ami", "12121212", "android.resource://com.example.youtube/" + R.raw.user3));
    }

    public static synchronized UsersManager getInstance() {
        if (instance == null) {
            instance = new UsersManager();
        }
        return instance;
    }

    // Simulated user database methods

    public List<User> getUsers() {
        return users;
    }

    public static void addUser(User user) {
        users.add(user);
    }

    // User authentication methods

    public boolean loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                return true;
            }
        }
        return false;
    }

    public void logoutUser() {
        loggedInUser = null;
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public User getUserByName(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
