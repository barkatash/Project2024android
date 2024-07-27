package com.example.youtube.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import java.util.List;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "liked_video_ids")
    private List<Integer> likedVideoIds;

    @ColumnInfo(name = "unliked_video_ids")
    private List<Integer> unLikedVideoIds;

    private String username;
    private String userDisplayName;
    private String password;
    private String imageUrl;

    // Constructor for Room
    public User(int id, String username, String userDisplayName, String password, String imageUrl, List<Integer> likedVideoIds, List<Integer> unLikedVideoIds) {
        this.id = id;
        this.username = username;
        this.userDisplayName = userDisplayName;
        this.password = password;
        this.imageUrl = imageUrl;
        this.likedVideoIds = likedVideoIds;
        this.unLikedVideoIds = unLikedVideoIds;
    }

    // Constructor for login purpose
    @Ignore
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Full constructor
    @Ignore
    public User(String username, String userDisplayName, String password, String imageUrl) {
        this.username = username;
        this.userDisplayName = userDisplayName;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    @Ignore
    public User(String username, String userDisplayName, String password) {
        this.username = username;
        this.userDisplayName = userDisplayName;
        this.password = password;
        this.imageUrl = null;
    }

    // Getters and Setters
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

    public List<Integer> getLikedVideoIds() {
        return likedVideoIds;
    }

    public void setLikedVideoIds(List<Integer> likedVideoIds) {
        this.likedVideoIds = likedVideoIds;
    }

    public List<Integer> getUnLikedVideoIds() {
        return unLikedVideoIds;
    }

    public void setUnLikedVideoIds(List<Integer> unLikedVideoIds) {
        this.unLikedVideoIds = unLikedVideoIds;
    }
}
