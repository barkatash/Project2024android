package com.example.youtube;

import com.example.youtube.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsManager {
    private static CommentsManager instance;
    private List<Comment> comments;

    private CommentsManager() {
        comments = new ArrayList<>();
        comments.add(new Comment(1, "omer", "good song", "11 months ago", 30, 2));
        comments.add(new Comment(4, "bar", "love this!", "3 months ago", 10, 0));
        comments.add(new Comment(6, "yael", "amazing", "7 days ago", 4, 6));
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
}
