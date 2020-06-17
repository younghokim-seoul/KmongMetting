package com.example.meeting.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.meeting.data.dao.AccountDao;
import com.example.meeting.data.dao.AlarmDao;
import com.example.meeting.data.dao.CalendarDao;
import com.example.meeting.data.model.Account;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.data.model.Calendar;


@Database(entities = {Account.class, Alarm.class, Calendar.class}, version = 1, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDao accountDao();

    public abstract AlarmDao alarmDao();

    public abstract CalendarDao calendarDao();
}
