package com.example.meeting;

import android.content.Context;

import androidx.room.Room;

import com.example.meeting.data.AppDataManger;
import com.example.meeting.data.AppDatabase;


public class AppContainer {
    public AppDataManger appDataManger;
    private volatile static AppDatabase appDatabase = null;

    AppContainer(Context context) {
        appDataManger = AppDataManger.getInstance(getInstance(context));
    }

    private static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                appDatabase = Room.databaseBuilder(
                        context,
                        AppDatabase.class,
                        "sample.db")
                        .build();
            }
        }
        return appDatabase;
    }
}
