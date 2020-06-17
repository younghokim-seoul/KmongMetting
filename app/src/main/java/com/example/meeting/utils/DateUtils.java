package com.example.meeting.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {
    public static String getConvertTime(long timeMils) {
        SimpleDateFormat alarmdate = new SimpleDateFormat("yyyy년MM월dd일");
        return alarmdate.format(timeMils);
    }

    public static String getConvertCurTime(long timeMils) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMils);
        SimpleDateFormat alarmdate = new SimpleDateFormat("yyyy년MM월dd일 HH시mm분");
        return alarmdate.format(timeMils);
    }

    public static long curTimeInMillis(long time) {
        long timeInMillis = 0L;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        timeInMillis = calendar.getTimeInMillis();

        return timeInMillis;
    }

    public static long getDateMils(Calendar cal) {
        Calendar calendar;
        if (cal == null) {
            calendar = Calendar.getInstance();
        } else {
            calendar = cal;
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String convert24To12System(int hour, int minute) {
        String time = "";
        String am_pm = "";
        if (hour < 12) {
            if (hour == 0) hour = 12;
            am_pm = "오전";
        } else {
            if (hour != 12)
                hour -= 12;
            am_pm = "오후";
        }
        String h = hour + "", m = minute + "";
        if (h.length() == 1) h = "0" + h;
        if (m.length() == 1) m = "0" + m;
        time = am_pm + " " + h + ":" + m;
        return time;
    }
}
