package com.example.meeting.data;

import com.example.meeting.data.model.Account;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface DBHelper {
    Completable insertUser(Account account);
    Single<Account> getUser(String uid);
}
