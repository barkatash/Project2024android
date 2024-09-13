package com.example.youtube.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.youtube.entities.Video;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {

    private LiveData<List<Video>> videos;
    private LiveData<List<Video>> recommendedVideos;
    private LiveData<Video> video;
    private VideoRepository videoRepository;

    public LiveData<List<Video>> getRecommendedVideos() {
        return recommendedVideos;
    }

    public void setRecommendedVideos(LiveData<List<Video>> recommendedVideos) {
        this.recommendedVideos = recommendedVideos;
    }

    public VideoViewModel(Application application) {
        super(application);
        videoRepository = new VideoRepository(application);
        videos = videoRepository.getAllVideos();
        recommendedVideos = videoRepository.getRecommendedVideos();
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