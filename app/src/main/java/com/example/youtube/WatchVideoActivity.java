package com.example.youtube;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.adapters.CommentsListAdapter;
import com.example.youtube.entities.Comment;
import com.example.youtube.entities.Video;

import java.util.ArrayList;
import java.util.List;

public class WatchVideoActivity extends AppCompatActivity implements CommentsListAdapter.CommentInteractionListener {

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
        setContentView(R.layout.activity_watch_video);

        videoView = findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        initializeViews();
        initializeVideoPlayer();
        initializeCommentsList();

        ImageButton btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> finish());

        ImageButton btnLike = findViewById(R.id.btnLike);
        ImageButton btnUnlike = findViewById(R.id.btnUnlike);
        ImageButton btnAddComment = findViewById(R.id.btnAddComment);
        EditText etComment = findViewById(R.id.etComment);

        ImageButton btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, btnShare);
            popupMenu.getMenuInflater().inflate(R.menu.share_menu, popupMenu.getMenu());
            popupMenu.show();
        });


        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = etComment.getText().toString().trim();
                if (!TextUtils.isEmpty(commentText)) {
                    Comment newComment = new Comment(getVideoId(), "@user", commentText, "now", 0, 0);
                    newComment.setId(filteredComments.size() + 1);
                    filteredComments.add(0, newComment);
                    CommentsManager.getInstance().addComment(newComment);
                    etComment.setText("");
                    adapter.setComments(filteredComments);
                }
            }
        });

        btnLike.setOnClickListener(v -> {
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
        });

        btnUnlike.setOnClickListener(v -> {
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
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvContent = findViewById(R.id.tvContent);
        TextView tvDuration = findViewById(R.id.tvDuration);
        TextView tvViews = findViewById(R.id.tvViews);
        TextView tvUploadDate = findViewById(R.id.tvUploadDate);

        int videoId = getIntent().getIntExtra("videoId", -1);
        Video video = VideoRepository.getVideoById(videoId);
        if (video != null) {
            tvAuthor.setText(video.getAuthor());
            tvContent.setText(video.getContent());
            tvDuration.setText(video.getDuration());
            tvViews.setText(video.getViews());
            tvUploadDate.setText(video.getUploadDate());
        }
    }

    private void initializeVideoPlayer() {
        int videoId = getIntent().getIntExtra("videoId", -1);
        Video video = VideoRepository.getVideoById(videoId);
        if (video != null) {
            String videoFile = video.getVideoFilePath();
            if (videoFile != null) {
                videoView.setVideoPath(videoFile);
                videoView.start();
            } else {
                String path = "android.resource://" + getPackageName() + "/" + video.getVideo();
                videoView.setVideoURI(Uri.parse(path));
                videoView.start();
            }
        }
    }


    private int getVideoId() {
        return getIntent().getIntExtra("videoId", -1);
    }

    private void initializeCommentsList() {
        RecyclerView recyclerView = findViewById(R.id.lstComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentsListAdapter(this, this);
        recyclerView.setAdapter(adapter);

        int videoId = getIntent().getIntExtra("videoId", -1);
        comments = CommentsManager.getInstance().getCommentsForVideo(videoId);
        filteredComments = new ArrayList<>(comments);
        adapter.setComments(filteredComments);
    }

    @Override
    public void onDeleteComment(Comment comment) {
        CommentsManager.getInstance().deleteComment(comment);
        filteredComments.remove(comment);
        adapter.setComments(filteredComments);
    }

    private void updateLikeDislikeUI() {
        TextView tvLikeCount = findViewById(R.id.tvLikeCount);
        TextView tvUnlikeCount = findViewById(R.id.tvUnlikeCount);

        tvLikeCount.setText(String.valueOf(likeCount));
        tvUnlikeCount.setText(String.valueOf(unlikeCount));
    }


}
