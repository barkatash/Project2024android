package com.example.youtube.api;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube.MyApplication;
import com.example.youtube.R;
import com.example.youtube.dao.CommentDao;
import com.example.youtube.entities.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAPI {
    private MutableLiveData<List<Comment>> commentListData;
    private CommentDao dao;
    Retrofit retrofit;
    commentWebServiceAPI webServiceAPI;

    // Constructor that accepts Context
    public CommentAPI(MutableLiveData<List<Comment>> commentListData, CommentDao dao) {
        this.commentListData = commentListData;
        this.dao = dao;
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create()).build();
        webServiceAPI = retrofit.create(commentWebServiceAPI.class);
   }

    // Method to get comments for a specific video
    public void getCommentsForVideo(int videoId, Callback<List<Comment>> callback) {
        Call<List<Comment>> call = webServiceAPI.getCommentsForVideo(videoId);
        call.enqueue(callback);
    }

    // Method to add a new comment
    public void addComment(Comment comment, Callback<Comment> callback) {
        Call<Comment> call = webServiceAPI.addComment(comment);
        call.enqueue(callback);
    }

    // Method to delete a comment by ID
    public void deleteComment(int id, Callback<Void> callback) {
        Call<Void> call = webServiceAPI.deleteComment(id);
        call.enqueue(callback);
    }
}
