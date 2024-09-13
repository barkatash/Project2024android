package com.example.youtube.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Comment {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;
    private String videoId;
    private String userName;
    private String description;
    private int likes;
    private int dislikes;



    public Comment(String videoId, String userName, String description, int likes, int dislikes) {
        this.userName = userName;
        this.videoId = videoId;
        this.description = description;
        this.likes = likes;
        this.dislikes = dislikes;
    }
    @Ignore
    public Comment(String videoId, String userName, String description) {
        this.userName = userName;
        this.videoId = videoId;
        this.description = description;
        this.likes = 0;
        this.dislikes = 0;
    }

    public String getUserName() {
        return userName;
    }
    public void setUsername(String username) {
        this.userName = username;
    }
    public int getUnlikes() { return dislikes; }

    public void setUnlikes(int unlikes) { this.dislikes = unlikes; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
    public String getUsername() {
        return userName;
    }
}
