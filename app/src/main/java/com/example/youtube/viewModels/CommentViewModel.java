package com.example.youtube.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.youtube.entities.Comment;
import com.example.youtube.repositories.CommentRepository;

import java.util.List;

public class CommentViewModel extends ViewModel {

    private LiveData<List<Comment>> comments;
    private CommentRepository commentRepository;

    public CommentViewModel() {
        this.commentRepository = CommentRepository.getInstance(null);
        this.comments = this.commentRepository.getAllComments();
    }

    public LiveData<List<Comment>> loadComments(String videoId) {
        return commentRepository.fetchCommentsForVideo(videoId);
    }

    public LiveData<List<Comment>> getAllComments() {
        return comments;
    }

    public void addComment(String token, Comment comment) {
        commentRepository.addComment(token, comment);
    }

    public void deleteComment(String token, String username, String commentId) {
        commentRepository.deleteComment(token, username, commentId);
    }
}
