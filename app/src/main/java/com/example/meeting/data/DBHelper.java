package com.example.meeting.data;

import com.example.meeting.data.model.Account;
import com.example.meeting.data.model.Alarm;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface DBHelper {
    Completable insertUser(Account account);

    Single<Account> getUser(String uid);

    Single<List<Alarm>> getAllAlarms();

    Completable insertAlarm(Alarm alarm);

    Completable deleteAlarm(Long id);
}
