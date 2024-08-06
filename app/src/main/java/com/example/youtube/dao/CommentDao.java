package com.example.youtube.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.youtube.entities.Comment;
import com.example.youtube.entities.Video;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM Comment")
    List<Comment> index();

    @Query("SELECT * FROM Comment WHERE id = :id")
    LiveData<Comment> get(String id);

    @Query("SELECT * FROM Comment WHERE videoId = :videoId")
    LiveData<List<Comment>> getCommentsForVideo(String videoId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Comment> comments);
    @Insert
    void insert(Comment... comments);

    @Update
    void update(Comment... comments);

    @Delete
    void delete(Comment... comments);
}
