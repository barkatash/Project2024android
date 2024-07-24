package com.example.youtube;

import com.example.youtube.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsManager {
    private static CommentsManager instance;
    private static List<Comment> comments;

    private CommentsManager() {
        comments = new ArrayList<>();
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
    public static int getNextCommentId() {
        return comments.size() + 1;
    }

    public void addComment(Comment comment) {
        comments.add(0, comment);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    public List<Comment> getCommentsForVideo(String videoId) {
        List<Comment> commentsForVideo = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getVideoId() == videoId) {
                commentsForVideo.add(comment);
            }
        }
        return commentsForVideo;
    }
}
