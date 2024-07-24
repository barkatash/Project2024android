package com.example.youtube.entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String userDisplayName;
    private String password;
    private String imageUrl = null;
    private List<String> likedVideos = new ArrayList<>();

    public User() {}

    private List<String> unLikedVideos = new ArrayList<>();
    private List<Integer> likedComments = new ArrayList<>();
    private List<Integer> unLikedComments = new ArrayList<>();

    public List<String> getUnLikedVideos() {
        return unLikedVideos;
    }

    public void setUnLikedVideos(List<String> unLikedVideos) {
        this.unLikedVideos = unLikedVideos;
    }

    public List<Integer> getLikedComments() {
        return likedComments;
    }

    public void setLikedComments(List<Integer> likedComments) {
        this.likedComments = likedComments;
    }

    public List<Integer> getUnLikedComments() {
        return unLikedComments;
    }

    public void setUnLikedComments(List<Integer> unLikedComments) {
        this.unLikedComments = unLikedComments;
    }

    public List<String> getLikedVideos() {
        return likedVideos;
    }

    public void setLikedVideos(List<String> likedVideos) {
        this.likedVideos = likedVideos;
    }

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
