package com.example.youtube;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.VideoRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UploadActivity extends AppCompatActivity {


    private static final int REQUEST_VIDEO_FILE = 1;
    private static final int REQUEST_IMAGE_FILE = 2;
    private VideoView videoUpload;
    private EditText newContent;
    private ImageView videoUploadImageView;
    private final Video newVideo = new Video();
    User loggedInUser;
    String duration = "2:00";
    VideoRepository videoRepository;
    private boolean videoFileUpload = false;
    private boolean imageFileUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        videoRepository = new VideoRepository(getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        videoUpload = findViewById(R.id.videoViewUpload);
        MediaController mediaController = new MediaController(this);
        videoUpload.setMediaController(mediaController);
        videoUploadImageView = findViewById(R.id.videoUploadImage);
        newContent = findViewById(R.id.etNewContent);
        loggedInUser = MyApplication.getCurrentUser();

        Button buttonSelectVideo = findViewById(R.id.btnUploadVideo);
        Button buttonSelectImage = findViewById(R.id.btnUploadImage);
        Button buttonCancel = findViewById(R.id.btnCancel);
        buttonSelectVideo.setOnClickListener(v -> selectVideo());
        buttonSelectImage.setOnClickListener(v -> selectImage());
        buttonCancel.setOnClickListener(v -> finish());

        Button buttonUploadVideo = findViewById(R.id.btnSaveVideo);
        buttonUploadVideo.setOnClickListener(v -> uploadVideo());
    }

    private void selectImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, REQUEST_IMAGE_FILE);
    }

    private void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_VIDEO_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_FILE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                try {
                    Uri imageUri = data.getData();
                    saveImageToInternalStorage(imageUri);
                    imageFileUpload = true;
                    Bitmap bitmap;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());
                        bitmap = ImageDecoder.decodeBitmap(source);
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    }
                    videoUploadImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == REQUEST_VIDEO_FILE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri videoUri = data.getData();
                saveVideoToInternalStorage(videoUri);
                videoFileUpload = true;
                videoUpload.setVideoURI(videoUri);
                videoUpload.start();
            }
        }
    }

    private void uploadVideo() {
        if (isOffline()) {
            Toast.makeText(this, "you are offline, to upload a video please connect to the internet first", Toast.LENGTH_SHORT).show();
            return;
        }
        File videoFile = new File(getFilesDir(), "video_" + newVideo.getId() + ".mp4");
        File imageFile = new File(getFilesDir(), "image_" + newVideo.getId() + ".jpg");
        if (videoFileUpload && imageFileUpload) {
            newVideo.setUploader(loggedInUser.getUsername());
            newVideo.setTitle(newContent.getText().toString().trim());
            newVideo.setDuration(duration);
            newVideo.setVisits(0);
            videoRepository.addVideo(loggedInUser.getToken(), newVideo, imageFile, videoFile);
            Toast.makeText(this, "Video Uploaded Successfully", Toast.LENGTH_SHORT).show();
            goToMainActivity(null);
        } else {
            Toast.makeText(this, "Please select a video and image first", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            String imageId = newVideo.getId();
            String imageFileName = "image_" + imageId + ".jpg";
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            File imageFile = new File(getFilesDir(), imageFileName);
            OutputStream outputStream = new FileOutputStream(imageFile);

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
    private void saveVideoToInternalStorage(Uri videoUri) {
        try {
            String videoId = newVideo.getId();
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
            duration = getVideoDuration(videoUri);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getVideoDuration(Uri videoUri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoUri);
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long durationMillis = Long.parseLong(duration);
        long durationSeconds = durationMillis / 1000;
        long minutes = durationSeconds / 60;
        long seconds = durationSeconds % 60;
        retriever.release();

        return String.format("%02d:%02d", minutes, seconds);
    }

    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private boolean isOffline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isConnected();
    }
}