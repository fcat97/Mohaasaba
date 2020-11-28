package com.example.mohaasaba.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.mohaasaba.R;

import java.util.Calendar;

public class NotifyHelper {
    private Context context;
    private NotificationManager notificationManager;

    public NotifyHelper(Context context) {
        this.context = context;
    }

    // Step 1: Create Notification Channel
    public static final String DEFAULT_CHANNEL_ID = "NotifyHelper.DEFAULT_CHANNEL_ID";
    public static final String DEFAULT_CHANNEL_NAME = "Default";

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createDefaultChannel(Context context) {
        NotificationChannel notificationChannel = new NotificationChannel(DEFAULT_CHANNEL_ID,
                DEFAULT_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

        // set channel Properties
        notificationChannel.enableVibration(true);
        notificationChannel.canShowBadge();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);

        // Now resister the channel for this app
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }


    // Step 2: Create A Notification
    private NotificationCompat.Builder getBuilder(Context context, String title, String message) {
        return new NotificationCompat.Builder(context.getApplicationContext(),DEFAULT_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setVibrate(new long[] {100, 5000, 100, 500, 100, 500})
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }

    // Step 3: Get the notification
    public void notify(String title, String message, int uniqueID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createDefaultChannel(context);
        if (notificationManager == null ) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        notificationManager.notify(uniqueID, getBuilder(context, title, message).build());
    }


    /*
    * Simple 3 Steps to Create a Simple Android Notification
    * Author sz97;
    * 23/11/2020 10:23 PM GMT +6
    * */
}
