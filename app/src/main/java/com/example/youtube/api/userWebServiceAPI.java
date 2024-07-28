package com.example.youtube.api;

import com.example.youtube.UserLogin;
import com.example.youtube.entities.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface userWebServiceAPI {
    @GET("users")
    Call<List<User>> getUsers();

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") String id);

    @POST("users")
    Call<Void> addUser(@Body User user);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") String id);

    @POST("tokens/")
    Call<User> login(@Body UserLogin credentials);
}
