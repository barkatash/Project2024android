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
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.adapters.CommentsListAdapter;
import com.example.youtube.entities.Comment;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.CommentRepository;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class WatchVideoActivity extends AppCompatActivity implements CommentsListAdapter.CommentInteractionListener {

    private VideoView videoView;
    private CommentsListAdapter adapter;
    private LiveData<List<Comment>> filteredComments;
    private int likeCount = 0;
    private boolean isLiked = false;
    private boolean isUnliked = false;

    private UserRepository userRepository;
    private VideoRepository videoRepository;
    private CommentRepository commentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);

        // Initialize repositories with application context
        userRepository = UserRepository.getInstance(getApplicationContext());
        videoRepository = VideoRepository.getInstance(getApplicationContext());
        commentRepository = CommentRepository.getInstance(getApplicationContext());

        videoView = findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        int videoId = getVideoId();
        Video video = videoRepository.getVideoById(videoId);

        if (video != null && userRepository.getLoggedInUser() != null) {
            isLiked = userRepository.getLoggedInUser().getLikedVideoIds().contains(video.getVideoId());
            isUnliked = userRepository.getLoggedInUser().getUnLikedVideoIds().contains(video.getVideoId());
        }

        initializeViews();
        initializeVideoPlayer();
        initializeCommentsList();

        ImageButton btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> finish());

        ImageButton btnLike = findViewById(R.id.btnLike);
        ImageButton btnUnlike = findViewById(R.id.btnUnlike);
        ImageButton btnAddComment = findViewById(R.id.btnAddComment);
        EditText etComment = findViewById(R.id.etComment);

        if (!isLiked) {
            btnLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
        }
        if (!isUnliked) {
            btnUnlike.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
        }

        ImageButton btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, btnShare);
            popupMenu.getMenuInflater().inflate(R.menu.share_menu, popupMenu.getMenu());
            popupMenu.show();
        });

        btnAddComment.setOnClickListener(v -> {
            if (userRepository.getLoggedInUser() == null) {
                Toast.makeText(WatchVideoActivity.this, "You need to be logged in to leave a comment.", Toast.LENGTH_SHORT).show();
                return;
            }
            String commentText = etComment.getText().toString().trim();
            if (!TextUtils.isEmpty(commentText)) {
                Comment newComment = new Comment(video.getVideoId(), userRepository.getLoggedInUser().getId(), commentText, new Date().toString(), 0, 0);
                commentRepository.addComment(newComment);
                etComment.setText("");
            }
        });

        btnLike.setOnClickListener(v -> {
            if (userRepository.getLoggedInUser() == null) {
                Toast.makeText(WatchVideoActivity.this, "You need to be logged in to like a video", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isLiked) {
                likeCount++;
                isLiked = true;
                btnLike.setImageResource(R.drawable.baseline_thumb_up_24);
                List<Integer> newLikedVideos = userRepository.getLoggedInUser().getLikedVideoIds();
                newLikedVideos.add(video.getVideoId());
                userRepository.getLoggedInUser().setLikedVideoIds(newLikedVideos);

                if (isUnliked) {
                    btnUnlike.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
                    isUnliked = false;
                    List<Integer> newUnLikedVideos = userRepository.getLoggedInUser().getUnLikedVideoIds();
                    newUnLikedVideos.remove(video.getVideoId());
                    userRepository.getLoggedInUser().setUnLikedVideoIds(newUnLikedVideos);
                }
            } else {
                likeCount--;
                isLiked = false;
                btnLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                List<Integer> newLikedVideos = userRepository.getLoggedInUser().getLikedVideoIds();
                newLikedVideos.remove(video.getVideoId());
                userRepository.getLoggedInUser().setLikedVideoIds(newLikedVideos);
            }
            updateLikeDislikeUI();
        });

        btnUnlike.setOnClickListener(v -> {
            if (userRepository.getLoggedInUser() == null) {
                Toast.makeText(WatchVideoActivity.this, "You need to be logged in to unlike a video", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isUnliked) {
                btnUnlike.setImageResource(R.drawable.baseline_thumb_down_24);
                isUnliked = true;
                List<Integer> newUnlikedVideos = userRepository.getLoggedInUser().getUnLikedVideoIds();
                newUnlikedVideos.add(video.getVideoId());
                userRepository.getLoggedInUser().setUnLikedVideoIds(newUnlikedVideos);

                if (isLiked) {
                    likeCount--;
                    isLiked = false;
                    btnLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                    List<Integer> newLikedVideos = userRepository.getLoggedInUser().getLikedVideoIds();
                    newLikedVideos.remove(video.getVideoId());
                    userRepository.getLoggedInUser().setLikedVideoIds(newLikedVideos);
                }
            } else {
                btnUnlike.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
                isUnliked = false;
                List<Integer> newUnLikedVideos = userRepository.getLoggedInUser().getUnLikedVideoIds();
                newUnLikedVideos.remove(video.getVideoId());
                userRepository.getLoggedInUser().setUnLikedVideoIds(newUnLikedVideos);
            }
            updateLikeDislikeUI();
        });

        if (savedInstanceState != null) {
            likeCount = savedInstanceState.getInt("likeCount", 0);
            isLiked = savedInstanceState.getBoolean("isLiked", false);
            isUnliked = savedInstanceState.getBoolean("isUnliked", false);
        }

        updateLikeDislikeUI();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("likeCount", likeCount);
        outState.putBoolean("isLiked", isLiked);
        outState.putBoolean("isUnliked", isUnliked);
    }

    private void initializeViews() {
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvContent = findViewById(R.id.tvContent);
        TextView tvDuration = findViewById(R.id.tvDuration);
        TextView tvViews = findViewById(R.id.tvViews);
        TextView tvUploadDate = findViewById(R.id.tvUploadDate);
        TextView tvLikeCount = findViewById(R.id.tvLikeCount);

        int videoId = getVideoId();
        Video video = videoRepository.getVideoById(videoId);
        if (video != null) {
            tvAuthor.setText(video.getAuthor());
            tvContent.setText(video.getDescription());
            tvDuration.setText(video.getDuration());
            tvViews.setText(video.getViews());
            tvUploadDate.setText(video.getUploadDate().toString()); // Convert Date to String
            likeCount = video.getLikes();
            tvLikeCount.setText(String.valueOf(likeCount));
        }
    }

    private void initializeVideoPlayer() {
        int videoId = getVideoId();
        Video video = videoRepository.getVideoById(videoId);
        if (video != null) {
            String videoFile = video.getVideoFilePath();
            if (videoFile != null) {
                videoView.setVideoPath(videoFile);
                videoView.start();
            } else {
                String path = "android.resource://" + getPackageName() + "/" + video.getVideoFilePath();
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

        int videoId = getVideoId();
        commentRepository.fetchCommentsForVideo(videoId);
        filteredComments = commentRepository.getAllComments();
        filteredComments.observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                // Filter comments for the current video
                List<Comment> videoComments = new ArrayList<>();
                for (Comment comment : comments) {
                    if (comment.getVideoId() == videoId) {
                        videoComments.add(comment);
                    }
                }
                adapter.setComments(videoComments);
            }
        });
    }

    @Override
    public void onDeleteComment(Comment comment) {
        commentRepository.deleteComment(comment.getId());
    }

    private void updateLikeDislikeUI() {
        int videoId = getVideoId();
        Video video = videoRepository.getVideoById(videoId);
        if (video != null) {
            video.setLikes(likeCount);
            TextView tvLikeCount = findViewById(R.id.tvLikeCount);
            tvLikeCount.setText(String.valueOf(likeCount));
        }
    }
}
