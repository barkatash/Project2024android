package com.example.youtube.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.youtube.repositories.VideoRepository;
import com.example.youtube.entities.Video;

import java.util.List;

public class VideoViewModel extends ViewModel {

    private LiveData<List<Video>> videos;
    private LiveData<Video> video;
    private VideoRepository videoRepository;

    public VideoViewModel() {
        videoRepository = new VideoRepository();
        videos = videoRepository.getAllVideos();
    }

    public LiveData<List<Video>> getVideos() {
        return videos;
    }

    public void setVideos(LiveData<List<Video>> videos) {
        this.videos = videos;
    }

    public LiveData<Video> getVideo() {
        return video;
    }

    public void setVideo(LiveData<Video> video) {
        this.video = video;
    }

    public VideoRepository getVideoRepository() {
        return videoRepository;
    }

    public void setVideoRepository(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

}