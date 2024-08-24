package com.example.youtube;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.youtube.adapters.CommentsListAdapter;
import com.example.youtube.adapters.RecommendedVideoAdapter;
import com.example.youtube.entities.Comment;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.CommentRepository;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;
import com.example.youtube.viewModels.CommentViewModel;
import com.example.youtube.viewModels.VideoViewModel;
import com.example.youtube.viewModels.VideoViewModelFactory;

import java.util.Collections;
import java.util.List;

public class WatchVideoActivity extends AppCompatActivity implements CommentsListAdapter.CommentInteractionListener {

    private VideoView videoView;
    private CommentsListAdapter commentAdapter;
    private VideoRepository videoRepository;
    private CommentRepository commentRepository;
    private User loggedInUser = MyApplication.getCurrentUser();
    private boolean isCommentsVisible = false;
    private SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayout secondRefreshLayout;
    private Button btnToggleView;
    private int likeCount = 0;
    private boolean isLiked = false;
    private boolean isUnliked = false;
    private Video current;
    private ImageButton btnLike;
    private ImageButton btnUnlike;
    TextView tvLikeCount;
    private VideoViewModel recommendedVideoViewModel;
    private RecommendedVideoAdapter recommendedVideoAdapter;
    private TextView tvRecommendedVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);
        this.videoRepository = new VideoRepository(getApplication());
        this.commentRepository = new CommentRepository(getApplication());
        videoView = findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        ImageButton btnGoBack = findViewById(R.id.btnGoBack);
        tvRecommendedVideos = findViewById(R.id.tvRecommendedVideos);
        btnGoBack.setOnClickListener(v -> finish());
        btnLike = findViewById(R.id.btnLike);
        btnUnlike = findViewById(R.id.btnUnlike);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        ImageButton btnAddComment = findViewById(R.id.btnAddComment);
        EditText etComment = findViewById(R.id.etComment);
        RecyclerView lstRecommendedVideos = findViewById(R.id.lstRecommendedVideos);
        recommendedVideoAdapter = new RecommendedVideoAdapter(this);
        VideoViewModelFactory recommendedVideosFactory = new VideoViewModelFactory(getApplication());
        recommendedVideoViewModel = new ViewModelProvider(this, recommendedVideosFactory).get(VideoViewModel.class);
        recommendedVideoViewModel.getRecommendedVideos().observe(this, videos -> recommendedVideoAdapter.setRecommendedvideos(videos));
        lstRecommendedVideos.setAdapter(recommendedVideoAdapter);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        lstRecommendedVideos.setLayoutManager(horizontalLayoutManager);

        refreshLayout = findViewById(R.id.refreshLayout);
        secondRefreshLayout = findViewById(R.id.secondRefreshLayout);
        btnToggleView = findViewById(R.id.btnToggleView);
        showRecommendedVideos();
        btnToggleView.setOnClickListener(v -> {
            if (isCommentsVisible) {
                showRecommendedVideos();
            } else {
                showComments();
            }
        });

        initializeViews();
        initializeCommentsList();

        ImageButton btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, btnShare);
            popupMenu.getMenuInflater().inflate(R.menu.share_menu, popupMenu.getMenu());
            popupMenu.show();
        });


        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOffline()) {
                    Toast.makeText(WatchVideoActivity.this, "you are offline, to add a comment please connect to the internet first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (loggedInUser == null) {
                    Toast.makeText(WatchVideoActivity.this, "You need to be logged in to leave a comment.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String commentText = etComment.getText().toString().trim();
                if (!TextUtils.isEmpty(commentText)) {
                    Comment newComment = new Comment(getVideoId(), loggedInUser.getUsername(), commentText);
                    commentRepository.addComment(loggedInUser.getToken(), newComment);
                    etComment.setText("");
                    initializeCommentsList();
                }

            }
        });

        btnLike.setOnClickListener(v -> {
            if (isOffline()) {
                Toast.makeText(this, "you are offline, to like a video please connect to the internet first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (loggedInUser == null) {
                Toast.makeText(WatchVideoActivity.this, "You need to be logged in to like a video", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!loggedInUser.getLikedVideos().contains(getVideoId())) {
                likeCount++;
                isLiked = true;
                updateLikeDislikeUI();
                UserRepository.getInstance(this).editLikes(loggedInUser.getToken(), loggedInUser.getUsername(),
                        getVideoId(), current.getLikes() + 1);
                List<String> likedVideos = loggedInUser.getLikedVideos();
                if (likedVideos != null) {
                    if (!likedVideos.contains(getVideoId())) {
                        likedVideos.add(getVideoId());
                    }
                }
                loggedInUser.setLikedVideos(likedVideos);
                if (isUnliked) {
                    btnUnlike.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
                    isUnliked = false;
                    List<String> unlikedVideos = loggedInUser.getUnLikedVideos();
                    if (unlikedVideos != null) {
                        if (unlikedVideos.contains(getVideoId())) {
                            unlikedVideos.removeAll(Collections.singleton(getVideoId()));
                        }
                    }
                    loggedInUser.setUnLikedVideos(unlikedVideos);
                }
                UserRepository.getInstance(this).editUserLikes(loggedInUser);
            } else {
                likeCount--;
                isLiked = false;
                updateLikeDislikeUI();
                UserRepository.getInstance(this).editLikes(loggedInUser.getToken(), loggedInUser.getUsername(),
                        getVideoId(), current.getLikes() - 1);
                List<String> likedVideos = loggedInUser.getLikedVideos();
                if (likedVideos != null) {
                    if (likedVideos.contains(getVideoId())) {
                        likedVideos.removeAll(Collections.singleton(getVideoId()));
                    }
                }
                loggedInUser.setLikedVideos(likedVideos);
                UserRepository.getInstance(this).editUserLikes(loggedInUser);
            }
        });

        btnUnlike.setOnClickListener(v -> {
            if (isOffline()) {
                Toast.makeText(this, "you are offline, to unlike a video please connect to the internet first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (loggedInUser == null) {
                Toast.makeText(WatchVideoActivity.this, "You need to be logged in to unlike a video", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!loggedInUser.getUnLikedVideos().contains(getVideoId())) {
                btnUnlike.setImageResource(R.drawable.baseline_thumb_down_24);
                isUnliked = true;
                btnUnlike.setImageResource(R.drawable.baseline_thumb_down_24);
                if (loggedInUser.getLikedVideos().contains(getVideoId())) {
                    likeCount--;
                    isLiked = false;
                    updateLikeDislikeUI();
                    UserRepository.getInstance(this).editLikes(loggedInUser.getToken(), loggedInUser.getUsername(),
                            getVideoId(), current.getLikes() - 1);
                    List<String> likedVideos = loggedInUser.getLikedVideos();
                    if (likedVideos != null) {
                        if (likedVideos.contains(getVideoId())) {
                            likedVideos.removeAll(Collections.singleton(getVideoId()));
                        }
                    }
                    List<String> unlikedVideos = loggedInUser.getUnLikedVideos();
                    if (unlikedVideos != null) {
                        if (!unlikedVideos.contains(getVideoId())) {
                            unlikedVideos.add(getVideoId());
                        }
                    }
                    loggedInUser.setLikedVideos(likedVideos);
                    loggedInUser.setUnLikedVideos(unlikedVideos);
                    UserRepository.getInstance(this).editUserLikes(loggedInUser);
                }
            } else {
                btnUnlike.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
                isUnliked = false;
                List<String> unlikedVideos = loggedInUser.getUnLikedVideos();
                if (unlikedVideos != null) {
                    if (unlikedVideos.contains(getVideoId())) {
                        unlikedVideos.removeAll(Collections.singleton(getVideoId()));
                    }
                }
                loggedInUser.setUnLikedVideos(unlikedVideos);
                UserRepository.getInstance(this).editUserLikes(loggedInUser);
            }
        });
    }
    private void updateLikeDislikeUI() {
        if (isLiked) {
            btnLike.setImageResource(R.drawable.baseline_thumb_up_24);
            tvLikeCount.setText(String.valueOf(likeCount));
        } else {
            btnLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
            tvLikeCount.setText(String.valueOf(likeCount));
        }
        if (isUnliked) {
            btnUnlike.setImageResource(R.drawable.baseline_thumb_down_24);
        } else {
            btnUnlike.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
        }
    }

    private void initializeViews() {
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvContent = findViewById(R.id.tvContent);
        TextView tvDuration = findViewById(R.id.tvDuration);
        TextView tvViews = findViewById(R.id.tvViews);
        TextView tvUploadDate = findViewById(R.id.tvUploadDate);


        String videoId = getIntent().getStringExtra("videoId");
        LiveData<Video> videoLiveData = videoRepository.getVideoById(videoId);
        videoLiveData.observe(this, new Observer<Video>() {
            @Override
            public void onChanged(Video video) {
                if (video != null) {
                    current = video;
                    tvAuthor.setText(video.getUploader());
                    tvContent.setText(video.getTitle());
                    tvDuration.setText(video.getDuration());
                    tvViews.setText(String.valueOf(video.getVisits()));
                    tvUploadDate.setText(video.getUploadDate());
                    likeCount = video.getLikes();
                    tvLikeCount.setText(String.valueOf(likeCount));
                    initializeVideoPlayer(video);
                    if (loggedInUser != null && loggedInUser.getLikedVideos().contains(videoId)) {
                        isLiked = true;
                    }
                    if (loggedInUser != null && loggedInUser.getUnLikedVideos().contains(videoId)) isUnliked = true;
                    updateLikeDislikeUI();
                    tvAuthor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isOffline()) {
                                Toast.makeText(WatchVideoActivity.this, "you are offline, please connect to the internet first", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent intent = new Intent(WatchVideoActivity.this, UserPageActivity.class);
                            intent.putExtra("username", video.getUploader());
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private boolean isOffline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isConnected();
    }
    private void initializeVideoPlayer(Video video) {
        if (video != null) {
            String videoFile;
            if (isOffline() && video.getVideo() != null) {
                Toast.makeText(this, "you are offline, to watch the video please connect to the internet first", Toast.LENGTH_SHORT).show();
            } else {
                videoFile = "http://10.0.2.2:8080/" + video.getVideo();
                videoView.setVideoPath(videoFile);
                videoView.start();
            }
        }
    }


    private String getVideoId() {
        return getIntent().getStringExtra("videoId");
    }

    private void initializeCommentsList() {
        RecyclerView lstComments = findViewById(R.id.lstComments);
        commentAdapter = new CommentsListAdapter(this, this);

        CommentViewModel viewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        viewModel.loadComments(this.getVideoId()).observe(this, comments -> {
            commentAdapter.setComments(comments);
        });
        lstComments.setAdapter(commentAdapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onDeleteComment(Comment comment) {
        if (loggedInUser != null && loggedInUser.getUsername().equals(comment.getUsername())) {
            CommentRepository.getInstance(null).deleteComment(loggedInUser.getToken(), loggedInUser.getUsername(), comment.getId());
            initializeCommentsList();
            return;
        }
        Toast.makeText(this, "You need to be logged in and the owner of this comment to delete it.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditComment(Comment comment) {
        if (loggedInUser != null && loggedInUser.getUsername().equals(comment.getUsername())) {
            CommentRepository.getInstance(null).editComment(loggedInUser.getToken(), loggedInUser.getUsername(), comment.getId(), comment);
            initializeCommentsList();
            return;
        }
        Toast.makeText(this, "You need to be logged in and the owner of this comment to delete it.", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    private void showComments() {
        refreshLayout.setVisibility(View.VISIBLE);
        secondRefreshLayout.setVisibility(View.GONE);
        btnToggleView.setText("back");
        tvRecommendedVideos.setVisibility(View.GONE);
        isCommentsVisible = true;
    }

    @SuppressLint("SetTextI18n")
    private void showRecommendedVideos() {
        refreshLayout.setVisibility(View.GONE);
        secondRefreshLayout.setVisibility(View.VISIBLE);
        tvRecommendedVideos.setVisibility(View.VISIBLE);
        btnToggleView.setText("Comments");
        isCommentsVisible = false;
    }
}