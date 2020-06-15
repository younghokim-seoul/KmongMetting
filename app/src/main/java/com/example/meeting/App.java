package com.example.meeting;

import android.app.Application;


public class App extends Application {
    private AppContainer appContainer;
    @Override
    public void onCreate() {
        super.onCreate();
        L.initialize("young", true);
        appContainer = new AppContainer(getApplicationContext());
    }

    public AppContainer getAppContainer() {
        return appContainer;
    }
}
