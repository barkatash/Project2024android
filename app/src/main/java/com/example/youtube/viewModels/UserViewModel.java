package com.example.youtube.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.youtube.entities.User;
import com.example.youtube.repositories.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {

    private LiveData<List<User>> users;
    private UserRepository userRepository;
    private MutableLiveData<User> loggedInUser;

    public UserViewModel() {
        this.userRepository = UserRepository.getInstance(null); // Pass context if needed
        this.users = this.userRepository.getAllUsers();
        this.loggedInUser = this.userRepository.getLoggedInUser();
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
}
