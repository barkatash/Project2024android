package com.example.youtube.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.youtube.entities.Comment;
import com.example.youtube.api.CommentAPI;
import com.example.youtube.repositories.CommentRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentViewModel extends ViewModel {

    private LiveData<List<Comment>> comments;
    private CommentRepository commentRepository;

    public CommentViewModel() {
        this.commentRepository = CommentRepository.getInstance();
        this.comments = this.commentRepository.getAllComments();
    }

    public LiveData<List<Comment>> loadComments(String videoId) {
        return commentRepository.fetchCommentsForVideo(videoId);
    }

    public LiveData<List<Comment>> getAllComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        commentRepository.addComment(comment);
    }

    public void deleteComment(String commentId) {
        commentRepository.deleteComment(commentId);
    }
}
