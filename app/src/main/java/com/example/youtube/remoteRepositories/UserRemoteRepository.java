package com.example.youtube.remoteRepositories;

import com.example.youtube.apiService.UserApiService;
import com.example.youtube.entities.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRemoteRepository {
    private UserApiService apiService;

    public UserRemoteRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your-backend-url.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(UserApiService.class);
    }

    public void getUserById(int id, Callback<User> callback) {
        Call<User> call = apiService.getUserById(id);
        call.enqueue(callback);
    }

    public void addUser(User user, Callback<User> callback) {
        Call<User> call = apiService.addUser(user);
        call.enqueue(callback);
    }

    public void deleteUser(int id, Callback<Void> callback) {
        Call<Void> call = apiService.deleteUser(id);
        call.enqueue(callback);
    }

    public void loginUser(User credentials, Callback<User> callback) {
        Call<User> call = apiService.loginUser(credentials.getUsername(), credentials.getPassword());
        call.enqueue(callback);
    }
}
