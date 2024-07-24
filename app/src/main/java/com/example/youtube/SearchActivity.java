package com.example.youtube;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.repositories.VideoRepository;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ImageButton searchBtn;
    private ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize views using findViewById
        searchEditText = findViewById(R.id.searchEditText);
        searchBtn = findViewById(R.id.searchBtn);
        backArrow = findViewById(R.id.goBack);

        backArrow.setOnClickListener(v -> finish());

        searchBtn.setOnClickListener(v -> performSearch());
    }

    private void performSearch() {
        EditText searchEditText = findViewById(R.id.searchEditText);
        String query = searchEditText.getText().toString().trim();

        VideoRepository videoRepository = VideoRepository.getInstance(getApplicationContext());
        videoRepository.searchVideos(query);
        finish();
    }

}
