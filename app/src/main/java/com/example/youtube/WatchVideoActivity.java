package com.example.youtube;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

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
    List<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MediaController mediaController = binding.mediaController;
        TextView amount = binding.amount;

        RecyclerView lstComments = binding.lstComments;
        adapter = new CommentsListAdapter(this);
        lstComments.setAdapter(adapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));

        if (comments.isEmpty()) {
            int idCounter = 0;
            comments.add(new Comment(1, "omer", "good song", "11 months ago", 30, 2));
            comments.add(new Comment(4, "bar", "love this!", "3 months ago", 10, 0));
            comments.add(new Comment(6, "yael", "amazing", "7 days ago", 4, 6));

            for (Comment comment : comments) {
                comment.setId(idCounter++);
            }

        }


        videoView = binding.videoView;
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        int videoId = getIntent().getIntExtra("videoId", -1);
        if (videoId != -1) {
            Video video = VideoRepository.getVideoById(videoId);
            if (video != null) {
                String path = "android.resource://" + getPackageName() + "/" + video.getVideo();

                videoView.setVideoURI(Uri.parse(path));
                videoView.setMediaController(mediaController);

                videoView.start();
                updateUI(video);
            }
        }

        List<Comment> filteredComments = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getVideoId() == videoId) {
                filteredComments.add(comment);
            }
        }


        ImageButton btnAddComment = binding.btnAddComment;
        EditText etComment = binding.etComment;
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString().trim();
                if (!TextUtils.isEmpty(comment)) {
                    Comment newComment = new Comment(videoId, "@user",comment,"now",0,0);
                    newComment.setId(filteredComments.size() + 1);
                    filteredComments.add(newComment);
                    comments.add(newComment);
                    etComment.setText("");
                    amount.setText(String.valueOf(filteredComments.size()));
                }

            }
        });

        adapter.setComments(filteredComments);
        amount.setText(String.valueOf(filteredComments.size()));

        ImageButton btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateUI(Video video) {
        binding.tvAuthor.setText(video.getAuthor());
        binding.tvContent.setText(video.getContent());
        binding.tvDuration.setText(video.getDuration());
        binding.tvViews.setText(video.getViews());
        binding.tvUploadDate.setText(video.getUploadDate());
    }
}
