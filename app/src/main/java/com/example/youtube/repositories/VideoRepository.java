package com.example.youtube.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.AppDB;
import com.example.youtube.apiService.VideoApiService;
import com.example.youtube.dao.VideoDao;
import com.example.youtube.entities.Video;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoRepository {
    private static VideoRepository instance;
    private VideoDao videoDao;
    private VideoApiService apiService;
    private static LiveData<List<Video>> allVideos;
    private Map<Integer, Video> videoMap;

    private VideoRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        videoDao = db.videoDao();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your-backend-url.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(VideoApiService.class);

        allVideos = videoDao.index();
        videoMap = new HashMap<>();
        initializeVideoMap();
    }

    public static synchronized VideoRepository getInstance(Context context) {
        if (instance == null) {
            instance = new VideoRepository(context.getApplicationContext());
        }
        return instance;
    }

    public LiveData<List<Video>> getAllVideos() {
        return allVideos;
    }

    public Video getVideoById(int videoId) {
        return videoMap.get(videoId);
    }

    public void insert(Video video) {
        new Thread(() -> {
            videoDao.insert(video);
            videoMap.put(video.getVideoId(), video);
        }).start();
        apiService.addVideo(video).enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                // Handle response
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void update(Video video) {
        new Thread(() -> {
            videoDao.update(video);
            videoMap.put(video.getVideoId(), video);
        }).start();
        apiService.addVideo(video).enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                // Handle response
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void delete(Video video) {
        new Thread(() -> {
            videoDao.delete(video);
            videoMap.remove(video.getVideoId());
        }).start();
        apiService.deleteVideo(video.getVideoId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Handle response
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }
    public void searchVideos(String query) {
        apiService.searchVideos(query).enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    List<Video> videos = response.body();
                    // Update local database or LiveData with search results
                    if (videos != null) {
                        new Thread(() -> {
                            videoDao.insertAll(videos); // Assuming insertAll() is defined in VideoDao
                        }).start();
                        // You might also want to update videoMap or other relevant data structures
                    }
                } else {
                    Log.e("VideoRepository", "Failed to search videos: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e("VideoRepository", "Error searching videos", t);
            }
        });
    }
    public static int getNextVideoId() {
        int maxId = 0;
        List<Video> videos = allVideos.getValue();
        if (videos != null) {
            for (Video video : videos) {
                if (video.getVideoId() > maxId) {
                    maxId = video.getVideoId();
                }
            }
        }
        return maxId + 1;
    }


    private void initializeVideoMap() {
        allVideos.observeForever(videos -> {
            videoMap.clear();
            for (Video video : videos) {
                videoMap.put(video.getVideoId(), video);
            }
        });
    }
}
