package com.example.youtube.entities;

public class User {
    private String username;
    private String email;
    private String password;
    private String imageUri;

    // Constructor, getters, setters, etc.

    public User(String username, String email, String password, String imageUri) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.imageUri = imageUri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
