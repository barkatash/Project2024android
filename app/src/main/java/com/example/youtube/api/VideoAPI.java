package com.example.youtube.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube.MyApplication;
import com.example.youtube.R;
import com.example.youtube.RecommendationResponse;
import com.example.youtube.UsernameRequest;
import com.example.youtube.dao.VideoDao;
import com.example.youtube.entities.Video;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoAPI {

    private MutableLiveData<List<Video>> videoListData;
    private MutableLiveData<List<Video>> recommenedVideoListData;
    private VideoDao dao;
    Retrofit retrofit;
    videoWebServiceAPI webServiceAPI;

    public VideoAPI(MutableLiveData<List<Video>> videoListData, MutableLiveData<List<Video>> recommenedVideoListData, VideoDao dao) {
        this.videoListData = videoListData;
        this.recommenedVideoListData = recommenedVideoListData;
        this.dao = dao;
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create()).build();
        webServiceAPI = retrofit.create(videoWebServiceAPI.class);
    }

    public void getAllVideos(MutableLiveData<List<Video>> videos) {
        Call<List<Video>> call = webServiceAPI.getVideos();
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                videos.postValue(response.body());
                new Thread(() -> {
                    dao.insert(response.body());
                }).start();
            }
            @Override
            public void onFailure (Call<List<Video>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    public void getRecommendedVideos(MutableLiveData<List<Video>> videos, String username) {
        UsernameRequest usernameRequest = new UsernameRequest(username);
        Call<RecommendationResponse> call = webServiceAPI.getRecommendedVideos(usernameRequest);
        call.enqueue(new Callback<RecommendationResponse>() {
            @Override
            public void onResponse(Call<RecommendationResponse> call, Response<RecommendationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videos.postValue(response.body().getRecommendations());
                } else {
                    Log.e("response error", "Response was not successful or body was null");
                }
            }

            @Override
            public void onFailure(Call<RecommendationResponse> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    public void addVideo(String token, Video video, File videoImageFile, File videoFile) {
        MultipartBody.Part imagePart = null;
        if (videoImageFile != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"), videoImageFile);
            imagePart = MultipartBody.Part.createFormData("image", videoImageFile.getName(), imageBody);
        }
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(),
                RequestBody.create(MediaType.parse("video/mp4"), videoFile));
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), video.getTitle());
        RequestBody uploaderBody = RequestBody.create(MediaType.parse("text/plain"), video.getUploader());
        RequestBody durationBody = RequestBody.create(MediaType.parse("text/plain"), video.getDuration());
        RequestBody visitsBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(video.getVisits()));
        Call<Void> call = webServiceAPI.addVideo("Bearer " + token, video.getUploader(), titleBody,
                uploaderBody,
                durationBody,
                visitsBody,
                videoPart, imagePart);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("VideoAPI", "Video added successfully.");
                    getAllVideos(videoListData);
                } else {
                    Log.e("VideoAPI", "Failed to add video: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("VideoAPI", "Error adding video: " + t.getMessage());
            }
        });
    }
    public void editVideo(String token, Video video, File videoImageFile, File videoFile) {
        MultipartBody.Part imagePart = null;
        MultipartBody.Part videoPart = null;
        if (videoImageFile.exists() && videoImageFile.length() > 0) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"), videoImageFile);
            imagePart = MultipartBody.Part.createFormData("image", videoImageFile.getName(), imageBody);
        }
        if (videoFile.exists() && videoFile.length() > 0) {
            videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(),
                    RequestBody.create(MediaType.parse("video/mp4"), videoFile));
        }
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), video.getTitle());
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), video.getDescription());
        Call<Void> call = webServiceAPI.editVideo("Bearer " + token, video.getUploader(), video.getId(),
                titleBody,
                descriptionBody,
                videoPart, imagePart);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("VideoAPI", "Video edited successfully.");
                    getAllVideos(videoListData);
                } else {
                    Log.e("VideoAPI", "Failed to edit video: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("VideoAPI", "Error editing video: " + t.getMessage());
            }
        });
    }
    public void deleteVideo(String token, String username, String videoId) {
        Call<Void> call = webServiceAPI.deleteVideo("Bearer " + token, username, videoId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("VideoAPI", "Video deleted successfully.");
                    getAllVideos(videoListData);
                } else {
                    Log.e("VideoAPI", "Failed to delete video: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("VideoAPI", "Error deleting video: " + t.getMessage());
            }
        });
    }

    public MutableLiveData<Video> getVideoById(String videoId) {
        MutableLiveData<Video> videoLiveData = new MutableLiveData<>();

        Call<Video> call = webServiceAPI.getVideoById(videoId);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videoLiveData.setValue(response.body());
                } else {
                    Log.e("VideoAPI", "Failed to fetch video: " + response.message());
                    videoLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.e("VideoAPI", "Error fetching video: " + t.getMessage());
                videoLiveData.setValue(null);
            }
        });

        return videoLiveData;
    }

    public void getVideosByUser(String username, MutableLiveData<List<Video>> videos) {
        Call<List<Video>> call = webServiceAPI.getVideosByUser(username);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videos.postValue(response.body());
                } else {
                    Log.e("VideoAPI", "Failed to fetch videos by user: " + response.message());
                    videos.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e("VideoAPI", "Error fetching videos by user: " + t.getMessage());
                videos.postValue(null);
            }
        });
    }

}