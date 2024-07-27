package com.example.youtube.dao;

import androidx.lifecycle.LiveData;
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
    LiveData<List<User>> index(); // Return LiveData

    @Query("SELECT * FROM user WHERE id = :id")
    LiveData<User> get(int id); // Return LiveData

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    LiveData<User> getByUsernameAndPassword(String username, String password); // Return LiveData

    @Query("SELECT * FROM user WHERE id = :id")
    LiveData<User> getUserById(int id); // Return LiveData

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);
}
