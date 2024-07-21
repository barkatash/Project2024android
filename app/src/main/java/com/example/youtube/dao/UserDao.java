package com.example.youtube.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.youtube.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    MutableLiveData<List<User>> index(); // Return LiveData

    @Query("SELECT * FROM user WHERE id = :id")
    MutableLiveData<User> get(int id); // Return LiveData

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    MutableLiveData<User> getByUsernameAndPassword(String username, String password); // Return LiveData

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);
}
