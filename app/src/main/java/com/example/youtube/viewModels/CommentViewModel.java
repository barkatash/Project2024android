package com.example.youtube.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.Comment;
import com.example.youtube.repositories.CommentRepository;

import java.util.List;

public class CommentViewModel extends AndroidViewModel {
    private CommentRepository commentRepository;
    private LiveData<List<Comment>> commentsLiveData;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        commentRepository = CommentRepository.getInstance(application);
        commentsLiveData = commentRepository.getAllComments();
    }

    public LiveData<List<Comment>> getAllComments() {
        return commentsLiveData;
    }

    public void addComment(Comment comment) {
        commentRepository.addComment(comment);
    }

    public void deleteComment(Comment comment) {
        commentRepository.deleteComment(comment.getId());
    }
}
