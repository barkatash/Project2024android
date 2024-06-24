package com.example.youtube;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.entities.User;

import java.io.IOException;
import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText usernameInput, displayNameInput, passwordInput, verifyPasswordInput;
    private Button signInButton, uploadImageButton;
    private ImageView profileImageView;
    private Uri imageUri;
    public static ArrayList<User> users = new ArrayList<>();

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
                openImagePicker();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                }
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSignUp() {
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

        User newUser = new User(username, displayName, password, imageUri != null ? imageUri.toString() : null);
        users.add(newUser);

        Toast.makeText(this, "User signed up successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignInActivity.this, LoginActivity.class));
        finish();
    }
}
