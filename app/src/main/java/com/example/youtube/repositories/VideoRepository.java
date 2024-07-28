package com.example.youtube.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.api.VideoAPI;
import com.example.youtube.dao.VideoDao;
import com.example.youtube.entities.Video;

import java.util.LinkedList;
import java.util.List;

public class VideoRepository {
    private VideoDao dao;
    private VideoListData videoListData;
    private VideoAPI api;
    public VideoRepository() {
        videoListData = new VideoListData();
        api = new VideoAPI(videoListData, dao);
        api.getAllVideos(videoListData);
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
//         new Thread(() ->
//        {
//            VideoListData.postValue(dao.get());
//             }).start();
        }

    }
    public LiveData<List<Video>> getAllVideos() {
        return videoListData;
    }

    public LiveData<Video> getVideoById(String videoId) {
        return api.getVideoById(videoId);
    }
    public void resetVideos() {
        api.getAllVideos(videoListData);
    }
    public void deleteVideo(String videoId) {
        api.deleteVideo(videoId);
        resetVideos();
    }
    public void addVideo(Video newVideo) {
        api.addVideo(newVideo);
        resetVideos();
    }


}