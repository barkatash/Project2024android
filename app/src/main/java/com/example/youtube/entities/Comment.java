package com.example.youtube.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int videoId;
    private String author;
    private String description;
    private String uploadDate;
    private int likes;
    private int dislikes;
    private boolean liked;
    private boolean unliked;


    public Comment(int videoId, String author, String description, String uploadDate , int likes, int dislikes) {
        this.author = author;
        this.videoId = videoId;
        this.description = description;
        this.likes = likes;
        this.uploadDate = uploadDate;
        this.dislikes = dislikes;
        this.liked = false;
        this.unliked = false;
    }

    public boolean isLiked() { return liked; }

    public boolean isUnliked() {
        return unliked;
    }

    public int getUnlikes() { return dislikes; }

    public void setUnlikes(int unlikes) { this.dislikes = unlikes; }
    public void setUnliked(boolean unliked) {
        this.unliked = unliked;
    }

    public void setLiked(boolean liked) { this.liked = liked; }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
}
