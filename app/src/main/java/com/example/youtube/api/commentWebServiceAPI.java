package com.example.youtube.api;

import com.example.youtube.entities.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface commentWebServiceAPI {
    @GET("comments")
    Call<List<Comment>> getComments();
    @GET("comments/video/{videoId}")
    Call<List<Comment>> getCommentsForVideo(@Path("videoId") String videoId);

    @POST("comments/user/{id}/{pid}")
    Call<Void> addComment(@Header("Authorization") String authHeader,
                          @Path("id") String userId,
                          @Path("pid") String videoId,
                          @Body Comment comment);
    @PATCH("comments/user/{id}/{pid}")
    Call<Void> editComment(@Header("Authorization") String authHeader,
                          @Path("id") String userId,
                          @Path("pid") String commentId, @Body Comment comment
    );
    @DELETE("comments/user/{id}/{pid}")
    Call<Void> deleteComment(@Header("Authorization") String authHeader,
                             @Path("id") String userId,
                             @Path("pid") String commentId);
}
