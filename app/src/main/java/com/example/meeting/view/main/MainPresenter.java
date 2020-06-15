package com.example.meeting.view.main;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.appcompat.view.menu.MenuView;
import androidx.core.app.ActivityCompat;

import com.example.meeting.AppContainer;
import com.example.meeting.L;
import com.example.meeting.data.AppDataManger;
import com.example.meeting.model.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.MainPresenter {

    private MainContract.View mView;
    private FirebaseAuth auth;
    private DatabaseReference calendarDatabaseReference;
    private CompositeDisposable compositeDisposable;
    private AppDataManger appDataManager;

    public MainPresenter(MainContract.View view,
                         CompositeDisposable compositeDisposable,
                         AppContainer appContainer) {
        this.mView = view;
        this.compositeDisposable = compositeDisposable;
        this.appDataManager = appContainer.appDataManger;
    }


    @Override
    public void init() {
        auth = FirebaseAuth.getInstance();
        calendarDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Calendars");

        if (auth.getCurrentUser() != null) {
            getUser(auth.getCurrentUser().getUid());
        }

    }

    @Override
    public void getUser(String uid) {
        compositeDisposable.add(appDataManager.getUser(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    L.i("[getUser] Success");
                    if (result != null) {
                        L.i(":::call getUser " + result.toString());
                        mView.setUserName(result.name);
                    }
                }, error -> {
                    L.e("[getUser] Fail " + error.getMessage());
                    mView.setUserName("");
                }));
    }

    @Override
    public void setTimeSetting(Calendar cal) {
        mView.showTimeDialog();
    }

    private void onError() {
        mView.showLoading(false);
        mView.showMessage("다시 시도해주세요.");
    }

}
