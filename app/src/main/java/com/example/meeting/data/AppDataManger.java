package com.example.meeting.data;

import com.example.meeting.data.model.Account;

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
}
