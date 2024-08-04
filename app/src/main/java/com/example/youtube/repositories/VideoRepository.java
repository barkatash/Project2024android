package com.example.youtube.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.youtube.AppDB;
import com.example.youtube.api.VideoAPI;
import com.example.youtube.dao.VideoDao;
import com.example.youtube.entities.Video;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class VideoRepository {
    private VideoDao dao;
    private VideoListData videoListData;
    private VideoAPI api;
    private AppDB appDB;
    public VideoRepository(Application application) {
        appDB = Room.databaseBuilder(application, AppDB.class, "video_database")
                .fallbackToDestructiveMigration()
                .build();
        dao = appDB.videoDao();

        videoListData = new VideoListData();
        api = new VideoAPI(videoListData, dao);
        api.getAllVideos(videoListData);
        resetVideos();
    }
    class VideoListData extends MutableLiveData<List<Video>>
    {
        public VideoListData() {
            super();
            setValue(new LinkedList<Video>());
        }
        @Override
        protected void onActive() {
            super.onActive();
        }
    }
    private void loadVideosFromLocal() {
        new Thread(() -> {
            List<Video> videos = dao.index();
            videoListData.postValue(videos);
        }).start();
    }

    public LiveData<List<Video>> getAllVideos() {
        return videoListData;
    }

    public LiveData<Video> getVideoById(String videoId) {
        return api.getVideoById(videoId);
    }
    public void resetVideos() {
        api.getAllVideos(videoListData);
        new Thread(() -> {
            List<Video> videos = videoListData.getValue();
            if (videos != null) {
                dao.insert(videos);
            }
        }).start();
        loadVideosFromLocal();
    }
    public void deleteVideo(String token, String username, String videoId) {
        api.deleteVideo(token, username, videoId);
        resetVideos();
    }
    public void addVideo(String token, Video newVideo, File videoImageFile, File videoFile) {
        api.addVideo(token, newVideo, videoImageFile, videoFile);
        resetVideos();
    }
    public void editVideo(String token, Video newVideo, File videoImageFile, File videoFile) {
        api.editVideo(token, newVideo, videoImageFile, videoFile);
        resetVideos();
    }
    public LiveData<List<Video>> getVideosByUser(String username) {
        VideoListData userVideosData = new VideoListData();
        api.getVideosByUser(username, userVideosData);
        return userVideosData;
    }


}