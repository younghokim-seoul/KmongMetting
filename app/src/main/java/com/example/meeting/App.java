package com.example.meeting;

import android.app.Application;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        L.initialize("young", true);
    }
}
