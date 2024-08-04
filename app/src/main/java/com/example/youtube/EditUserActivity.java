package com.example.youtube;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.youtube.entities.User;
import com.example.youtube.repositories.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditUserActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private ImageView profileImageView;
    private EditText editTextDisplayName;
    private EditText editTextPassword;
    private final User newUser = new User();
    private Button buttonSave;
    private Button buttonChangeImage;
    private UserRepository userRepository;
    private User loggedInUser;
    private File imageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        userRepository = UserRepository.getInstance(this);
        loggedInUser = MyApplication.getCurrentUser();

        profileImageView = findViewById(R.id.imageViewProfile);
        editTextDisplayName = findViewById(R.id.editTextDisplayName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSave = findViewById(R.id.buttonSave);
        buttonChangeImage = findViewById(R.id.buttonChangeImage);

        if (loggedInUser != null) {
            if (loggedInUser.getImageUrl() != null) {
                String imageUrl = "http://10.0.2.2:8080/" + loggedInUser.getImageUrl();
                Glide.with(this).load(imageUrl).transform(new CircleCrop()).into(profileImageView);
            }
        }

        buttonChangeImage.setOnClickListener(v -> checkCameraPermission());
        buttonSave.setOnClickListener(v -> saveUser());
    }
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openImagePicker();
        }
    }
    private void openImagePicker() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooserIntent = Intent.createChooser(pickIntent, "Select or take a new Picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
        startActivityForResult(chooserIntent, REQUEST_IMAGE_PICK);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                openImagePicker();
                Toast.makeText(this, "Camera permission is required to upload profile picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            if (data != null && data.getData() != null) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());
                        bitmap = ImageDecoder.decodeBitmap(source);
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
            }

            if (bitmap != null) {
                imageFile = saveBitmapToFile(bitmap);
                if (imageFile != null) {
                    newUser.setImageUrl(imageFile.getAbsolutePath());
                    profileImageView.setImageBitmap(bitmap);
                }
            }
        }
    }


    private File saveBitmapToFile(Bitmap bitmap) {
        File storageDir = getExternalFilesDir("profile_images");
        if (storageDir == null) {
            return null;
        }
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "JPEG" + timeStamp + ".jpg";
        File imageFile = new File(storageDir, fileName);
        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageFile;
    }

    private void saveUser() {
        String displayName = editTextDisplayName.getText().toString();
        String password = editTextPassword.getText().toString();


        if (!password.isEmpty() && (password.length() < 8 || password.length() > 20)) {
            Toast.makeText(this, "Password must be 8-20 characters long", Toast.LENGTH_SHORT).show();
            return;
        }
        newUser.setUsername(loggedInUser.getUsername());
        newUser.setToken(loggedInUser.getToken());
        newUser.setDisplayName(loggedInUser.getDisplayName());
        newUser.setPassword(loggedInUser.getPassword());
        newUser.setPassword(password);
        newUser.setDisplayName(displayName);
        UserRepository.getInstance(this).editUser(newUser, imageFile);

        Toast.makeText(this, "User updated successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
