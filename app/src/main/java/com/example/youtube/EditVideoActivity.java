package com.example.youtube;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.entities.Video;

public class EditVideoActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_FILE = 1;
    private static final int REQUEST_IMAGE_FILE = 2;

    private EditText etAuthor;
    private EditText etContent;
    private EditText etDuration;
    private Button btnSelectVideo;
    private Button btnSelectImage;
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
        btnSelectVideo = findViewById(R.id.btnSelectVideo);
        btnSelectImage = findViewById(R.id.btnSelectImage);
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

        btnSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile(REQUEST_VIDEO_FILE);
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile(REQUEST_IMAGE_FILE);
            }
        });

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

    private void selectFile(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(requestCode == REQUEST_VIDEO_FILE ? "video/*" : "image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            if (requestCode == REQUEST_VIDEO_FILE) {
                currentVideo.setVideoFileUri(selectedFileUri.toString());
            } else if (requestCode == REQUEST_IMAGE_FILE) {
                currentVideo.setImageFileUri(selectedFileUri.toString());
            }
        }
    }
}
