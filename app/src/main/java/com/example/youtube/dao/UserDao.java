package com.example.youtube.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.youtube.entities.User;

@Dao
public interface UserDao {
//    @Query("SELECT * FROM User WHERE username = :username")
//    User getUserByUsername(String username);

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);
}