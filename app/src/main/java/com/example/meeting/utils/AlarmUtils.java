package com.example.meeting.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import com.example.meeting.L;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.receiver.AlarmReceiver;

import java.util.Calendar;


public class AlarmUtils {
    public static void addAlarm(Context context, Alarm alarm) {

        L.e(alarm.getYear() + ", " + alarm.getMonth() + ", " + alarm.getDay() + ", " + alarm.getHour() + ", " + alarm.getMinute());

        long triggerAtTime = alarm.pushTimeMils;
        L.i("::::: triggerAtTime " + DateUtils.getConvertCurTime(triggerAtTime));
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Const.REQUEST_ACTION_ALARM);
        int id = getUniqueID(alarm);
        intent.putExtra("triggerAtTime", triggerAtTime);
        intent.putExtra("triggerName", alarm.title);
        L.e("::::Long.valueOf(id).hashCode() = " + Long.valueOf(id).hashCode());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar Addcalendar = Calendar.getInstance();
        Addcalendar.setTimeInMillis(triggerAtTime);
        Addcalendar.set(Calendar.SECOND, 0);

        L.e("추가 되야될 시간 : " + DateUtils.getConvertCurTime(Addcalendar.getTimeInMillis()));
        setAlarm(context, pendingIntent, Addcalendar);

    }

    private static void setAlarm(Context context, PendingIntent pendingIntent, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        L.e("등록될 시간 : " + DateUtils.getConvertTime(calendar.getTimeInMillis()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            L.i("Build.VERSION.SDK_INT >= Build.VERSION_CODES.M");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            L.i("Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            L.i("Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT");
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public static void cancelAlarm(Context context, Alarm alarm) {
        L.e("::::hour = " + alarm.getHour() + ", minute = " + alarm.getMinute());
        long triggerAtTime = alarm.pushTimeMils;
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //        Intent intent = new Intent(context, PMCService.class);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Const.REQUEST_ACTION_ALARM);
        int id = getUniqueID(alarm);

        L.e("::::Long.valueOf(id).hashCode() = " + Long.valueOf(id).hashCode());
        intent.putExtra("triggerAtTime", triggerAtTime);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        L.e("::::::sender = " + sender);
        if (sender != null) {
            am.cancel(sender);
            sender.cancel();
        }
    }

    private static int getUniqueID(Alarm alarm) {
        int id;
        int month = alarm.getMonth();
        int day = alarm.getDay();
        int hour = alarm.getHour();
        int min = alarm.getMinute();
        int second = alarm.getSecond();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", month)).append(String.format("%02d", day)).append(String.format("%02d", hour))
                .append(String.format("%02d", min)).append(String.format("%02d", second));
        id = Integer.parseInt(sb.toString());
        L.e("::::id = " + id);
        return id;
    }
}
