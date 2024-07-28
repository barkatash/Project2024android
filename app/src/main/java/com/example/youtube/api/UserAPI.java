package com.example.youtube.api;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube.MyApplication;
import com.example.youtube.R;
import com.example.youtube.dao.UserDao;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private MutableLiveData<List<User>> userListData;
    private UserDao dao;
    Retrofit retrofit;
    userWebServiceAPI webServiceAPI;

    public UserAPI(MutableLiveData<List<User>> userListData, UserDao dao) {
        this.userListData = userListData;
        this.dao = dao;
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create()).build();
        webServiceAPI = retrofit.create(userWebServiceAPI.class);
    }

    public void getAllUsers(MutableLiveData<List<User>> users) {
        Call<List<User>> call = webServiceAPI.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                users.postValue(response.body());
            }
            @Override
            public void onFailure (Call<List<User>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }
    public void addUser(User user) {
        Call<Void> call = webServiceAPI.addUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("UserAPI", "user added successfully.");
                    // Optionally, refresh the video list
                    getAllUsers(userListData);
                } else {
                    Log.e("UserAPI", "Failed to add user: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserAPI", "Error adding user: " + t.getMessage());
            }
        });
    }
    public void deleteUser(String id) {
        Call<Void> call = webServiceAPI.deleteUser(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("UserAPI", "user deleted successfully.");
                    // Optionally, refresh the video list
                    getAllUsers(userListData);
                } else {
                    Log.e("UserAPI", "Failed to delete user: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserAPI", "Error deleting user: " + t.getMessage());
            }
        });
    }

    public MutableLiveData<User> getUserById(String userId) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();

        Call<User> call = webServiceAPI.getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.setValue(response.body());
                } else {
                    Log.e("UserAPI", "Failed to fetch user: " + response.message());
                    userLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserAPI", "Error fetching user: " + t.getMessage());
                userLiveData.setValue(null);
            }
        });

        return userLiveData;
    }
}
