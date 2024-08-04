package com.example.youtube.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.youtube.entities.Video;

import java.util.List;

@Dao
public interface VideoDao {
    @Query("SELECT * FROM Video")
    List<Video> index();

    @Query("SELECT * FROM Video WHERE id = :id")
    LiveData<Video> get(String id);

    @Insert
    void insert(List<Video> videos);

    @Update
    void update(Video... videos);

    @Delete
    void delete(Video... videos);

    @Query("DELETE FROM Video")
    void deleteAll(); // Deletes all videos
}

