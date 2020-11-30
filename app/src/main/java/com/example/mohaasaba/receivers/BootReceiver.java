package com.example.mohaasaba.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootReceiver extends BroadcastReceiver {
    public static final int BOOT_RECEIVED_ID = 90077;
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationScheduler notificationScheduler = new NotificationScheduler(context);
        notificationScheduler.scheduleNotifications();
    }
}
