package com.example.youtube.api;

import com.example.youtube.UserLogin;
import com.example.youtube.entities.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface userWebServiceAPI {
    @GET("users")
    Call<List<User>> getUsers();

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") String id);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") String id);

    @POST("tokens/")
    Call<User> login(@Body UserLogin credentials);


    @Multipart
    @POST("users/")
    Call<String> createUser(
            @Part("username") RequestBody username,
            @Part("displayName") RequestBody displayName,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part image
    );
    @PUT("api/users/{id}")
    Call<Void> updateUser(
            @Path("id") String userId,
            @Body User user
    );
}