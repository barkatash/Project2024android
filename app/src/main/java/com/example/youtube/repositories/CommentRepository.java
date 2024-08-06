package com.example.youtube.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.youtube.AppDB;
import com.example.youtube.api.CommentAPI;
import com.example.youtube.dao.CommentDao;
import com.example.youtube.entities.Comment;

import java.util.LinkedList;
import java.util.List;

public class CommentRepository {
    private static CommentRepository instance;
    private CommentListData commentListData;
    private CommentAPI apiService;
    private AppDB appDB;
    private CommentDao dao;
    private Application application;
    public CommentRepository(Application application) {
        this.application = application;
        appDB = Room.databaseBuilder(application, AppDB.class, "database")
                .allowMainThreadQueries()
                .build();
        dao = appDB.commentDao();
        commentListData = new CommentListData();
        apiService = new CommentAPI(commentListData, dao);
        resetComments();
    }

    public static synchronized CommentRepository getInstance(Application application) {
        if (instance == null) {
            synchronized (CommentRepository.class) {
                if (instance == null) {
                    instance = new CommentRepository(application);
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
    private void loadCommentsFromLocal() {
        new Thread(() -> {
            List<Comment> comments = dao.index();
            commentListData.postValue(comments);
        }).start();
    }
    public void resetComments() {
        apiService.getAllComments(commentListData);
        loadCommentsFromLocal();
    }
    public LiveData<List<Comment>> getAllComments() {
        return commentListData;
    }

    public LiveData<List<Comment>> fetchCommentsForVideo(String videoId) {
        resetComments();
        return apiService.getCommentsForVideo(videoId);
    }

    public void addComment(String token, Comment comment) {
        apiService.addComment(token, comment);
        resetComments();
    }
    public void editComment(String token, String username, String commentId, Comment comment) {
        apiService.editComment(token, username, commentId, comment);
        resetComments();
    }
    public void deleteComment(String token, String username, String commentId) {
        apiService.deleteComment(token, username, commentId);
        resetComments();
    }
}
