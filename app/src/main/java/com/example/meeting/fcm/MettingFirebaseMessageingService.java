package com.example.meeting.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.meeting.L;
import com.example.meeting.R;
import com.example.meeting.model.PushMessage;
import com.example.meeting.utils.PreferenceHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

public class MettingFirebaseMessageingService extends FirebaseMessagingService {
    private final static String NOTIFICATION_CHANNEL_ID = " com.example.meeting_fcm";
    PreferenceHelper prefHelper;
    String receivedMsg;
    int speechType;
    private Random random = new Random();

    //앱 최초 실행시 토큰을 preference에 저장
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        L.e("token = " + token);
        prefHelper = new PreferenceHelper(getApplicationContext());
        prefHelper.setToken(token);
    }


    //앱이 F/G 상일경우 메세지 수신
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        L.i(":::onMessageReceived >>>> " + remoteMessage.getData());
        L.i(":::onMessageReceived >>>> " + remoteMessage.getData().size());
        if (remoteMessage.getData().size() > 0) {
            L.i("::::여기?");
            String title = remoteMessage.getData().get("title");//firebase에서 보낸 메세지의 title
            String message = remoteMessage.getData().get("message");//firebase에서 보낸 메세지의 내용
            onShowNotification(title, message);
        }

        if (remoteMessage.getNotification() != null) {
            L.d("Message Notification Body: " + remoteMessage.getNotification().getBody());
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            onShowNotification(title,message);
        }

    }

    //앱이 B/G상에서 받는 푸쉬 노티피케이션과 동일한 노티 생성
    private void onShowNotification(String title, String content) {
        int id = random.nextInt(Integer.MAX_VALUE);
        Context context = getApplicationContext();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round)) //BitMap 이미지 요구
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        L.e("id = " + id + ", content = " + content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.mipmap.ic_launcher_round); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName = getString(R.string.app_name);
            String description = "푸쉬 메세지";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            channel.setDescription(description);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
        }

        notificationManager.notify(id, builder.build());

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            String seq = Objects.requireNonNull(    FirebaseDatabase.getInstance().getReference().child("notification").push().getKey());

            PushMessage pushMessage = PushMessage.builder().title(title).message(content).timeMils(System.currentTimeMillis()).build();
            FirebaseDatabase.getInstance().getReference()
                    .child("notification")
                    .child(auth.getCurrentUser().getUid())
                    .child(seq)
                    .setValue(pushMessage);
        }
    }
}
