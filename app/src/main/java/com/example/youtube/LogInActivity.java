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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.youtube.databinding.ActivityLoginBinding;
import com.example.youtube.entities.User;
import com.example.youtube.viewModels.UserViewModel;

public class LogInActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private ActivityLoginBinding binding;
    private Button logInButton;
    private SharedPreferences sharedPreferences;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        usernameInput = binding.loginUsernameInput;
        passwordInput = binding.loginPasswordInput;
        logInButton = binding.loginLogInButton;

        // Initialize UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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

        // Observe the loggedInUser LiveData
        userViewModel.getLoggedInUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Toast.makeText(LogInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    navigateToMainActivity();
                } else {
                    Toast.makeText(LogInActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleLogIn() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        userViewModel.loginUser(username, password);
    }

    private void navigateToMainActivity() {
        logInButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }
}
