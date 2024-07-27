package com.example.youtube.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.User;
import com.example.youtube.remoteRepositories.UserRemoteRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {
    private final UserRemoteRepository userRepository;
    private final MutableLiveData<List<User>> usersLiveData;
    private final MutableLiveData<User> loggedInUser;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRemoteRepository(application.getApplicationContext());
        usersLiveData = new MutableLiveData<>();
        loggedInUser = new MutableLiveData<>();
        loadUsers();
    }

    private void loadUsers() {
        userRepository.getAllUsers(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    usersLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public LiveData<List<User>> getAllUsers() {
        return usersLiveData;
    }

    public LiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    public void loginUser(String username, String password) {
        User credentials = new User(username, password);
        userRepository.loginUser(credentials, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    loggedInUser.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void logoutUser() {
        loggedInUser.setValue(null); // Clear the logged-in user
    }

    public void addUser(User user) {
        userRepository.addUser(user, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    loadUsers(); // Reload users after adding a new one
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void deleteUser(int userId) {
        userRepository.deleteUser(userId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadUsers(); // Reload users after deleting one
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
