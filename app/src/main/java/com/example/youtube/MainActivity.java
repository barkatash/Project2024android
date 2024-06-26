package com.example.youtube;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.youtube.adapters.UsersListAdapter;
import com.example.youtube.adapters.VideosListAdapter;
import com.example.youtube.databinding.ActivityMainBinding;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private VideosListAdapter videoAdapter;
    private UsersListAdapter userAdapter;
    private ImageButton youBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Dark Mode toggle
        ImageButton btnToggleDark = binding.modeBtn;
        btnToggleDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isDarkMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
                if (isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                recreate(); // Refresh activity to apply theme
            }
        });

        // Setup video list
        RecyclerView lstVideos = binding.lstVideos;
        videoAdapter = new VideosListAdapter(this);
        lstVideos.setAdapter(videoAdapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));

        VideoRepository videoRepository = VideoRepository.getInstance();
        List<Video> videos = videoRepository.getVideos();
        videoAdapter.setVideos(videos);

        // Setup user list
        RecyclerView lstUsers = binding.lstUsers;
        userAdapter = new UsersListAdapter(this);
        lstUsers.setAdapter(userAdapter);
        lstUsers.setLayoutManager(new LinearLayoutManager(this));

        UsersManager usersManager = UsersManager.getInstance();
        List<User> users = usersManager.getUsers();
        userAdapter.setUsers(users);

        // Handle navigation buttons
        ImageButton searchBtn = binding.searchBtn;
        searchBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        });

        ImageButton homeBtn = binding.homeBtn;
        homeBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            videoRepository.resetVideos();
            startActivity(i);
        });

        // Handle profile button (initial state)
        youBtn = binding.youBtn;
        updateProfileButtonState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh video list when returning from SearchActivity
        VideoRepository videoRepository = VideoRepository.getInstance();
        List<Video> videos = videoRepository.getVideos();
        videoAdapter.setVideos(videos);

        // Refresh user list in case there are new users
        UsersManager usersManager = UsersManager.getInstance();
        List<User> users = usersManager.getUsers();
        userAdapter.setUsers(users);

        // Update profile button state
        updateProfileButtonState();
    }

    private void updateProfileButtonState() {
        if (UsersManager.getInstance().isLoggedIn()) {
            setLoggedInState();
        } else {
            setLoggedOutState();
        }
    }


    private void setLoggedInState() {
        binding.youBtnText.setText("Log Out");
        User loggedInUser = UsersManager.getInstance().getLoggedInUser();
        if (loggedInUser != null) {
            String imageUrl = loggedInUser.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Load image using Glide
                Glide.with(this)
                        .load(Uri.parse(imageUrl)) // Parse the string to URI if necessary
                        .error(R.drawable.baseline_account_circle_24) // Error image if loading fails
                        .into(youBtn);
            } else {
                youBtn.setImageResource(R.drawable.baseline_account_circle_24); // Default profile icon
            }
        }
        youBtn.setOnClickListener(v -> {
            UsersManager.getInstance().logoutUser();
            setLoggedOutState();
        });
    }



    private void setLoggedOutState() {
        binding.youBtnText.setText("Log In");
        youBtn.setImageResource(R.drawable.baseline_account_circle_24); // Default profile icon
        youBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(i);
        });
    }
}
