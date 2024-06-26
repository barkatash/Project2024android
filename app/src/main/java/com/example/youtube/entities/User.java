package com.example.youtube.entities;

import com.example.youtube.R;

public class User {
    private String username;
    private String userDisplayName;
    private String password;
    private int imageUri = 0;

    // Constructor
    public User(String username, String userDisplayName, String password, int imageUri) {
        this.username = username;
        this.userDisplayName = userDisplayName;
        this.password = password;
        this.imageUri = imageUri;
    }
    public User(String username, String userDisplayName, String password) {
        this.username = username;
        this.userDisplayName = userDisplayName;
        this.password = password;
        this.imageUri = R.drawable.baseline_account_circle_24;
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

    public int getImageUri() {
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

    public void setImageUri(int imageUri) {
        this.imageUri = imageUri;
    }
}
