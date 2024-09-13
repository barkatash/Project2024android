package com.example.youtube;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.youtube.dao.CommentDao;
import com.example.youtube.dao.VideoDao;
import com.example.youtube.entities.Comment;
import com.example.youtube.entities.Video;

@Database(entities = {Video.class, Comment.class}, version = 3)
public abstract class AppDB extends RoomDatabase {
    public abstract VideoDao videoDao();
    public abstract CommentDao commentDao();

}