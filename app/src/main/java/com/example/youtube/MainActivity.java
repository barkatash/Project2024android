package com.example.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.adapters.PostsListAdapter;
import com.example.youtube.databinding.ActivityMainBinding;
import com.example.youtube.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageButton searchBtn = binding.searchBtn;
        searchBtn.setOnClickListener(v -> {
            Intent i = new Intent(this, SearchActivity.class);
            startActivity(i);
        });

        RecyclerView lstPosts = binding.lstPosts;
        final PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));


        List<Post> posts = new ArrayList<>();
        posts.add(new Post("Osher Cohen Music", "אושר כהן - אהבה", "3:27", "28M", "11 months ago", R.drawable.osher));
        posts.add(new Post("Aaron Jack", "Learn JAVASCRIPT in just 5 MINUTES", "5:14", "1.5M", "4 years ago", R.drawable.js));
        posts.add(new Post("JustinBieber", "Justin Bieber - Baby", "3:39", "3B", "14 years ago", R.drawable.baby));
        posts.add(new Post("EyalGolan", "אייל גולן - עם ישראל חי", "3:34", "21M", "7 months ago", R.drawable.eyal));
        posts.add(new Post("Rihanna", "Rihanna - Diamonds", "4:42", "11M", "11 years ago", R.drawable.diamonds));
        posts.add(new Post("Ed Sheeran", "Ed Sheeran - Perfect (Official Music Video)", "4:39", "3.7B", "6 years ago", R.drawable.ed));
        posts.add(new Post("Eurovision Song Contest", "Eden Golan - Hurricane | Israel 🇮🇱 | Official Music Video | Eurovision 2024", "3:06", "7.9M", "2 months ago", R.drawable.hurricane));
        posts.add(new Post("BuzzFeedVideo", "Americans Try Israeli Snacks", "3:01", "5.8M", "9 years ago", R.drawable.images));
        posts.add(new Post("MaccabiHealthcare", "אדיר מילר מאלתר במוקד מכבי - מאושרת, מאושרת, מאושרת", "2:28", "1.9M", "8 years ago", R.drawable.adir));
        posts.add(new Post("NBA", "NBA Impossible Moments", "8:08", "2.7M", "9 months ago", R.drawable.nba));
        posts.add(new Post("NBATop10", "Stephen Curry Top 10 Plays of Career", "4:12", "15K", "1 year ago", R.drawable.maxresdefault));

        adapter.setPosts(posts);

    }
}