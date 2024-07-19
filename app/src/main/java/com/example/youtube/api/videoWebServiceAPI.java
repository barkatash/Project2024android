package com.example.youtube.api;

import com.example.youtube.entities.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface videoWebServiceAPI {

    @GET("videos")
    Call<List<Video>> getVideos();
    @POST("users/{id}/videos")
    Call<Void> addVideo (@Body Video video);
    @DELETE("users/{id}/videos/{pid}")
    Call<Void> deleteVideo (@Path("id") String id);
}
