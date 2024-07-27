package com.example.youtube.remoteRepositories;

import android.content.Context;
import com.example.youtube.R;
import com.example.youtube.apiService.UserApiService;
import com.example.youtube.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRemoteRepository {
    private final UserApiService apiService;

    public UserRemoteRepository(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(UserApiService.class);
    }

    public void getAllUsers(Callback<List<User>> callback) {
        Call<List<User>> call = apiService.getUsers();
        call.enqueue(callback);
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
        Call<User> call = apiService.loginUser(credentials);
        call.enqueue(callback);
    }
}
