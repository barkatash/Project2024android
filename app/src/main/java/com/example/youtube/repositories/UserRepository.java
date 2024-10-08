package com.example.youtube.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.api.UserAPI;
import com.example.youtube.entities.User;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class UserRepository {
    private static volatile UserRepository INSTANCE;
    private UserListData userListData;
    private UserAPI apiService;
    private MutableLiveData<User> loggedInUser = new MutableLiveData<>();

    public UserRepository() {
        userListData = new UserListData();
        apiService = new UserAPI(userListData);
        apiService.getAllUsers(userListData);
    }

    public static UserRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (UserRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRepository();
                }
            }
        }
        return INSTANCE;
    }

    class UserListData extends MutableLiveData<List<User>> {
        public UserListData() {
            super();
            setValue(new LinkedList<User>());
        }

        @Override
        protected void onActive() {
            super.onActive();
        }
    }

    public LiveData<List<User>> getAllUsers() {
        return userListData;
    }

    public User getUserByUsername(String username) {
        List<User> userList = userListData.getValue();
        if (userList != null) {
            for (User user : userList) {
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }

    public void resetUsers() {
        apiService.getAllUsers(userListData);
    }

    public MutableLiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    public void addUser(User newUser, File profileImageFile) {
        apiService.addUser(newUser, profileImageFile);
    }
    public void editUser(User newUser, File profileImageFile) {
        apiService.editUser(newUser, profileImageFile);
    }
    public void delete(String userId, String token) {
        apiService.deleteUser(userId, token);
        resetUsers();
    }
    public void editLikes(String token, String username, String videoId, int newLikes) {
        apiService.updateUserLikeVideo(token, username, videoId, newLikes);
    }
    public void editUserLikes(User user) {
        apiService.editUserLike(user);
    }

    public void loginUser(MutableLiveData<User> user) {
        this.loggedInUser = user;
    }

    public void logoutUser() {
        loggedInUser = new MutableLiveData<>();
    }

    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }

    public void checkUserCredentials(String username, String password) {
        apiService.login(username, password, this.loggedInUser);
    }
    public void fetchUserByUsername(String username, MutableLiveData<User> userLiveData) {
        apiService.getUserByUsername(username, userLiveData);
    }
}