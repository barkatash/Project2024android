package com.example.youtube;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Converters {
    private static final Gson gson = new Gson();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    // Converters for List<Integer>
    @TypeConverter
    public static String fromIntegerList(List<Integer> videoIds) {
        if (videoIds == null) {
            return null;
        }
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.toJson(videoIds, type);
    }

    @TypeConverter
    public static List<Integer> toIntegerList(String videoIdsString) {
        if (videoIdsString == null) {
            return null;
        }
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(videoIdsString, type);
    }

    // Converters for Date
    @TypeConverter
    public static String fromDate(Date date) {
        return date == null ? null : dateFormat.format(date);
    }

    @TypeConverter
    public static Date toDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Converters for Uri
    @TypeConverter
    public static Uri fromString(String value) {
        return value == null ? null : Uri.parse(value);
    }

    @TypeConverter
    public static String uriToString(Uri uri) {
        return uri == null ? null : uri.toString();
    }

    // Converters for Bitmap
    @TypeConverter
    public static Bitmap fromByteArray(byte[] bytes) {
        return bytes == null ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @TypeConverter
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
}
