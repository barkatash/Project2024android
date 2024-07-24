package com.example.youtube;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.youtube.adapters.UsersListAdapter;
import com.example.youtube.adapters.VideosListAdapter;
import com.example.youtube.databinding.ActivityMainBinding;
import com.example.youtube.entities.User;
import com.example.youtube.viewmodels.VideosViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private VideosListAdapter videoAdapter;
    private UsersListAdapter userAdapter;
    private ImageView youBtn;


    @SuppressLint("NotifyDataSetChanged")
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


        RecyclerView lstVideos = binding.lstVideos;
        videoAdapter = new VideosListAdapter(this);

        VideosViewModel viewModel = new ViewModelProvider(this).get(VideosViewModel.class);
        viewModel.getVideos().observe(this, videos -> {
            videoAdapter.setVideos(videos);
            // Optionally log the videos to debug
            Log.d("MainActivity", "Videos: " + videos);
        });

        lstVideos.setAdapter(videoAdapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));

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
            //videoRepository.resetVideos();
            startActivity(i);
        });

        ImageButton btnUploadVideo = binding.uploadBtn;
        btnUploadVideo.setOnClickListener(v -> {
            if (!UsersManager.getInstance().isLoggedIn()) {
                Toast.makeText(MainActivity.this, "You need to be logged in to upload a video", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(MainActivity.this, UploadActivity.class);
            startActivity(i);
        });

        youBtn = binding.youBtn;
        updateProfileButtonState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //VideoRepository videoRepository = VideoRepository.getInstance(getApplicationContext());
        //List<Video> videos = videoRepository.getVideos();
        //videoAdapter.setVideos(videos);

        UsersManager usersManager = UsersManager.getInstance();
        List<User> users = usersManager.getUsers();
        userAdapter.setUsers(users);

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
        youBtn.setImageResource(R.drawable.baseline_account_circle_24);
        if (loggedInUser != null) {
             if (loggedInUser.getImageUrl() != null && !loggedInUser.getImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(UsersManager.getInstance().getLoggedInUser().getImageUrl())
                        .transform(new CircleCrop())
                        .into(youBtn);
            }
        }
        youBtn.setOnClickListener(v -> {
            UsersManager.getInstance().logoutUser();
            setLoggedOutState();
        });
    }

    private void setLoggedOutState() {
        binding.youBtnText.setText("Log In");
        UsersManager.getInstance().setLoggedInUser(null);
        youBtn.setImageResource(R.drawable.baseline_account_circle_24);
        youBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(i);
        });
    }
}
