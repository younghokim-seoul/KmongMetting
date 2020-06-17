package com.example.meeting.data.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;


@Entity(tableName = "alarms")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    public String title;

    public String todo;

    @ColumnInfo(name = "push_at")
    public String pushAt;

    public Long pushTimeMils;

    public int getHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(pushTimeMils);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(pushTimeMils);
        return calendar.get(Calendar.MINUTE);
    }

    public int getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(pushTimeMils);
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(pushTimeMils);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(pushTimeMils);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    public int getSecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(pushTimeMils);
        return calendar.get(Calendar.SECOND);
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", pushAt='" + pushAt + '\'' +
                ", pushTimeMils=" + pushTimeMils +
                '}';
    }
}
