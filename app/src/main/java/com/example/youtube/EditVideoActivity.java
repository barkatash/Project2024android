package com.example.youtube;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.entities.Video;

public class EditVideoActivity extends AppCompatActivity {

    private EditText etAuthor;
    private EditText etContent;
    private EditText etDuration;
    private Button btnSave;

    private Video currentVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        // Initialize views
        etAuthor = findViewById(R.id.etAuthor);
        etContent = findViewById(R.id.etContent);
        etDuration = findViewById(R.id.etDuration);
        btnSave = findViewById(R.id.btnSave);

        // Get video ID from intent
        int videoId = getIntent().getIntExtra("videoId", -1);
        if (videoId != -1) {
            currentVideo = VideoRepository.getVideoById(videoId);
            if (currentVideo != null) {
                // Populate EditText fields with current video details
                etAuthor.setText(currentVideo.getAuthor());
                etContent.setText(currentVideo.getContent());
                etDuration.setText(currentVideo.getDuration());
            }
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentVideo.setAuthor(etAuthor.getText().toString().trim());
                currentVideo.setContent(etContent.getText().toString().trim());
                currentVideo.setDuration(etDuration.getText().toString().trim());
                VideoRepository.getInstance().updateVideo(currentVideo);
                finish();
            }
        });
    }
}
