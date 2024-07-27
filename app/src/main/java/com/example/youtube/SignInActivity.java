package com.example.youtube;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.youtube.entities.User;
import com.example.youtube.repositories.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SignInActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 2;
    private final User newUser = new User();
    private EditText usernameInput, displayNameInput, passwordInput, verifyPasswordInput;
    private Button signInButton, uploadImageButton;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_signin);

        usernameInput = findViewById(R.id.signin_usernameInput);
        displayNameInput = findViewById(R.id.signin_displayNameInput);
        passwordInput = findViewById(R.id.signin_passwordInput);
        verifyPasswordInput = findViewById(R.id.signin_verifyPasswordInput);
        signInButton = findViewById(R.id.signin_signInButton);
        uploadImageButton = findViewById(R.id.signin_uploadImageButton);
        profileImageView = findViewById(R.id.signin_profileImageView);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignIn();
            }
        });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openImagePicker();
        }
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

    private void openImagePicker() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooserIntent = Intent.createChooser(pickIntent, "Select or take a new Picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
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
                File imageFile = saveBitmapToFile(bitmap);
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
        String fileName = "profile_image_" + timeStamp + ".jpg";
        File imageFile = new File(storageDir, fileName);
        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageFile;
    }

    private void handleSignIn() {
        String username = usernameInput.getText().toString();
        String displayName = displayNameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String verifyPassword = verifyPasswordInput.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(displayName) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(verifyPassword)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8 || password.length() > 20) {
            Toast.makeText(this, "Password must be 8-20 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(verifyPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        newUser.setPassword(password);
        newUser.setUsername(username);
        newUser.setUserDisplayName(displayName);
        UserRepository.getInstance(this).addUser(newUser);

        Toast.makeText(this, "User signed up successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignInActivity.this, LogInActivity.class));
        finish();
    }
}