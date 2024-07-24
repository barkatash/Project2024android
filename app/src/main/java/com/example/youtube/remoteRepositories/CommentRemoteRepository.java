package com.example.youtube.remoteRepositories;

import android.content.Context;

import com.example.youtube.R;
import com.example.youtube.api.CommentApiService;
import com.example.youtube.entities.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentRemoteRepository {
    private final CommentApiService apiService;

    // Constructor that accepts Context
    public CommentRemoteRepository(Context context) {
        // Initialize Retrofit with base URL from resources
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.BaseUrl)) // Retrieve base URL from strings.xml
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(CommentApiService.class);
    }

    // Method to get comments for a specific video
    public void getCommentsForVideo(int videoId, Callback<List<Comment>> callback) {
        Call<List<Comment>> call = apiService.getCommentsForVideo(videoId);
        call.enqueue(callback);
    }

    // Method to add a new comment
    public void addComment(Comment comment, Callback<Comment> callback) {
        Call<Comment> call = apiService.addComment(comment);
        call.enqueue(callback);
    }

    // Method to delete a comment by ID
    public void deleteComment(int id, Callback<Void> callback) {
        Call<Void> call = apiService.deleteComment(id);
        call.enqueue(callback);
    }
}
