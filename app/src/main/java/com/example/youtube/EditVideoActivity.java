package com.example.youtube;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.youtube.entities.Video;
import com.example.youtube.repositories.VideoRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EditVideoActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_FILE = 1;
    private static final int REQUEST_IMAGE_FILE = 2;
    private EditText etAuthor;
    private EditText etContent;
    private EditText etDuration;
    private Button btnSelectVideo;
    private Button btnSelectImage;
    private Button btnSave;
    VideoRepository videoRepository = new VideoRepository();
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

        String videoId = getIntent().getStringExtra("videoId");
        LiveData<Video> videoLiveData;
        videoLiveData = videoRepository.getVideoById(videoId);
        videoLiveData.observe(this, cur -> {
            if (cur != null) {
                currentVideo = cur; // Save the video data
            } else {
            }
        });

        MediaController mediaController = new MediaController(this);
        videoViewUpload.setMediaController(mediaController);

        if (currentVideo != null) {
            String videoFile = currentVideo.getVideo();
            if (videoFile != null) {
                videoViewUpload.setVideoPath(videoFile);
            } else {
                String path = "android.resource://" + getPackageName() + "/" + currentVideo.getVideo();
                videoViewUpload.setVideoURI(Uri.parse(path));
            }
            videoViewUpload.start();
        }

        if (videoId != null) {
            if (currentVideo != null) {
                etAuthor.setText(currentVideo.getUploader());
                etContent.setText(currentVideo.getTitle());
                etDuration.setText(currentVideo.getDuration());
            }
        }

        btnSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoPicker();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentVideo.setUploader(etAuthor.getText().toString().trim());
                currentVideo.setTitle(etContent.getText().toString().trim());
                currentVideo.setDuration(etDuration.getText().toString().trim());
                //VideoRepository.getInstance(getApplicationContext()).updateVideo(currentVideo);
                finish();
            }
        });
    }

    private void openImagePicker() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        startActivityForResult(pickIntent, REQUEST_IMAGE_FILE);
    }

    private void openVideoPicker() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("video/*");

        startActivityForResult(pickIntent, REQUEST_VIDEO_FILE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_FILE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                try {
                    Bitmap bitmap;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());
                        bitmap = ImageDecoder.decodeBitmap(source);
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    }
                    videoImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == REQUEST_VIDEO_FILE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri videoUri = data.getData();
                saveVideoToInternalStorage(videoUri);
                videoViewUpload.setVideoURI(videoUri);
                videoViewUpload.start();
            }
        }
    }

    private void saveVideoToInternalStorage(Uri videoUri) {
        try {
            String videoId = currentVideo.getId();
            String videoFileName = "video_" + videoId + ".mp4";
            InputStream inputStream = getContentResolver().openInputStream(videoUri);
            File videoFile = new File(getFilesDir(), videoFileName);
            OutputStream outputStream = new FileOutputStream(videoFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
