package com.example.mohaasaba.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.mohaasaba.helper.NotifyHelper;

public class NotyFire extends BroadcastReceiver {
    public static final String NOTIFICATION_TITLE = "com.mohaasaba.NOTIFICATION_TITLE";
    public static final String NOTIFICATION_MESSAGE = "com.mohaasaba.NOTIFICATION_MESSAGE";
    public static final String NOTIFICATION_ID = "com.mohaasaba.NOTIFICATION_CH_ID";
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String message = intent.getStringExtra(NOTIFICATION_MESSAGE);
        int notification_ID = intent.getIntExtra(NOTIFICATION_ID, -1001);

        NotifyHelper notifyHelper = new NotifyHelper(context);
        notifyHelper.notify(title, message, notification_ID);
    }
}
