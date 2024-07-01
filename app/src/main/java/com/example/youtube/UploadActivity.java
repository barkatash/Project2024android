package com.example.youtube;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UploadActivity extends AppCompatActivity {

    private static final int SELECT_VIDEO_REQUEST_CODE = 100;
    private Uri selectedVideoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        VideoView videoUpload = findViewById(R.id.videoViewUpload);
        MediaController mediaController = new MediaController(this);
        videoUpload.setMediaController(mediaController);

        Button buttonSelectVideo = findViewById(R.id.btnUploadVideo);
        Button buttonCancel = findViewById(R.id.btnCancel);
        buttonSelectVideo.setOnClickListener(v -> selectVideo());
        buttonSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideo();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button buttonUploadVideo = findViewById(R.id.btnSaveVideo);
        buttonUploadVideo.setOnClickListener(v -> uploadVideo());
    }

    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    private void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, SELECT_VIDEO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_VIDEO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedVideoUri = data.getData();
            Toast.makeText(this, "Video Selected: " + selectedVideoUri, Toast.LENGTH_SHORT).show();
            Log.d("UploadActivity", "Selected Video Uri: " + selectedVideoUri);
        }
    }

    private void uploadVideo() {
        if (selectedVideoUri != null) {

        } else {
            Toast.makeText(this, "Please select a video first", Toast.LENGTH_SHORT).show();
        }
    }
}
