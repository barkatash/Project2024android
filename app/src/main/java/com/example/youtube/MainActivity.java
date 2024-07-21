package com.example.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.youtube.adapters.UsersListAdapter;
import com.example.youtube.adapters.VideosListAdapter;
import com.example.youtube.databinding.ActivityMainBinding;
import com.example.youtube.entities.Comment;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.viewModels.CommentViewModel;
import com.example.youtube.viewModels.UserViewModel;
import com.example.youtube.viewModels.VideoViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private VideosListAdapter videoAdapter;
    private UsersListAdapter userAdapter;
    private ImageView youBtn;
    private VideoViewModel videoViewModel;
    private UserViewModel userViewModel;
    private CommentViewModel commentViewModel;

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
        lstVideos.setAdapter(videoAdapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView lstUsers = binding.lstUsers;
        userAdapter = new UsersListAdapter(this);
        lstUsers.setAdapter(userAdapter);
        lstUsers.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModels
        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        // Observe LiveData
        videoViewModel.getAllVideos().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                videoAdapter.setVideos(videos);
            }
        });

        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userAdapter.setUsers(users);
            }
        });

        commentViewModel.getAllComments().observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                // Handle comments here if you want to display them in the activity
            }
        });

        ImageButton searchBtn = binding.searchBtn;
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });

        ImageButton homeBtn = binding.homeBtn;
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        ImageButton btnUploadVideo = binding.uploadBtn;
        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userViewModel.getLoggedInUser().getValue() == null) {
                    Toast.makeText(MainActivity.this, "You need to be logged in to upload a video", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(i);
            }
        });

        youBtn = binding.youBtn;
        updateProfileButtonState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProfileButtonState();
    }

    private void updateProfileButtonState() {
        if (userViewModel.getLoggedInUser().getValue() != null) {
            setLoggedInState();
        } else {
            setLoggedOutState();
        }
    }

    private void setLoggedInState() {
        binding.youBtnText.setText("Log Out");
        User loggedInUser = userViewModel.getLoggedInUser().getValue();
        if (loggedInUser != null) {
            String imageUrl = loggedInUser.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(loggedInUser.getImageUrl())
                        .transform(new CircleCrop())
                        .into(youBtn);
            } else {
                youBtn.setImageResource(R.drawable.baseline_account_circle_24);
            }
        }
        youBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.logoutUser();
                setLoggedOutState();
            }
        });
    }

    private void setLoggedOutState() {
        binding.youBtnText.setText("Log In");
        youBtn.setImageResource(R.drawable.baseline_account_circle_24);
        youBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });
    }
}
