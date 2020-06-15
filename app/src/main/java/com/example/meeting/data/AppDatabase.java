package com.example.meeting.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.meeting.data.dao.AccountDao;
import com.example.meeting.data.model.Account;

@Database(entities = {Account.class}, version = 1, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDao accountDao();
}
