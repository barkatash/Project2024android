package com.example.youtube;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.youtube.dao.VideoDao;
import com.example.youtube.entities.Video;

@Database(entities = {Video.class}, version = 2)
public abstract class AppDB extends RoomDatabase {
    public abstract VideoDao videoDao();
}