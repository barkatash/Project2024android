package com.example.youtube.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private LiveData<List<User>> users;
    private UserRepository userRepository;
    private VideoRepository videoRepository;
    private MutableLiveData<User> loggedInUser;
    private MutableLiveData<User> fetchedUser;
    private LiveData<List<Video>> userVideos;

    public UserViewModel(Application application) {
        super(application);
        this.userRepository = UserRepository.getInstance(null);
        this.users = userRepository.getAllUsers();
        videoRepository = new VideoRepository(application);
        this.loggedInUser = userRepository.getLoggedInUser();
        this.fetchedUser = new MutableLiveData<>();
        this.userVideos = new MutableLiveData<>();
    }

    public LiveData<List<Video>> getUserVideos() {
        return userVideos;
    }
    public void fetchUserVideos(String username) {
        userVideos = videoRepository.getVideosByUser(username);
    }
    public LiveData<List<User>> getUsers() {
        return users;
    }

    public void setUsers(LiveData<List<User>> users) {
        this.users = users;
    }

    public MutableLiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(MutableLiveData<User> loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.userRepository.loginUser(loggedInUser);
    }

    public boolean isLogin() {
        return userRepository.isUserLoggedIn();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkUserCredentials(String username, String password) {
        userRepository.checkUserCredentials(username, password);
    }

    public void fetchUserByUsername(String username) {
        userRepository.fetchUserByUsername(username, fetchedUser);
    }
    public LiveData<User> getFetchedUser() {
        return fetchedUser;
    }
}