package com.example.youtube.api;

import com.example.youtube.entities.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface commentWebServiceAPI {
    @GET("comments")
    Call<List<Comment>> getComments();
    @GET("comments/video/{videoId}")
    Call<List<Comment>> getCommentsForVideo(@Path("videoId") String videoId);

    @POST("comments")
    Call<Void> addComment(@Body Comment comment);

    @DELETE("comments/{id}")
    Call<Void> deleteComment(@Path("id") String id);
}
