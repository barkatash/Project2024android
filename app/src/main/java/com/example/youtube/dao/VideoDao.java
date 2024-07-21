package com.example.youtube.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.youtube.entities.Video;

import java.util.List;

@Dao
public interface VideoDao {
    @Query("SELECT * FROM video")
    MutableLiveData<List<Video>> index(); // Return LiveData

    @Query("SELECT * FROM video WHERE id = :id")
    MutableLiveData<Video> get(int id); // Return LiveData

    @Insert
    void insert(Video... videos);

    @Update
    void update(Video... videos);

    @Delete
    void delete(Video... videos);
}
