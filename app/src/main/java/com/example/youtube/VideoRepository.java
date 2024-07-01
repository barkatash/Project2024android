package com.example.youtube;

import android.content.Context;

import com.example.youtube.entities.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class VideoRepository {
    private static VideoRepository instance;
    private static List<Video> originalVideos;
    private List<Video> filteredVideos;

    private VideoRepository(Context context) {
        originalVideos = loadVideosFromJson(context);
        filteredVideos = new ArrayList<>(originalVideos);
    }

    public static synchronized VideoRepository getInstance(Context context) {
        if (instance == null) {
            instance = new VideoRepository(context);
        }
        return instance;
    }

    private List<Video> loadVideosFromJson(Context context) {
        List<Video> videos = new ArrayList<>();

        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.videos);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder json = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json.append(line);
            }

            JSONObject jsonObject = new JSONObject(json.toString());
            JSONArray jsonVideos = jsonObject.getJSONArray("videos");

            for (int i = 0; i < jsonVideos.length(); i++) {
                JSONObject jsonVideo = jsonVideos.getJSONObject(i);
                String author = jsonVideo.getString("author");
                String content = jsonVideo.getString("content");
                String duration = jsonVideo.getString("duration");
                String views = jsonVideo.getString("views");
                String uploadDate = jsonVideo.getString("uploadDate");
                String pic = jsonVideo.getString("pic");
                String videoName = jsonVideo.getString("video");
                int picResourceId = context.getResources().getIdentifier(pic, "drawable", context.getPackageName());
                int videoResourceId = context.getResources().getIdentifier(videoName, "raw", context.getPackageName());
                int id = i + 1;
                Video video = new Video(author, content, duration, views, uploadDate, picResourceId, videoResourceId);
                video.setId(id);
                videos.add(video);
            }
            bufferedReader.close();
            inputStream.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return videos;
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

    public static Video getVideoById(int videoId) {
        for (Video video : originalVideos) {
            if (video.getId() == videoId) {
                return video;
            }
        }
        return null;
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
    public void addVideo(Video newVideo) {
        originalVideos.add(0, newVideo);
        resetVideos();
    }

    public static int getNextVideoId() {
        return originalVideos.size() + 1;
    }
}
