package com.example.youtube;

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
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.adapters.CommentsListAdapter;
import com.example.youtube.entities.Comment;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;

public class WatchVideoActivity extends AppCompatActivity implements CommentsListAdapter.CommentInteractionListener {

    private VideoView videoView;
    private CommentsListAdapter adapter;
    private List<Comment> comments;
    VideoRepository videoRepository = new VideoRepository();
    private List<Comment> filteredComments;
    private int likeCount = 0;
    private boolean isLiked = false;
    private boolean isUnliked = false;
    Video video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);

        videoView = findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        if (UsersManager.getInstance().getLoggedInUser() != null) {
            isLiked = UsersManager.getInstance().getLoggedInUser().getLikedVideos().contains(getVideoId());
            isUnliked = UsersManager.getInstance().getLoggedInUser().getUnLikedVideos().contains(getVideoId());
        }

        initializeViews();
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


        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UsersManager.getInstance().isLoggedIn()) {
                    Toast.makeText(WatchVideoActivity.this, "You need to be logged in to leave a comment.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String commentText = etComment.getText().toString().trim();
                if (!TextUtils.isEmpty(commentText)) {
                    Comment newComment = new Comment(getVideoId(), UsersManager.getInstance().getLoggedInUser(), commentText, "now", 0, 0);
                    newComment.setId(CommentsManager.getNextCommentId());
                    CommentsManager.getInstance().addComment(newComment);
                    etComment.setText("");
                    initializeCommentsList();
                }
            }
        });

        btnLike.setOnClickListener(v -> {
            if (!UsersManager.getInstance().isLoggedIn()) {
                Toast.makeText(WatchVideoActivity.this, "You need to be logged in to like a video", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isLiked) {
                likeCount++;
                isLiked = true;
                btnLike.setImageResource(R.drawable.baseline_thumb_up_24);
                List<String> newLikedVideos = UsersManager.getInstance().getLoggedInUser().getLikedVideos();
                newLikedVideos.add(getVideoId());
                UsersManager.getInstance().getLoggedInUser().setLikedVideos(newLikedVideos);

                if (isUnliked) {
                    btnUnlike.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
                    isUnliked = false;
                    List<String> newUnLikedVideos = UsersManager.getInstance().getLoggedInUser().getUnLikedVideos();
                    int index = newUnLikedVideos.indexOf(getVideoId());
                    newUnLikedVideos.remove(index);
                    UsersManager.getInstance().getLoggedInUser().setUnLikedVideos(newUnLikedVideos);
                }
            } else {
                likeCount--;
                isLiked = false;
                btnLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                List<String> newLikedVideos = UsersManager.getInstance().getLoggedInUser().getLikedVideos();
                int index = newLikedVideos.indexOf(getVideoId());
                newLikedVideos.remove(index);
                UsersManager.getInstance().getLoggedInUser().setLikedVideos(newLikedVideos);
            }
            updateLikeDislikeUI();
        });

        btnUnlike.setOnClickListener(v -> {
            if (!UsersManager.getInstance().isLoggedIn()) {
                Toast.makeText(WatchVideoActivity.this, "You need to be logged in to unlike a video", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isUnliked) {
                btnUnlike.setImageResource(R.drawable.baseline_thumb_down_24);
                isUnliked = true;
                List<String> newUnlikedVideos = UsersManager.getInstance().getLoggedInUser().getUnLikedVideos();
                newUnlikedVideos.add(getVideoId());
                UsersManager.getInstance().getLoggedInUser().setUnLikedVideos(newUnlikedVideos);

                if (isLiked) {
                    likeCount--;
                    isLiked = false;
                    btnLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                    List<String> newLikedVideos = UsersManager.getInstance().getLoggedInUser().getLikedVideos();
                    int index = newLikedVideos.indexOf(getVideoId());
                    newLikedVideos.remove(index);
                    UsersManager.getInstance().getLoggedInUser().setLikedVideos(newLikedVideos);
                }
            } else {
                btnUnlike.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
                isUnliked = false;
                List<String> newUnLikedVideos = UsersManager.getInstance().getLoggedInUser().getUnLikedVideos();
                int index = newUnLikedVideos.indexOf(getVideoId());
                newUnLikedVideos.remove(index);
                UsersManager.getInstance().getLoggedInUser().setUnLikedVideos(newUnLikedVideos);
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

        String videoId = getIntent().getStringExtra("videoId");
        LiveData<Video> videoLiveData = videoRepository.getVideoById(videoId);
        videoLiveData.observe(this, new Observer<Video>() {
            @Override
            public void onChanged(Video video) {
                if (video != null) {
                    tvAuthor.setText(video.getUploader());
                    tvContent.setText(video.getTitle());
                    tvDuration.setText(video.getDuration());
                    tvViews.setText(video.getVisits());
                    tvUploadDate.setText(video.getUploadDate());
                    likeCount = video.getLikes();
                    tvLikeCount.setText(String.valueOf(likeCount));
                    initializeVideoPlayer(video);
                }
            }
        });
    }


    private void initializeVideoPlayer(Video video) {
        if (video != null) {
            String videoFile = video.getVideo();
            if (videoFile != null && !videoFile.isEmpty()) {
                videoView.setVideoPath("http://10.0.2.2:8080/" + videoFile);
            }
            videoView.start();
        }
    }


    private String getVideoId() {
        return getIntent().getStringExtra("videoId");
    }

    private void initializeCommentsList() {
        RecyclerView recyclerView = findViewById(R.id.lstComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentsListAdapter(this, this);
        recyclerView.setAdapter(adapter);

        String videoId = getIntent().getStringExtra("videoId");
        comments = CommentsManager.getInstance().getComments();
        filteredComments = CommentsManager.getInstance().getCommentsForVideo(videoId);
        adapter.setComments(filteredComments);
    }

    @Override
    public void onDeleteComment(Comment comment) {
        CommentsManager.getInstance().deleteComment(comment);
        filteredComments.remove(comment);
        adapter.setComments(filteredComments);
    }

    private void updateLikeDislikeUI() {
       // String videoId = getIntent().getStringExtra("videoId");
//        video.setLikes(likeCount);
//        TextView tvLikeCount = findViewById(R.id.tvLikeCount);
//        tvLikeCount.setText(String.valueOf(likeCount));
    }
}
