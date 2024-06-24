package com.example.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.databinding.ActivityLoginBinding;
import com.example.youtube.entities.User;

public class LogInActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private ActivityLoginBinding binding;
    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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

        for (User user : SignInActivity.users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                logInButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Optional: close LoginActivity so it's not in the back stack
                    }
                }, 500);
                return;
            }
        }

        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
    }

}
