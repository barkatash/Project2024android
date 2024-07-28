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

import com.example.youtube.api.UserAPI;
import com.example.youtube.databinding.ActivityLoginBinding;
import com.example.youtube.entities.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


        UserAPI userAPI = new UserAPI();

        userAPI.authenticateUser(username, password, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User loggedInUser = response.body();
                    String token = response.headers().get("Authorization"); // Extract token from response headers

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", loggedInUser.getUsername());
                    editor.putString("authToken", token);
                    editor.apply();

                    UsersManager.getInstance().setLoggedInUser(loggedInUser);

                    Toast.makeText(LogInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    logInButton.postDelayed(() -> {
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }, 500);
                } else {

                    Toast.makeText(LogInActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(LogInActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
