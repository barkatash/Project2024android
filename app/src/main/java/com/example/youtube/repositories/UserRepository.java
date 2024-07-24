package com.example.youtube.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.AppDB;
import com.example.youtube.apiService.UserApiService;
import com.example.youtube.dao.UserDao;
import com.example.youtube.entities.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class UserRepository {
    private static UserRepository instance;
    private static UserDao userDao;
    private static UserApiService apiService;
    private LiveData<List<User>> allUsers;
    private User loggedInUser = null;

    private UserRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        userDao = db.userDao();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your-backend-url.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(UserApiService.class);

        allUsers = userDao.index();
    }

    public static synchronized UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context.getApplicationContext());
        }
        return instance;
    }

    public static LiveData<User> getUserById(int id) {
        return userDao.getUserById(id);
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public static void insert(User user) {
        new Thread(() -> userDao.insert(user)).start();
        apiService.addUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // Handle response
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void update(User user) {
        new Thread(() -> userDao.update(user)).start();
    }

    public void delete(User user) {
        new Thread(() -> userDao.delete(user)).start();
    }

    public void loginUser(String username, String password) {
        apiService.loginUser(new User(username, password)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loggedInUser = response.body();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void logoutUser() {
        loggedInUser = null;
    }
}
