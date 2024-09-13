package com.example.youtube;

import android.app.Application;
import android.content.Context;

import com.example.youtube.entities.User;

public class MyApplication extends Application {
    public static Context context;
    private static User currentUser;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        MyApplication.currentUser = user;
    }

}

