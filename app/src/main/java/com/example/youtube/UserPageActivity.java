package com.example.youtube;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.youtube.adapters.UserVideoAdapter;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.viewModels.UserViewModel;

import java.util.List;

public class UserPageActivity extends AppCompatActivity {
    private ImageView imageViewProfile;
    private TextView textViewUsername;
    private UserViewModel userViewModel;
    private RecyclerView recyclerViewVideos;
    private UserVideoAdapter videoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        imageViewProfile = findViewById(R.id.imageViewProfile);
        textViewUsername = findViewById(R.id.textViewUsername);
        recyclerViewVideos = findViewById(R.id.recyclerViewUserVideos);
        ImageButton btnGoBack = findViewById(R.id.btnGoBackUserPage);
        btnGoBack.setOnClickListener(v -> onBackPressed());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(this));
        videoAdapter = new UserVideoAdapter(this,null);
        recyclerViewVideos.setAdapter(videoAdapter);

        String username = getIntent().getStringExtra("username");
        if (username != null) {
            userViewModel.fetchUserByUsername(username);
        }
        userViewModel.getFetchedUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    displayUserInfo(user);
                }
            }
        });
        userViewModel.fetchUserVideos(username);
        userViewModel.getUserVideos().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                videoAdapter.updateVideoList(videos);
            }
        });

    }

    private void displayUserInfo(User user) {
        textViewUsername.setText(user.getDisplayName());

         if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
             String imageUrl = "http://10.0.2.2:8080/" + user.getImageUrl();
             Glide.with(this)
                     .load(imageUrl)
                     .transform(new CircleCrop())
                     .into(imageViewProfile);
         }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
