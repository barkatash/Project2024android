package com.example.youtube;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.databinding.ActivityLoginBinding;

public class LogInActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private ActivityLoginBinding binding;
    private Button logInButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        usernameInput = findViewById(R.id.login_usernameInput);
        passwordInput = findViewById(R.id.login_passwordInput);
        logInButton = findViewById(R.id.login_logInButton);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogIn();
            }
        });

        Button signInButton = binding.loginSignInButton;
        signInButton.setOnClickListener(v -> {
            Intent i = new Intent(LogInActivity.this, SignInActivity.class);
            startActivity(i);
        });
    }

    private void handleLogIn() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (UsersManager.getInstance().loginUser(username, password)) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            logInButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 500);
            return;
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
