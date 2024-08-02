package com.example.youtube.dao;

import androidx.lifecycle.LiveData;
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
    LiveData<List<Comment>> index(); // Return LiveData

    @Query("SELECT * FROM comment WHERE id = :id")
    LiveData<Comment> get(String id); // Return LiveData

    @Query("SELECT * FROM comment WHERE videoId = :videoId")
    LiveData<List<Comment>> getCommentsForVideo(String videoId); // Return LiveData

    @Insert
    void insert(Comment... comments);

    @Update
    void update(Comment... comments);

    @Delete
    void delete(Comment... comments);
}
