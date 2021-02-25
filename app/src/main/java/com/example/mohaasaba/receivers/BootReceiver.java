package com.example.mohaasaba.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;


public class BootReceiver extends BroadcastReceiver {
    public static final int BOOT_RECEIVED_ID = 90077;
    private static final String TAG = BootReceiver.class.getSimpleName() + "AlarmIssue";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: called");

        Intent alarmIntent = new Intent(context.getApplicationContext(), NotificationScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), BOOT_RECEIVED_ID,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (pendingIntent != null) Log.d(TAG, "onReceive: pending Intent created");

        try {
            pendingIntent.send(context.getApplicationContext(), 10099, alarmIntent);
            Log.d(TAG, "BootReceive: done");
        } catch (PendingIntent.CanceledException e) {
            Log.d(TAG, "BootReceiver: failed");
            e.printStackTrace();
        }
    }
}
