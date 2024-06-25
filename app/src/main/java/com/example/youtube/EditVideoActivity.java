package com.example.youtube;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.entities.Video;

public class EditVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        int videoId = getIntent().getIntExtra("videoId", -1);
        if (videoId != -1) {
            Video video = VideoRepository.getVideoById(videoId);
            // Implement your edit logic using the video object
        } else {
            // Handle error or default behavior
        }
    }
}