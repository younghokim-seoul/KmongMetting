package com.example.meeting.utils;

import android.content.Context;
import android.os.PowerManager;


/**
 * Created by user on 2016-07-12.
 */
public class AlarmWakeLock {

    private final static String TAG = AlarmWakeLock.class.getSimpleName();
    private static PowerManager.WakeLock sWakeLock;

    public static void acquireWakeLock(Context context) {
        if (sWakeLock != null || context == null) {
            return;
        }

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        sWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        //wakeLock이 이미 acquire되었을 경우 pass
        if (!sWakeLock.isHeld())
            sWakeLock.acquire();
    }

    public static void releaseWakeLock(Context context) {
        if (sWakeLock != null && sWakeLock.isHeld()) {
            sWakeLock.release();
            sWakeLock = null;
        }
    }
}
