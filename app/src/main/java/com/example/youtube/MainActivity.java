package com.example.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                recreate();
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


        RecyclerView lstUsers = binding.lstUsers;
        userAdapter = new UsersListAdapter(this);
        lstUsers.setAdapter(userAdapter);
        lstUsers.setLayoutManager(new LinearLayoutManager(this));

        UsersManager usersManager = UsersManager.getInstance();
        List<User> users = usersManager.getUsers();
        userAdapter.setUsers(users);

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

        ImageButton youBtn = binding.youBtn;
        youBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the video list when returning from the search activity
        VideoRepository videoRepository = VideoRepository.getInstance();
        List<Video> videos = videoRepository.getVideos();
        videoAdapter.setVideos(videos);

        // Refresh the user list in case there are new users
        UsersManager usersManager = UsersManager.getInstance();
        List<User> users = usersManager.getUsers();
        userAdapter.setUsers(users);
    }
}
