package com.example.youtube.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.Video;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {
    private VideoRepository videoRepository;
    private MutableLiveData<List<Video>> videosLiveData;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        videoRepository = VideoRepository.getInstance(application);
        videosLiveData = new MutableLiveData<>();
        videosLiveData = videoRepository.getAllVideos();
    }

    public LiveData<List<Video>> getAllVideos() {
        return videosLiveData;
    }

    public void addVideo(Video video) {
        videoRepository.insert(video);
        videosLiveData = videoRepository.getAllVideos();
    }

    public void deleteVideo(Video video) {
        videoRepository.delete(video);
        videosLiveData = videoRepository.getAllVideos();
    }

    public void updateVideo(Video video) {
        videoRepository.update(video);
        videosLiveData = videoRepository.getAllVideos();
    }
}
