package com.example.meeting.data;

import com.example.meeting.data.model.Account;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.data.model.Calendar;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface DBHelper {

    //유저 회원
    Completable insertUser(Account account);

    Single<Account> getUser(String uid);


    //알람
    Single<List<Alarm>> getAllAlarms();

    Completable insertAlarm(Alarm alarm);

    Completable deleteAlarm();


    //스케쥴

    Single<List<Calendar>> getMonthSchedulers(String uid, String rootKey);

    Completable insertScheduler(Calendar calendar);

    Completable deleteScheduler();


}
