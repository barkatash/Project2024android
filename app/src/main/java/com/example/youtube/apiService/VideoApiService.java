package com.example.youtube.apiService;

import com.example.youtube.entities.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VideoApiService {
    @GET("videos")
    Call<List<Video>> getAllVideos();

    @GET("videos/{id}")
    Call<Video> getVideoById(@Path("id") int id);

    @POST("videos")
    Call<Video> addVideo(@Body Video video);

    @DELETE("videos/{id}")
    Call<Void> deleteVideo(@Path("id") int id);
}
