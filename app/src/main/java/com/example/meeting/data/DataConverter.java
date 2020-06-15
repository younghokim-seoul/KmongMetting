package com.example.meeting.data;

import androidx.room.TypeConverter;

import java.util.Date;

public class DataConverter {
    //데이터베이스에 있는 long 타입이있다면? date 클래스로 변환시켜주고 역으로 변한다
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
