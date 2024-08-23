package com.example.youtube.api;

import com.example.youtube.entities.Video;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface videoWebServiceAPI {

    @GET("videos")
    Call<List<Video>> getVideos();
    @GET("users/{id}/recommendations")
    Call<List<Video>> getRecommendedVideos(@Path("id") String id);
    @Multipart
    @POST("users/{id}/videos")
    Call<Void> addVideo(
            @Header("Authorization") String authHeader,
            @Path("id") String userId,
            @Part("title") RequestBody title,
            @Part("uploader") RequestBody uploader,
            @Part("duration") RequestBody duration,
            @Part("visits") RequestBody visits,
            @Part MultipartBody.Part videoFile,
            @Part MultipartBody.Part image
    );

    @Multipart
    @PATCH("users/{id}/videos/{pid}")
    Call<Void> editVideo(
            @Header("Authorization") String authHeader,
            @Path("id") String userId,
            @Path("pid") String videoId,
            @Part("title") RequestBody title,
            @Part("uploader") RequestBody uploader,
            @Part MultipartBody.Part videoFile,
            @Part MultipartBody.Part image
    );
    @DELETE("users/{id}/videos/{pid}")
    Call<Void> deleteVideo(@Header("Authorization") String authHeader, @Path("id") String id, @Path("pid") String pid);

    @GET("videos/{id}")
    Call<Video> getVideoById(@Path("id") String id);
    @GET("users/{username}/videos")
    Call<List<Video>> getVideosByUser(@Path("username") String username);
}
