package com.example.youtube.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.api.UserAPI;
import com.example.youtube.dao.UserDao;
import com.example.youtube.AppDB;
import com.example.youtube.entities.User;

import java.util.LinkedList;
import java.util.List;

public class UserRepository {
    private static volatile UserRepository INSTANCE;
    private UserDao userDao;
    private UserListData userListData;
    private UserAPI apiService;
    private User loggedInUser = null;

    public UserRepository() {
        userListData = new UserListData();
        apiService = new UserAPI(userListData, userDao);
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

    public LiveData<User> getUserById(String userId) {
        return apiService.getUserById(userId);
    }

    public void resetUsers() {
        apiService.getAllUsers(userListData);
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void addUser(User newUser) {
        apiService.addUser(newUser);
    }

    public void delete(String userId) {
        apiService.deleteUser(userId);
        resetUsers();
    }

    public void loginUser(User user) {
        this.loggedInUser = user;
    }

    public void logoutUser() {
        loggedInUser = null;
    }

    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }

    public User checkUserCredentials(String username, String password) {
        List<User> users = userListData.getValue();
        if (users != null) {
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;
                }
            }
        }
        return null;
    }
}
