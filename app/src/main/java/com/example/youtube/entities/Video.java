package com.example.youtube.entities;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Video {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String author;
    private int video;
    private String content;
    private int likes;
    private int pic;
    private String duration;
    private String views;

    private Bitmap thumbnailBitmap;
    private Uri videoFileUri;
    private Uri imageFileUri;
    private Bitmap imageBitMap;
    private String videoFilePath;

    public String getVideoFilePath() {
        return videoFilePath;
    }

    public void setVideoFilePath(String videoFilePath) {
        this.videoFilePath = videoFilePath;
    }

    public Bitmap getImageBitMap() {
        return imageBitMap;
    }

    public void setImageBitMap(Bitmap imageBitMap) {
        this.imageBitMap = imageBitMap;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    private String uploadDate;

    public Video(String author, String content, String duration , String views, String uploadDate, int pic, int video) {
        this.author = author;
        this.content = content;
        this.pic = pic;
        this.duration = duration;
        this.views = views;
        this.uploadDate = uploadDate;
        this.video = video;
    }
//    public Video(String author, String content, String duration , String views, String uploadDate) {
//        this.author = author;
//        this.content = content;
//        this.duration = duration;
//        this.views = views;
//        this.uploadDate = uploadDate;
//        this.pic = R.drawable.osher;
//        this.video = R.raw.video1;
//    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Uri getVideoFileUri() {
        return videoFileUri;
    }

    public void setVideoFileUri(Uri videoFileUri) {
        this.videoFileUri = videoFileUri;
    }

    public Uri getImageFileUri() {
        return imageFileUri;
    }

    public void setImageFileUri(Uri imageFileUri) {
        this.imageFileUri = imageFileUri;
    }
    public Bitmap getThumbnailBitmap() {
        return thumbnailBitmap;
    }

    public void setThumbnailBitmap(Bitmap thumbnailBitmap) {
        this.thumbnailBitmap = thumbnailBitmap;
    }
}
