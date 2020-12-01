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
    private static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: called");

        Intent alarmIntent = new Intent(context.getApplicationContext(), NotificationScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), BOOT_RECEIVED_ID,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (pendingIntent != null) Log.d(TAG, "onReceive: pending Intent created");
        Calendar calendar = Calendar.getInstance();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmIntent != null) Log.d(TAG, "onReceive: alarm manager created");
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.d(TAG, "onReceive: done");
    }
}
