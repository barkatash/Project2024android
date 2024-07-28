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
    private static volatile CommentRepository instance;
    private CommentDao commentDao;
    private CommentListData commentListData;
    private CommentAPI apiService;

    private CommentRepository() {
        commentListData = new CommentListData();
        apiService = new CommentAPI(commentListData, commentDao);
        apiService.getAllComments(commentListData);
    }

    public static synchronized CommentRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (CommentRepository.class) {
                if (instance == null) {
                    instance = new CommentRepository();
                }
            }
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

    public LiveData<List<Comment>> fetchCommentsForVideo(String videoId) {
        return apiService.getCommentsForVideo(videoId);
    }

    public void addComment(Comment comment) {
        apiService.addComment(comment);
    }

    public void deleteComment(String id) {
        apiService.deleteComment(id);
    }
}
