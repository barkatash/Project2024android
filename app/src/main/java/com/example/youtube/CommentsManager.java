package com.example.youtube;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.youtube.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsManager {
    private static CommentsManager instance;
    private List<Comment> comments;

    private CommentsManager() {
        comments = new ArrayList<>();
        comments.add(new Comment(1, UsersManager.getInstance().getUserByName("sagi"), "good song", "11 months ago", 30, 2));
        comments.add(new Comment(4, UsersManager.getInstance().getUserByName("chen"), "love this!", "3 months ago", 10, 0));
        comments.add(new Comment(6, UsersManager.getInstance().getUserByName("amit"), "amazing", "7 days ago", 4, 6));
    }

    public static synchronized CommentsManager getInstance() {
        if (instance == null) {
            instance = new CommentsManager();
        }
        return instance;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(0, comment);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    public List<Comment> getCommentsForVideo(int videoId) {
        List<Comment> commentsForVideo = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getVideoId() == videoId) {
                commentsForVideo.add(comment);
            }
        }
        return commentsForVideo;
    }
}
