package com.example.youtube.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private String id;
    private String videoId;
    private String userName;
    private String description;
    private String uploadDate;
    private int likes;
    private int dislikes;



    public Comment(String videoId, String userName, String description, String uploadDate , int likes, int dislikes) {
        this.userName = userName;
        this.videoId = videoId;
        this.description = description;
        this.likes = likes;
        this.uploadDate = uploadDate;
        this.dislikes = dislikes;
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

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
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
