package com.example.youtube;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView backArrow = binding.goBack;
        backArrow.setOnClickListener(v -> finish());

        ImageButton searchBtn = binding.searchBtn;
        searchBtn.setOnClickListener(v -> performSearch());
    }

    private void performSearch() {
        EditText searchEditText = binding.searchEditText;
        String query = searchEditText.getText().toString().trim();

        VideoRepository videoRepository = VideoRepository.getInstance();
        videoRepository.searchVideos(query);

        finish();
    }
}
