package com.example.youtube.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.User;
import com.example.youtube.repositories.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<List<User>> usersLiveData;
    private MutableLiveData<User> loggedInUser;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
        usersLiveData = userRepository.getAllUsers();
        loggedInUser = userRepository.getLoggedInUser();
    }

    public LiveData<List<User>> getAllUsers() {
        return usersLiveData;
    }

    public LiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    public void loginUser(String username, String password) {
        userRepository.loginUser(username, password);
    }

    public void logoutUser() {
        userRepository.logoutUser();
    }

    public void addUser(User user) {
        userRepository.insert(user);
    }
}
