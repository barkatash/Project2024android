package com.example.youtube.entities;
public class User {
    private String username;
    private String userDisplayName;
    private String password;
    private String imageUri;

    // Constructor
    public User(String username, String userDisplayName, String password, String imageUri) {
        this.username = username;
        this.userDisplayName = userDisplayName;
        this.password = password;
        this.imageUri = imageUri;
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

    public String getImageUri() {
        return imageUri;
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

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
