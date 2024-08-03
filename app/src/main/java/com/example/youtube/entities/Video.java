package com.example.youtube.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Video {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;
    private String uploader;
    private String video;
    private String title;
    private int likes;
    private String image;
    private String duration;
    private int visits;


    public Video() {
    }


    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    private String uploadDate;

    public Video(String id, String uploader, String title, String duration , int visits, String uploadDate, String image, String video) {
        this.id = id;
        this.uploader = uploader;
        this.title = title;
        this.image = image;
        this.duration = duration;
        this.visits = visits;
        this.uploadDate = uploadDate;
        this.video = video;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

}
