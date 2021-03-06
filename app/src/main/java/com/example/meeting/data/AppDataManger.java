package com.example.meeting.data;

import com.example.meeting.data.model.Account;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.data.model.Calendar;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AppDataManger implements DBHelper {
    private final AppDatabase mAppDatabase;
    private static AppDataManger sInstance;

    public static AppDataManger getInstance(AppDatabase appDatabase) {
        if (sInstance == null) {
            sInstance = new AppDataManger(appDatabase);
        }
        return sInstance;
    }

    private AppDataManger(AppDatabase appDatabase) {
        this.mAppDatabase = appDatabase;
    }

    @Override
    public Completable insertUser(Account account) {
        return mAppDatabase.accountDao().insert(account);
    }

    @Override
    public Single<Account> getUser(String uid) {
        return Single.fromCallable(() -> mAppDatabase.accountDao().loadUser(uid));
    }

    @Override
    public Single<List<Alarm>> getAllAlarms() {
        return Single.fromCallable(() -> mAppDatabase.alarmDao().loadAll());
    }

    @Override
    public Completable insertAlarm(Alarm alarm) {
        return mAppDatabase.alarmDao().insert(alarm);
    }

    @Override
    public Completable deleteAlarm() {
        return mAppDatabase.alarmDao().delete();
    }

    @Override
    public Single<List<Calendar>> getMonthSchedulers(String uid, String rootKey) {
        return Single.fromCallable(() -> mAppDatabase.calendarDao().loadMonthSchedulers(uid, rootKey));
    }

    @Override
    public Completable insertScheduler(Calendar calendar) {
        return mAppDatabase.calendarDao().insert(calendar);
    }

    @Override
    public Completable deleteScheduler() {
        return mAppDatabase.calendarDao().delete();
    }
}
