package com.example.youtube.remoteRepositories;

import com.example.youtube.apiService.VideoApiService;
import com.example.youtube.entities.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoRemoteRepository {
    private VideoApiService apiService;

    public VideoRemoteRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your-backend-url.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(VideoApiService.class);
    }

    public void getAllVideos(Callback<List<Video>> callback) {
        Call<List<Video>> call = apiService.getAllVideos();
        call.enqueue(callback);
    }

    public void getVideoById(int id, Callback<Video> callback) {
        Call<Video> call = apiService.getVideoById(id);
        call.enqueue(callback);
    }

    public void addVideo(Video video, Callback<Video> callback) {
        Call<Video> call = apiService.addVideo(video);
        call.enqueue(callback);
    }

    public void deleteVideo(int id, Callback<Void> callback) {
        Call<Void> call = apiService.deleteVideo(id);
        call.enqueue(callback);
    }
}
