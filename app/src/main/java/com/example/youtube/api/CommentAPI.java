package com.example.youtube.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube.MyApplication;
import com.example.youtube.R;
import com.example.youtube.dao.CommentDao;
import com.example.youtube.entities.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAPI {
    private MutableLiveData<List<Comment>> commentListData;
    private CommentDao dao;
    Retrofit retrofit;
    commentWebServiceAPI webServiceAPI;

    public CommentAPI(MutableLiveData<List<Comment>> commentListData, CommentDao dao) {
        this.commentListData = commentListData;
        this.dao = dao;
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create()).build();
        webServiceAPI = retrofit.create(commentWebServiceAPI.class);
   }

   public void getAllComments(MutableLiveData<List<Comment>> comments) {
       Call<List<Comment>> call = webServiceAPI.getComments();
       call.enqueue(new Callback<List<Comment>>() {
           @Override
           public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
               comments.postValue(response.body());
           }
           @Override
           public void onFailure (Call<List<Comment>> call, Throwable t) {
               Log.e("error", t.getMessage());
           }
       });
   }

    public MutableLiveData<List<Comment>> getCommentsForVideo(String videoId) {
        MutableLiveData<List<Comment>> videosComment = new MutableLiveData<>();
        Call<List<Comment>> call = webServiceAPI.getCommentsForVideo(videoId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videosComment.setValue(response.body());
                } else {
                    Log.e("CommentAPI", "Failed to fetch video's comments: " + response.message());
                    videosComment.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e("CommentAPI", "Error fetching video's comments: " + t.getMessage());
                videosComment.setValue(null);
            }
        });
        return videosComment;
    }

    public void addComment(String token, Comment comment) {
        Call<Void> call = webServiceAPI.addComment("Bearer " + token, comment.getUsername(), comment.getVideoId(), comment);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("CommentAPI", "comment added successfully.");
                    getAllComments(commentListData);
                } else {
                    Log.e("CommentAPI", "Failed to add comment: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CommentAPI", "Error adding comment: " + t.getMessage());
            }
        });
    }
    public void editComment(String token, String username, String commentId, Comment comment) {
        Call<Void> call = webServiceAPI.editComment("Bearer " + token, username, commentId, comment);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("CommentAPI", "comment added successfully.");
                    getAllComments(commentListData);
                } else {
                    Log.e("CommentAPI", "Failed to edit comment: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CommentAPI", "Error adding comment: " + t.getMessage());
            }
        });
    }

    public void deleteComment(String token, String username, String commentId) {
        Call<Void> call = webServiceAPI.deleteComment("Bearer " + token, username,commentId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("CommentAPI", "comment deleted successfully.");
                    // Optionally, refresh the video list
                    getAllComments(commentListData);
                } else {
                    Log.e("CommentAPI", "Failed to delete comment: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CommentAPI", "Error deleting comment: " + t.getMessage());
            }
        });
    }
}
