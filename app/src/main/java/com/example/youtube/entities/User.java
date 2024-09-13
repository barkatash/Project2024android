package com.example.youtube.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String displayName;
    private String password;

    @SerializedName("image")
    private String imageUrl = null;

    @SerializedName("videoIdListLiked")
    private List<String> likedVideos = new ArrayList<>();
    private List<String> videoIdListUnliked = new ArrayList<>();
    private List <String> commentIdListLiked = new ArrayList<>();
    private List <String> commentIdListUnliked = new ArrayList<>();
    private String token;

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

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public User(String username, String displayName, String password, String imageUrl) {
        this.username = username;
        this.displayName = displayName;
        this.password = password;
        this.imageUrl = imageUrl;
    }
    public User(String username, String displayName, String password) {
        this.username = username;
        this.displayName = displayName;
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
