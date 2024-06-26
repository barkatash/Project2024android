package com.example.youtube.entities;

public class User {
    private String username;
    private String userDisplayName;
    private String password;
    private String imageUrl = null; // URL of the profile image

    // Constructor with image URL
    public User(String username, String userDisplayName, String password, String imageUrl) {
        this.username = username;
        this.userDisplayName = userDisplayName;
        this.password = password;
        this.imageUrl = imageUrl;
    }
    public User(String username, String userDisplayName, String password) {
        this.username = username;
        this.userDisplayName = userDisplayName;
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getPassword() {
        return password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setters (optional, depending on your needs)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
