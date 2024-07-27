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
    @Query("SELECT * FROM video")
    LiveData<List<Video>> index(); // Return LiveData

    @Query("SELECT * FROM video WHERE videoId = :videoId")
    LiveData<Video> get(int videoId); // Return LiveData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Video... videos);

    @Update
    void update(Video... videos);

    @Delete
    void delete(Video... videos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Video> videos);
}
