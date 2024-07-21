package com.example.youtube.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "authorId",
        onDelete = ForeignKey.CASCADE))
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Video video;
    private User author; // Foreign key to User
    private String description;
    private String uploadDate;
    private int likes;
    private int dislikes;

    // Constructor, Getters, and Setters
    public Comment(Video video, User author, String description, String uploadDate, int likes, int dislikes) {
        this.video = video;
        this.author = author;
        this.description = description;
        this.uploadDate = uploadDate;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public int getUnlikes() { return dislikes; }

    public void setUnlikes(int unlikes) { this.dislikes = unlikes; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideoId(Video videoId) {
        this.video = videoId;
    }

    public void setAuthor(User author) {
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
    public User getUser() {
        return author;
    }
}
