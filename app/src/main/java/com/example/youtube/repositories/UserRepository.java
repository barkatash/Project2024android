package com.example.youtube.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.api.UserAPI;
import com.example.youtube.dao.UserDao;
import com.example.youtube.entities.User;

import java.util.LinkedList;
import java.util.List;

public class UserRepository {
    private static final String SHARED_PREFS_NAME = "user_prefs";
    private static final String KEY_USER_TOKEN = "user_token";

    private UserDao dao;
    private UserListData userListData;
    private UserAPI api;
    private SharedPreferences sharedPreferences;

    public UserRepository(Context context) {
        userListData = new UserListData();
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String token = getUserToken();
        api = new UserAPI(userListData, dao, token);
        api.getAllUsers(userListData);
    }

    class UserListData extends MutableLiveData<List<User>> {
        public UserListData() {
            super();
            setValue(new LinkedList<User>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            // Load data from the database if needed
        }
    }

    public LiveData<List<User>> getAllUsers() {
        return userListData;
    }

    public LiveData<User> getUserById(String username) {
        return api.getUserByUsername(username);
    }

    public void resetUsers() {
        api.getAllUsers(userListData);
    }

    public void deleteUser(String username) {
        api.deleteUser(username);
        resetUsers();
    }

    public void addUser(User newUser) {
        api.addUser(newUser);
        resetUsers();
    }

    public void saveUserToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_TOKEN, token);
        editor.apply();
    }

    public String getUserToken() {
        return sharedPreferences.getString(KEY_USER_TOKEN, null);
    }
}
