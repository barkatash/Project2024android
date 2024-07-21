package com.example.youtube.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.AppDB;
import com.example.youtube.dao.CommentDao;
import com.example.youtube.entities.Comment;
import com.example.youtube.remoteRepositories.CommentRemoteRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {
    private static CommentRepository instance;
    private CommentDao commentDao;
    private CommentRemoteRepository remoteRepository;
    private MutableLiveData<List<Comment>> allComments;

    private CommentRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        commentDao = db.commentDao();
        remoteRepository = new CommentRemoteRepository();
        allComments = commentDao.index();
    }

    public static synchronized CommentRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CommentRepository(context.getApplicationContext());
        }
        return instance;
    }

    public LiveData<List<Comment>> getAllComments() {
        return allComments;
    }

    public void fetchCommentsForVideo(int videoId) {
        remoteRepository.getCommentsForVideo(videoId, new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    List<Comment> comments = response.body();
                    // Update local database with remote comments
                    if (comments != null) {
                        new Thread(() -> {
                            commentDao.insert(comments.toArray(new Comment[0]));
                        }).start();
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
                        new Thread(() -> commentDao.insert(newComment)).start();
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
                    new Thread(() -> commentDao.delete(commentDao.get(id).getValue())).start();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
