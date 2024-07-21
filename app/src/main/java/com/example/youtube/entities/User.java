package com.example.youtube.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private List<Video> likedVideo;
    private List<Video> unLikedVideo;

    private String username;
    private String userDisplayName;
    private String password;
    private String imageUrl;

    // Constructor, Getters, and Setters
    public User(String username, String userDisplayName, String password, String imageUrl) {
        this.username = username;
        this.userDisplayName = userDisplayName;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void addLikedVideo(Video video) {
        likedVideo.add(video);
    }
    public List<Video> getLikedVideos() {
        return likedVideo;
    }
    public void addUnLikedVideo(Video video) {
        unLikedVideo.add(video);
    }
    public List<Video> getUnLikedVideos() {
        return unLikedVideo;
    }

    public void setLikedVideos(List<Video> newLikedVideos) {
        likedVideo = newLikedVideos;
    }
    public void setUnLikedVideos(List<Video> newUnLikedVideos) {
        unLikedVideo = newUnLikedVideos;
    }
}
