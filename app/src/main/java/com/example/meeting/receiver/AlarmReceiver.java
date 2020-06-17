package com.example.meeting.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.meeting.App;
import com.example.meeting.L;
import com.example.meeting.R;
import com.example.meeting.utils.AlarmWakeLock;
import com.example.meeting.utils.Const;
import com.example.meeting.utils.DateUtils;

import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    App app = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;
        app = (App) context.getApplicationContext();

        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            L.i("::[ACTION_BOOT_COMPLETED]::");
        } else if (action.equals(Const.REQUEST_ACTION_ALARM)) {
            L.i("[REQUEST_ACTION_ALARM]");
            AlarmWakeLock.acquireWakeLock(context);
            final long triggerAtTime = intent.getLongExtra("triggerAtTime", -1);
            final String triggerName = intent.getStringExtra("triggerName");
            L.i("[triggerAtTime]" + triggerAtTime + " triggerName : " + triggerName);
            if (DateUtils.curTimeInMillis(System.currentTimeMillis()) <= triggerAtTime) {
                onShowNotification(context, triggerName);
            }
        }
    }

    private void onShowNotification(Context context, String content) {
        Random random = new Random();
        int id = random.nextInt(Integer.MAX_VALUE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "com.example.meeting")
                .setContentTitle("일정 알림")
                .setContentText(content)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round)) //BitMap 이미지 요구
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        L.e("id = " + id + ", content = " + content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.mipmap.ic_launcher_round); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName = context.getString(R.string.app_name);
            String description = "푸쉬 메세지";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("com.example.meeting", channelName, importance);
            channel.setDescription(description);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
        }

        notificationManager.notify(id, builder.build());
    }

}
