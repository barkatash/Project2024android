package com.example.youtube;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.youtube.adapters.UsersListAdapter;
import com.example.youtube.adapters.VideosListAdapter;
import com.example.youtube.databinding.ActivityMainBinding;
import com.example.youtube.entities.User;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.viewModels.VideoViewModel;
import com.example.youtube.viewModels.VideoViewModelFactory;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private VideosListAdapter videoAdapter;
    private UsersListAdapter userAdapter;
    private VideoViewModel videoViewModel;
    private ImageView youBtn;
    private UserRepository userRepository;
    private User loggedInUser;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = UserRepository.getInstance(this);

        ImageButton btnToggleDark = binding.modeBtn;
        btnToggleDark.setOnClickListener(view -> {
            boolean isDarkMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
            AppCompatDelegate.setDefaultNightMode(isDarkMode ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
            recreate();
        });

        RecyclerView lstVideos = binding.lstVideos;
        videoAdapter = new VideosListAdapter(this);

        VideoViewModelFactory factory = new VideoViewModelFactory(getApplication());
        videoViewModel = new ViewModelProvider(this, factory).get(VideoViewModel.class);
        videoViewModel.getVideos().observe(this, videos -> videoAdapter.setVideos(videos));

        lstVideos.setAdapter(videoAdapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView lstUsers = binding.lstUsers;
        userAdapter = new UsersListAdapter(this);
        lstUsers.setAdapter(userAdapter);
        lstUsers.setLayoutManager(new LinearLayoutManager(this));

        LiveData<List<User>> users = userRepository.getAllUsers();
        users.observe(this, userList -> userAdapter.setUsers(userList));

        ImageButton searchBtn = binding.searchBtn;
        searchBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        });

        ImageButton homeBtn = binding.homeBtn;
        homeBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
        });

        ImageButton btnUploadVideo = binding.uploadBtn;
        btnUploadVideo.setOnClickListener(v -> {
            if (loggedInUser == null) {
                Toast.makeText(MainActivity.this, "You need to be logged in to upload a video", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(MainActivity.this, UploadActivity.class);
            startActivity(i);
        });

        loggedInUser = MyApplication.getCurrentUser();
        youBtn = binding.youBtn;
        if (loggedInUser == null) {
            setLoggedOutState();
        } else {
            setLoggedInState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView lstVideos = binding.lstVideos;
        videoViewModel.getVideos().observe(this, videos -> videoAdapter.setVideos(videos));
        lstVideos.setAdapter(videoAdapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));
        LiveData<List<User>> users = userRepository.getAllUsers();
        users.observe(this, userList -> userAdapter.setUsers(userList));
    }

    private void setLoggedInState() {
        youBtn.setImageResource(R.drawable.baseline_account_circle_24);
        if (loggedInUser != null) {
            binding.youBtnText.setText("You");
            if (loggedInUser.getImageUrl() != null && !loggedInUser.getImageUrl().isEmpty()) {
                String imageUrl = "http://10.0.2.2:8080/" + loggedInUser.getImageUrl();
                Glide.with(this)
                        .load(imageUrl)
                        .transform(new CircleCrop())
                        .into(youBtn);
            }
        }
        youBtn.setOnClickListener(this::showUserOptionsMenu);
    }

    private void showUserOptionsMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.user_options_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            CharSequence title = item.getTitle();
            if (title != null) {
                if (title.equals("Log Out")) {
                    userRepository.logoutUser();
                    setLoggedOutState();
                    return true;
                } else if (title.equals("Edit User")) {
                    Intent editIntent = new Intent(MainActivity.this, EditUserActivity.class);
                    startActivity(editIntent);
                    return true;
                } else if (title.equals("Delete User")) {
                    if (loggedInUser != null) {
                        userRepository.delete(loggedInUser.getUsername(), loggedInUser.getToken());
                        userRepository.logoutUser();
                        setLoggedOutState();
                    }
                    return true;
                }
            }
            return false;
        });

        popupMenu.show();
    }

    private void setLoggedOutState() {
        binding.youBtnText.setText("Log In");
        userRepository.logoutUser();
        youBtn.setImageResource(R.drawable.baseline_account_circle_24);
        youBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(i);
        });
    }
}