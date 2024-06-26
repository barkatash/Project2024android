package com.example.youtube;

import com.example.youtube.entities.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoRepository {
    private static VideoRepository instance;
    private static List<Video> originalVideos;
    private List<Video> filteredVideos;

    private VideoRepository() {
        originalVideos = new ArrayList<>();
        filteredVideos = new ArrayList<>();

        int idCounter = 0;
        originalVideos.add(new Video("Osher Cohen Music", "אושר כהן - אהבה", "3:27", "28M", "11 months ago", R.drawable.osher, R.raw.video1));
        originalVideos.add(new Video("Aaron Jack", "Learn JAVASCRIPT in just 5 MINUTES", "5:14", "1.5M", "4 years ago", R.drawable.js, R.raw.video2));
        originalVideos.add(new Video("JustinBieber", "Justin Bieber - Baby", "3:39", "3B", "14 years ago", R.drawable.baby, R.raw.video3));
        originalVideos.add(new Video("EyalGolan", "אייל גולן - עם ישראל חי", "3:34", "21M", "7 months ago", R.drawable.eyal, R.raw.video4));
        originalVideos.add(new Video("Rihanna", "Rihanna - Diamonds", "4:42", "11M", "11 years ago", R.drawable.diamonds, R.raw.video5));
        originalVideos.add(new Video("Ed Sheeran", "Ed Sheeran - Perfect (Official Music Video)", "4:39", "3.7B", "6 years ago", R.drawable.ed, R.raw.video6));
        originalVideos.add(new Video("Eurovision Song Contest", "Eden Golan - Hurricane | Israel 🇮🇱 | Official Music Video | Eurovision 2024", "3:06", "7.9M", "2 months ago", R.drawable.hurricane, R.raw.video1));
        originalVideos.add(new Video("BuzzFeedVideo", "Americans Try Israeli Snacks", "3:01", "5.8M", "9 years ago", R.drawable.images, R.raw.video2));
        originalVideos.add(new Video("MaccabiHealthcare", "אדיר מילר מאלתר במוקד מכבי - מאושרת, מאושרת, מאושרת", "2:28", "1.9M", "8 years ago", R.drawable.adir, R.raw.video3));
        originalVideos.add(new Video("NBA", "NBA Impossible Moments", "8:08", "2.7M", "9 months ago", R.drawable.nba, R.raw.video4));
        originalVideos.add(new Video("NBATop10", "Stephen Curry Top 10 Plays of Career", "4:12", "15K", "1 year ago", R.drawable.maxresdefault, R.raw.video5));

        for (Video video : originalVideos) {
            video.setId(idCounter++);
        }
        filteredVideos.addAll(originalVideos);
    }

    public static synchronized VideoRepository getInstance() {
        if (instance == null) {
            instance = new VideoRepository();
        }
        return instance;
    }

    public static Video getVideoById(int videoId) {
        for (Video video : originalVideos) {
            if (video.getId() == videoId) {
                return video;
            }
        }
        return null;
    }

    public List<Video> getVideos() {
        return new ArrayList<>(filteredVideos);
    }

    public void searchVideos(String query) {
        filteredVideos.clear();
        if (query.isEmpty()) {
            filteredVideos.addAll(originalVideos);
        } else {
            for (Video video : originalVideos) {
                if (video.getContent().toLowerCase().contains(query.toLowerCase())) {
                    filteredVideos.add(video);
                }
            }
        }
    }

    public void resetVideos() {
        filteredVideos.clear();
        filteredVideos.addAll(originalVideos);
    }
    public void updateVideo(Video updatedVideo) {
        for (int i = 0; i < originalVideos.size(); i++) {
            if (originalVideos.get(i).getId() == updatedVideo.getId()) {
                originalVideos.set(i, updatedVideo);
                break;
            }
        }
        for (int i = 0; i < filteredVideos.size(); i++) {
            if (filteredVideos.get(i).getId() == updatedVideo.getId()) {
                filteredVideos.set(i, updatedVideo);
                break;
            }
        }
        resetVideos();
    }

    public void deleteVideo(int videoId) {
        for (Video video : originalVideos) {
            if (video.getId() == videoId) {
                originalVideos.remove(video);
                break;
            }
        }
        resetVideos();
    }
}
