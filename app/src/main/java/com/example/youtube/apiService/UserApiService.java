package com.example.youtube.apiService;

import com.example.youtube.entities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApiService {
    @GET("users/{id}")
    Call<User> getUserById(@Path("id") int id);

    @POST("users")
    Call<User> addUser(@Body User user);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") int id);

    @POST("users/login")
    Call<User> loginUser(@Body String username, String password);
}
