package com.example.youtube.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.youtube.entities.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comment")
    MutableLiveData<List<Comment>> index(); // Return LiveData

    @Query("SELECT * FROM comment WHERE id = :id")
    MutableLiveData<Comment> get(int id); // Return LiveData

    @Query("SELECT * FROM comment WHERE video = :video")
    MutableLiveData<List<Comment>> getCommentsForVideo(int video); // Return LiveData

    @Insert
    void insert(Comment... comments);

    @Update
    void update(Comment... comments);

    @Delete
    void delete(Comment... comments);
}