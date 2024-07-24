package com.example.youtube.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "authorId",
        onDelete = ForeignKey.CASCADE))
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Integer videoId;
    private Integer authorId;
    private String description;
    private String uploadDate;
    private int likes;
    private int dislikes;

    // No-argument constructor
    public Comment() {
    }

    // Constructor with all fields
    public Comment(Integer videoId, Integer authorId, String description, String uploadDate, int likes, int dislikes) {
        this.videoId = videoId;
        this.authorId = authorId;
        this.description = description;
        this.uploadDate = uploadDate;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
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
