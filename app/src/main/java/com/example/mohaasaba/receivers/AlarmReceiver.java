package com.example.mohaasaba.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.mohaasaba.models.Reminder;
import com.example.mohaasaba.helper.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_TITLE = "com.example.mohasabap.NOTIFICATION_TITLE";
    public static final String NOTIFICATION_MESSAGE = "com.example.mohasabap.NOTIFICATION_MESSAGE";
    public static final String NOTIFICATION_CH_ID = "com.example.mohasabap.NOTIFICATION_CH_ID";
    private static final String TAG = "Alarm Receiver";
    private Context mContext;

    public AlarmReceiver() {
        /*Default Constructor for Broadcast Receiver in manifest*/
    }

    public AlarmReceiver(Context context) {
        this.mContext = context;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        /*This context will be the context when onReceive will be invoked
         * Don't be confused with mContext.
         */
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String message = intent.getStringExtra(NOTIFICATION_MESSAGE);
        String chID = intent.getStringExtra(NOTIFICATION_CH_ID);    // to which channel it will notify.

        NotificationHelper helper = new NotificationHelper(context);
        NotificationCompat.Builder nb = helper.getNotification(title,message,chID);

        helper.getManager().notify(1001,nb.build());
    }
    public void activateReminder(Reminder reminder, int pendingIntentID, String scheduleTitle) {
        long reminderTime = reminder.getReminderTime().getTimeInMillis();
        long preReminderTime;
        long repeatInterval = 0L;

        /*Calculate time before notification*/
        if (reminder.getPreRemindTime() != Reminder._NULL_) {
            switch (reminder.getPreReminderUnit()) {
                case Reminder.MINUTE:
                    preReminderTime = reminder.getPreRemindTime() * Reminder.milMinute;
                    break;
                case Reminder.HOUR:
                    preReminderTime = reminder.getPreRemindTime() * Reminder.milHour;
                    break;
                case Reminder.DAY:
                    preReminderTime = reminder.getPreRemindTime() * Reminder.milDay;
                    break;
                case Reminder.MONTH:
                    preReminderTime = reminder.getPreRemindTime() * Reminder.milMonth;
                    break;
                default:
                    preReminderTime = 0L;
            }
            reminderTime = reminderTime - preReminderTime;
        }

        /*Set Repeat Interval Time*/
        if (reminder.getRepeatingInterval() != Reminder._NULL_) {
            switch (reminder.getRepeatIntervalUnit()) {
                case Reminder.MINUTE:
                    repeatInterval = reminder.getRepeatingInterval() * Reminder.milMinute;
                    break;
                case Reminder.HOUR:
                    repeatInterval = reminder.getRepeatingInterval() * Reminder.milHour;
                    break;
                case Reminder.DAY:
                    repeatInterval = reminder.getRepeatingInterval() * Reminder.milDay;
                    break;
                case Reminder.MONTH:
                    repeatInterval = reminder.getRepeatingInterval() * Reminder.milMonth;
                    break;
                default:
                    repeatInterval = 0L;
            }
        }

        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra(NOTIFICATION_TITLE,scheduleTitle);
        if (reminder.getReminderLabel() != null) intent.putExtra(NOTIFICATION_MESSAGE,reminder.getReminderLabel());
        else intent.putExtra(NOTIFICATION_MESSAGE, " ");
        intent.putExtra(NOTIFICATION_CH_ID, NotificationHelper.CHANNEL_DEFAULT_ID);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,pendingIntentID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        assert alarmManager != null;
        if (reminder.getRepeatingInterval() == Reminder._NULL_) {
            alarmManager.set(AlarmManager.RTC,reminderTime,pendingIntent);
        } else {
            alarmManager.setInexactRepeating(AlarmManager.RTC,reminderTime,repeatInterval,pendingIntent);
        }
        Log.d(TAG, "activateReminder: activated pID " + pendingIntentID);
    }

    public void cancelReminder(Reminder reminder) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,reminder.getPendingIntentID(),intent,PendingIntent.FLAG_NO_CREATE);
        Log.d(TAG, "cancelReminder: called pID " + reminder.getPendingIntentID());
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            assert alarmManager != null;
            alarmManager.cancel(pendingIntent);
            Log.d(TAG, "cancelReminder: canceled pID " + reminder.getPendingIntentID());
        }
    }
}
