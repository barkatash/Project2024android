package com.example.youtube.remoteRepositories;

import com.example.youtube.api.CommentApiService;
import com.example.youtube.entities.Comment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class CommentRemoteRepository {
    private CommentApiService apiService;

    public CommentRemoteRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your-backend-url.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(CommentApiService.class);
    }

    public void getCommentsForVideo(int videoId, Callback<List<Comment>> callback) {
        Call<List<Comment>> call = apiService.getCommentsForVideo(videoId);
        call.enqueue(callback);
    }

    public void addComment(Comment comment, Callback<Comment> callback) {
        Call<Comment> call = apiService.addComment(comment);
        call.enqueue(callback);
    }

    public void deleteComment(int id, Callback<Void> callback) {
        Call<Void> call = apiService.deleteComment(id);
        call.enqueue(callback);
    }
}
