package com.example.youtube;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
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
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_FILE = 1;
    private static final int REQUEST_IMAGE_FILE = 2;
    private VideoView videoUpload;
    private EditText newContent;
    private ImageView videoUploadImageView;
    private final Video newVideo = new Video();
    private String duration = "2:00";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        newVideo.setVideoId(VideoRepository.getNextVideoId());
        videoUpload = findViewById(R.id.videoViewUpload);
        MediaController mediaController = new MediaController(this);
        videoUpload.setMediaController(mediaController);
        videoUploadImageView = findViewById(R.id.videoUploadImage);
        newContent = findViewById(R.id.etNewContent);

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
                Uri imageUri = data.getData();
                saveImageToInternalStorage(imageUri);
                // Set the image preview
                Bitmap bitmap = BitmapFactory.decodeFile(newVideo.getImageFilePath());
                videoUploadImageView.setImageBitmap(bitmap);
            }
        }
        if (requestCode == REQUEST_VIDEO_FILE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri videoUri = data.getData();
                saveVideoToInternalStorage(videoUri);
                videoUpload.setVideoURI(videoUri);
                videoUpload.start();
                newVideo.setVideoFilePath(getFilePathFromUri(videoUri));
            }
        }
    }

    private void uploadVideo() {
        if (newVideo.getVideoFilePath() != null) {
            UserRepository userRepository = UserRepository.getInstance(getApplicationContext());
            User loggedInUser = userRepository.getLoggedInUser();
            if (loggedInUser != null) {
                newVideo.setAuthor(loggedInUser.getUserDisplayName());
                newVideo.setDescription(newContent.getText().toString().trim());
                newVideo.setDuration(duration);
                newVideo.setViews(0); // Use integer for views
                newVideo.setUploadDate(dateFormat.format(new Date())); // Set current date

                VideoRepository videoRepository = VideoRepository.getInstance(getApplicationContext());
                videoRepository.insert(newVideo);

                Toast.makeText(this, "Video Uploaded Successfully", Toast.LENGTH_SHORT).show();
                goToMainActivity(null);
            } else {
                Toast.makeText(this, "Unable to get user details", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select a video first", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveVideoToInternalStorage(Uri videoUri) {
        try {
            int videoId = newVideo.getVideoId();
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
            newVideo.setVideoFilePath(videoFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            int videoId = newVideo.getVideoId();
            String imageFileName = "image_" + videoId + ".jpg";
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
            newVideo.setImageFilePath(imageFile.getAbsolutePath());

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

    private String getFilePathFromUri(Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Video.Media.DATA };

        // Where id is equal to
        String sel = MediaStore.Video.Media._ID + "=?";

        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
