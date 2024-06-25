package com.example.youtube;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.adapters.CommentsListAdapter;
import com.example.youtube.databinding.ActivityWatchVideoBinding;
import com.example.youtube.entities.Comment;
import com.example.youtube.entities.Video;

import java.util.ArrayList;
import java.util.List;

public class WatchVideoActivity extends AppCompatActivity {

    private ActivityWatchVideoBinding binding;
    private VideoView videoView;
    private CommentsListAdapter adapter;
    private List<Comment> comments;
    private List<Comment> filteredComments;
    private int likeCount = 0;
    private int unlikeCount = 0;
    private boolean isLiked = false;
    private boolean isUnliked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();
        initializeVideoPlayer();
        initializeCommentsList();

        ImageButton btnAddComment = findViewById(R.id.btnAddComment);
        EditText etComment = findViewById(R.id.etComment);
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = etComment.getText().toString().trim();
                if (!TextUtils.isEmpty(commentText)) {
                    Comment newComment = new Comment(getVideoId(), "@user", commentText, "now", 0, 0);
                    newComment.setId(filteredComments.size() + 1);
                    filteredComments.add(0, newComment);  // Add the new comment at the beginning
                    CommentsManager.getInstance().addComment(newComment);
                    etComment.setText("");
                    updateCommentsAmount();
                    adapter.setComments(filteredComments);
                }
            }
        });

        ImageButton btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btnLike = findViewById(R.id.btnLike);
        ImageButton btnUnlike = findViewById(R.id.btnUnlike);

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLiked) {
                    likeCount++;
                    isLiked = true;

                    if (isUnliked) {
                        unlikeCount--;
                        isUnliked = false;
                    }
                } else {
                    likeCount--;
                    isLiked = false;
                }

                updateLikeDislikeUI();
            }
        });

        btnUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUnliked) {
                    unlikeCount++;
                    isUnliked = true;

                    if (isLiked) {
                        likeCount--;
                        isLiked = false;
                    }
                } else {
                    unlikeCount--;
                    isUnliked = false;
                }

                updateLikeDislikeUI();
            }
        });

        if (savedInstanceState != null) {
            likeCount = savedInstanceState.getInt("likeCount", 0);
            unlikeCount = savedInstanceState.getInt("unlikeCount", 0);
            isLiked = savedInstanceState.getBoolean("isLiked", false);
            isUnliked = savedInstanceState.getBoolean("isUnliked", false);
        }

        updateLikeDislikeUI();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("likeCount", likeCount);
        outState.putInt("unlikeCount", unlikeCount);
        outState.putBoolean("isLiked", isLiked);
        outState.putBoolean("isUnliked", isUnliked);
    }

    private void initializeViews() {
        // Initialize views and set click listeners
        binding.btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle adding comment
            }
        });
    }

    private void initializeVideoPlayer() {
        // Initialize video player and set video details
        videoView = binding.videoView;
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        int videoId = getIntent().getIntExtra("videoId", -1);
        Video video = VideoRepository.getVideoById(videoId);
        if (video != null) {
            String path = "android.resource://" + getPackageName() + "/" + video.getVideo();
            videoView.setVideoURI(Uri.parse(path));
            videoView.start();
            updateVideoDetails(video);
        }
    }

    private void initializeCommentsList() {
        // Initialize comments list and adapter
        RecyclerView lstComments = binding.lstComments;
        adapter = new CommentsListAdapter(this);
        lstComments.setAdapter(adapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));

        comments = CommentsManager.getInstance().getComments();
        int videoId = getIntent().getIntExtra("videoId", -1);
        filteredComments = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getVideoId() == videoId) {
                filteredComments.add(comment);
            }
        }
        adapter.setComments(filteredComments);
        updateCommentsAmount();
    }

    private int getVideoId() {
        // Get video ID from intent extras
        return getIntent().getIntExtra("videoId", -1);
    }

    private void updateVideoDetails(Video video) {
        // Update video details UI
        binding.tvAuthor.setText(video.getAuthor());
        binding.tvContent.setText(video.getContent());
        binding.tvDuration.setText(video.getDuration());
        binding.tvViews.setText(video.getViews());
        binding.tvUploadDate.setText(video.getUploadDate());
    }

    private void updateCommentsAmount() {
        // Update comments count UI
        binding.amount.setText(String.valueOf(filteredComments.size()));
    }

    private void updateLikeDislikeUI() {
        // Update like and dislike UI elements
        binding.tvLikeCount.setText(String.valueOf(likeCount));
        binding.tvUnlikeCount.setText(String.valueOf(unlikeCount));
    }
}
