package com.example.youtube.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.Video;
import com.example.youtube.remoteRepositories.VideoRemoteRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoViewModel extends AndroidViewModel {
    private final VideoRemoteRepository videoRepository;
    private final MutableLiveData<List<Video>> videosLiveData;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        videoRepository = new VideoRemoteRepository(application.getApplicationContext());
        videosLiveData = new MutableLiveData<>();
        loadVideos();
    }

    private void loadVideos() {
        videoRepository.getAllVideos(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    Log.d("VideoViewModel", "API call successful, response: " + response.body());
                    videosLiveData.setValue(response.body());
                } else {
                    Log.e("VideoViewModel", "API call unsuccessful, response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e("VideoViewModel", "API call failed", t);
            }
        });
    }

    public LiveData<List<Video>> getAllVideos() {
        return videosLiveData;
    }

    public void addVideo(Video video) {
        videoRepository.addVideo(video, new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful()) {
                    Log.d("VideoViewModel", "Video added successfully");
                    loadVideos(); // Reload videos after adding a new one
                } else {
                    Log.e("VideoViewModel", "Failed to add video, response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.e("VideoViewModel", "Failed to add video", t);
            }
        });
    }

    public void deleteVideo(int videoId) {
        videoRepository.deleteVideo(videoId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("VideoViewModel", "Video deleted successfully");
                    loadVideos(); // Reload videos after deleting one
                } else {
                    Log.e("VideoViewModel", "Failed to delete video, response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("VideoViewModel", "Failed to delete video", t);
            }
        });
    }
}
