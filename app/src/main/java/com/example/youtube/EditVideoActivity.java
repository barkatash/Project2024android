package com.example.youtube;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.youtube.entities.Video;

public class EditVideoActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_FILE = 1;
    private static final int REQUEST_IMAGE_FILE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private EditText etAuthor;
    private EditText etContent;
    private EditText etDuration;
    private Button btnSelectVideo;
    private Button btnSelectImage;
    private Button btnSave;

    private VideoView videoViewUpload;
    private Video currentVideo;
    private ImageView videoImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        etAuthor = findViewById(R.id.etAuthor);
        etContent = findViewById(R.id.etContent);
        etDuration = findViewById(R.id.etDuration);
        btnSelectVideo = findViewById(R.id.btnSelectVideo);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSave = findViewById(R.id.btnSave);
        videoImageView = findViewById(R.id.videoImage);
        videoViewUpload = findViewById(R.id.videoViewUpload);

        MediaController mediaController = new MediaController(this);
        videoViewUpload.setMediaController(mediaController);

        int videoId = getIntent().getIntExtra("videoId", -1);
        if (videoId != -1) {
            currentVideo = VideoRepository.getVideoById(videoId);
            if (currentVideo != null) {
                etAuthor.setText(currentVideo.getAuthor());
                etContent.setText(currentVideo.getContent());
                etDuration.setText(currentVideo.getDuration());
            }
        }

        btnSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
                selectFile(REQUEST_VIDEO_FILE);
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
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
                currentVideo.setVideoFileUri(selectedFileUri);
                videoViewUpload.setVideoURI(selectedFileUri);
                videoViewUpload.start();
            } else if (requestCode == REQUEST_IMAGE_FILE) {
                videoImageView.setImageURI(selectedFileUri);
                currentVideo.setImageFileUri(selectedFileUri);
            }
            VideoRepository.getInstance().updateVideo(currentVideo);
        }
    }
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                Toast.makeText(this, "Camera permission is required to upload files", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
