package com.example.youtube;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.databinding.ActivityWatchVideoBinding;
import com.example.youtube.entities.Video;

public class WatchVideoActivity extends AppCompatActivity {

    private ActivityWatchVideoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int videoId = getIntent().getIntExtra("videoId", -1);
        if (videoId != -1) {
            try {
                Video video = VideoRepository.getVideoById(videoId);
                if (video != null) {
                    updateUI(video);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUI(Video video) {
        binding.tvAuthor.setText(video.getAuthor());
        binding.tvContent.setText(video.getContent());
        binding.ivPic.setImageResource(video.getPic());
        binding.tvDuration.setText(video.getDuration());
        binding.tvViews.setText(video.getViews());
        binding.tvUploadDate.setText(video.getUploadDate());
    }
}
