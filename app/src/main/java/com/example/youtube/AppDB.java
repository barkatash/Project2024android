package com.example.youtube;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.youtube.dao.CommentDao;
import com.example.youtube.dao.UserDao;
import com.example.youtube.dao.VideoDao;
import com.example.youtube.entities.Comment;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;

@Database(entities = {User.class, Comment.class, Video.class}, version = 5)
@TypeConverters(Converters.class) // Custom type converters
public abstract class AppDB extends RoomDatabase {
    private static AppDB instance;

    public abstract UserDao userDao();
    public abstract CommentDao commentDao();
    public abstract VideoDao videoDao();

    public static synchronized AppDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
