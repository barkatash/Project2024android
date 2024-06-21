package com.example.youtube;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.databinding.ActivityWatchVideoBinding;
import com.example.youtube.entities.Video;

public class WatchVideoActivity extends AppCompatActivity {

    private ActivityWatchVideoBinding binding;
    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videoView = binding.videoView;
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        // Retrieve videoId from intent extras
        int videoId = getIntent().getIntExtra("videoId", -1);
        if (videoId != -1) {
            Video video = VideoRepository.getVideoById(videoId);
            if (video != null) {
                // Construct path to the video resource
                String path = "android.resource://" + getPackageName() + "/" + video.getVideo();

                // Set the video URI to the VideoView
                videoView.setVideoURI(Uri.parse(path));
                videoView.setMediaController(mediaController);

                // Start playing the video
                videoView.start();

                // Update UI with video details
                updateUI(video);
            }
        }
    }

    private void updateUI(Video video) {
        binding.tvAuthor.setText(video.getAuthor());
        binding.tvContent.setText(video.getContent());
        // Set other UI elements as needed
        binding.tvDuration.setText(video.getDuration());
        binding.tvViews.setText(video.getViews());
        binding.tvUploadDate.setText(video.getUploadDate());
    }
}
