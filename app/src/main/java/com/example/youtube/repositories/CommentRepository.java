package com.example.youtube.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.AppDB;
import com.example.youtube.dao.CommentDao;
import com.example.youtube.dao.UserDao;
import com.example.youtube.dao.VideoDao;
import com.example.youtube.entities.Comment;
import com.example.youtube.api.CommentAPI;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {
    private static CommentRepository instance;
    private CommentDao commentDao;
    private CommentListData commentListData;
    private CommentAPI apiService;

    private CommentRepository(Context context) {
        commentListData = new CommentListData();
        apiService = new CommentAPI(commentListData, commentDao);
    }

    public static synchronized CommentRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CommentRepository(context);
        }
        return instance;
    }
    class CommentListData extends MutableLiveData<List<Comment>> {
        public CommentListData() {
            super();
            setValue(new LinkedList<Comment>());
        }

        @Override
        protected void onActive() {
            super.onActive();
        }
    }
    public LiveData<List<Comment>> getAllComments() {
        return commentListData;
    }
/*
    public void fetchCommentsForVideo(int videoId) {
        remoteRepository.getCommentsForVideo(videoId, new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    List<Comment> comments = response.body();
                    if (comments != null) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            commentDao.insert(comments.toArray(new Comment[0]));
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void addComment(Comment comment) {
        remoteRepository.addComment(comment, new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Comment newComment = response.body();
                    if (newComment != null) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            commentDao.insert(newComment);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void deleteComment(int id) {
        remoteRepository.deleteComment(id, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        Comment comment = commentDao.get(id).getValue();
                        if (comment != null) {
                            commentDao.delete(comment);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }

 */
}
