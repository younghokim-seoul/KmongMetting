package com.example.meeting.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceHelper {
    private static final String PREF_NAME_APP_SETTINGS = "perf_name_xxx";
    private Context context;
    private SharedPreferences mPref;


    public PreferenceHelper(Context context) {
        this.context = context;
        this.mPref = context.getSharedPreferences(PREF_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public synchronized void setToken(String token) {
        String token_key = "user.token";
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(token_key, token);
        editor.apply();
    }

    public synchronized String getToken() {
        String token_key = "user.token";
        return mPref.getString(token_key, "");
    }

}

