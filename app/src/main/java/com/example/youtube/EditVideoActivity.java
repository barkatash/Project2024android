package com.example.youtube;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private EditText etTitle;
    private EditText etDuration;
    private EditText etDescription;
    private Button btnSelectVideo;
    private Button btnSelectImage;
    private Button btnSave;

    private VideoView videoViewUpload;
    private Video currentVideo;
    private ImageView videoImageView;
    private VideoRepository videoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        etAuthor = findViewById(R.id.etAuthor);
        etTitle = findViewById(R.id.etContent);
        etDuration = findViewById(R.id.etDuration);
        btnSelectVideo = findViewById(R.id.btnSelectVideo);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSave = findViewById(R.id.btnSave);
        videoImageView = findViewById(R.id.videoImage);
        videoViewUpload = findViewById(R.id.videoViewUpload);

        videoRepository = VideoRepository.getInstance(getApplicationContext());

        int videoId = getIntent().getIntExtra("videoId", -1);
        currentVideo = videoRepository.getVideoById(videoId);

        MediaController mediaController = new MediaController(this);
        videoViewUpload.setMediaController(mediaController);

        if (currentVideo != null) {
            String videoFile = currentVideo.getVideoFilePath();
            if (videoFile != null && !videoFile.isEmpty()) {
                videoViewUpload.setVideoPath(videoFile);
            } else {
                Toast.makeText(this, "No video file found.", Toast.LENGTH_SHORT).show();
            }
            videoViewUpload.start();

            etAuthor.setText(currentVideo.getAuthor());
            etTitle.setText(currentVideo.getTitle());
            etDuration.setText(currentVideo.getDuration());
            etDescription.setText(currentVideo.getDescription());

            // Set image if available
            if (currentVideo.getImageFilePath() != null && !currentVideo.getImageFilePath().isEmpty()) {
                File imgFile = new File(currentVideo.getImageFilePath());
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    videoImageView.setImageBitmap(bitmap);
                }
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
                if (currentVideo != null) {
                    currentVideo.setAuthor(etAuthor.getText().toString().trim());
                    currentVideo.setTitle(etTitle.getText().toString().trim());
                    currentVideo.setDuration(etDuration.getText().toString().trim());
                    currentVideo.setDescription(etDescription.getText().toString().trim());
                    videoRepository.update(currentVideo); // Update method to use repository
                    finish();
                }
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
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    }
                    videoImageView.setImageBitmap(bitmap);
                    saveImageToInternalStorage(imageUri);
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

                currentVideo.setVideoFilePath(getFilePathFromUri(videoUri));
            }
        }
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            int videoId = currentVideo.getVideoId();
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
            currentVideo.setImageFilePath(imageFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveVideoToInternalStorage(Uri videoUri) {
        try {
            int videoId = currentVideo.getVideoId();
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
            currentVideo.setVideoFilePath(videoFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
