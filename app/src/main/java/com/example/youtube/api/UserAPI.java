package com.example.youtube.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube.Like;
import com.example.youtube.MyApplication;
import com.example.youtube.R;
import com.example.youtube.UserLogin;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private MutableLiveData<List<User>> userListData;
    Retrofit retrofit;
    userWebServiceAPI webServiceAPI;

    public UserAPI(MutableLiveData<List<User>> userListData) {
        this.userListData = userListData;
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create()).build();
        webServiceAPI = retrofit.create(userWebServiceAPI.class);
    }
    public void login(String username, String password, MutableLiveData<User> userLiveData) {
        UserLogin credentials = new UserLogin(username, password);
        Call<User> call = webServiceAPI.login(credentials);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.setValue(response.body());
                    MyApplication.setCurrentUser(response.body());
                } else {
                    Log.e("UserAPI", "Failed to login: " + response.message());
                    userLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserAPI", "Error logging in: " + t.getMessage());
                userLiveData.setValue(null);
            }
        });
    }
    public void getAllUsers(MutableLiveData<List<User>> users) {
        Call<List<User>> call = webServiceAPI.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                users.postValue(response.body());
            }
            @Override
            public void onFailure (Call<List<User>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }
    public void addUser(User user, File profileImageFile) {
        RequestBody usernameBody = RequestBody.create(MultipartBody.FORM, user.getUsername());
        RequestBody displayNameBody = RequestBody.create(MultipartBody.FORM, user.getDisplayName());
        RequestBody passwordBody = RequestBody.create(MultipartBody.FORM, user.getPassword());

        MultipartBody.Part imagePart = null;
        if (profileImageFile != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"), profileImageFile);
            imagePart = MultipartBody.Part.createFormData("image", profileImageFile.getName(), imageBody);
        }
        Call<String> call = webServiceAPI.createUser(usernameBody, displayNameBody, passwordBody, imagePart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("UserAPI", "User added successfully.");
                    // Optionally, refresh the user list
                    getAllUsers(userListData);
                } else {
                    Log.e("UserAPI", "Failed to add user: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("UserAPI", "Error adding user: " + t.getMessage());
            }
        });
    }

    public void deleteUser(String id, String token) {
        Call<Void> call = webServiceAPI.deleteUser("Bearer " + token, id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("UserAPI", "user deleted successfully.");
                    getAllUsers(userListData);
                } else {
                    Log.e("UserAPI", "Failed to delete user: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserAPI", "Error deleting user: " + t.getMessage());
            }
        });
    }
    public void editUser(User user, File profileImageFile) {
        RequestBody displayNameBody = RequestBody.create(MultipartBody.FORM, user.getDisplayName());
        RequestBody passwordBody = RequestBody.create(MultipartBody.FORM, user.getPassword());

        MultipartBody.Part imagePart = null;
        if (profileImageFile != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"), profileImageFile);
            imagePart = MultipartBody.Part.createFormData("image", profileImageFile.getName(), imageBody);
        }

        Call<Void> call = webServiceAPI.updateUser("Bearer " + user.getToken(), user.getUsername(), displayNameBody, passwordBody, imagePart);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("UserAPI", "User updated successfully.");
                    Log.d("UserAPI", "User updated successfully.");
                    getAllUsers(userListData);
                } else {
                    Log.e("UserAPI", "Failed to update user: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserAPI", "Error updating user: " + t.getMessage());
            }
        });
    }
    public void editUserLike(User user) {
        Gson gson = new Gson();
        String userJsonString = gson.toJson(user);
        RequestBody userJson = RequestBody.create(MediaType.parse("application/json"), userJsonString);
        Call<Void> call = webServiceAPI.updateUserLike("Bearer " + user.getToken(), user.getUsername(), null, userJson);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("UserAPI", "User updated successfully.");
                    Log.d("UserAPI", "User updated successfully.");
                    getAllUsers(userListData);
                } else {
                    Log.e("UserAPI", "Failed to update user: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserAPI", "Error updating user: " + t.getMessage());
            }
        });
    }
    public MutableLiveData<User> getUserById(String userId) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();

        Call<User> call = webServiceAPI.getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.setValue(response.body());
                } else {
                    Log.e("UserAPI", "Failed to fetch user: " + response.message());
                    userLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserAPI", "Error fetching user: " + t.getMessage());
                userLiveData.setValue(null);
            }
        });

        return userLiveData;
    }
    public void getUserByUsername(String username, MutableLiveData<User> userLiveData) {
        Call<User> call = webServiceAPI.getUserById(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.setValue(response.body());
                } else {
                    Log.e("UserAPI", "Failed to fetch user: " + response.message());
                    userLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserAPI", "Error fetching user: " + t.getMessage());
                userLiveData.setValue(null);
            }
        });
    }
    public void updateUserLikeVideo(String token, String userId, String videoId, int newLikes) {
        Like updatedLikes = new Like(newLikes);
        Call<Video> call = webServiceAPI.updateUserLikeVideo("Bearer " + token, userId, videoId, updatedLikes);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful()) {
                    Log.d("UserAPI", "Video like status updated successfully.");
                } else {
                    Log.e("UserAPI", "Failed to update video like status: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.e("UserAPI", "Error updating video like status: " + t.getMessage());
            }
        });
    }
}