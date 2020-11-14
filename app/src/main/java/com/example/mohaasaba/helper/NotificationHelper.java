package com.example.mohaasaba.helper;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mohaasaba.R;

public class NotificationHelper extends ContextWrapper {

    /*Step 1: create these constants here*/
    public static final String CHANNEL_DEFAULT_ID = "notification_ID_default";
    public static final String CHANNEL_DEFAULT_NAME = "Default";
    private static final String TAG = "Notification Helper";

    /*Step 4*/
    public NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        /*Step 3*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel_DEFAULT(CHANNEL_DEFAULT_ID,CHANNEL_DEFAULT_NAME);
        }
    }

    /*Step 2*/
    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel_DEFAULT(String ch_id, String ch_name) {
        NotificationChannel channel = new NotificationChannel(ch_id,ch_name, NotificationManager.IMPORTANCE_HIGH);
        /*Channel specification can be provided here*/
        channel.enableVibration(true);
        channel.canShowBadge();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);


        /*Step 5*/
        getManager().createNotificationChannel(channel);
    }

    /*Step 4*/
    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    /*Step 6*/
    public NotificationCompat.Builder getNotification(String title, String message, String ch_ID) {
        return new NotificationCompat.Builder(getApplicationContext(),ch_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setVibrate(new long[] {100, 5000, 100, 500, 100, 500})
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }

}




/*Steps to create NotificationHelper.class
 *
 * Step 1:   Declare constants of channels name and ID
 * Step 2:   Create channel
 * Step 3:   Give the create channel in constructor with condition
 * Step 4:   Create NotificationManager member variable and initialize it in getManager()
 * Step 5:   createNotificationChannel
 * Step 6:   Create Notification by NotificationCompat.Builder()
 * Step 7:   That's all done with NotificationHelper.class
 *
 * To through Notification:
 * Step 1:   Create a NotificationHelper instance in That activity
 * Step 2:   Initialize it by passing Context
 * Step 3:   Use this function as follow:
 *       NotificationCompat.Builder nb = notificationHelper.getNotification(title, message, ch_ID);
 *       notificationHelper.getManager().notify(unique_id_for_notification, nb.build());
 *
 * Thanks you all..
 *
 * Author:   sz97
 * Date:     03 Jun 2020, 14:21
 * */