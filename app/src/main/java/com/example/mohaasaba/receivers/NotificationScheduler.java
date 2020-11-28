package com.example.mohaasaba.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mohaasaba.database.AppRepository;
import com.example.mohaasaba.database.Notify;
import com.example.mohaasaba.database.Schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class NotificationScheduler extends BroadcastReceiver{
    private static final String TAG = "NotificationScheduler";
    public static final String RUNNING_PID = "com.mohaasaba.RUNNING_PENDING_INTENTS";
    public static final String NOTIFY_SHARED_PREF = "com.mohaasaba.NOTIFY_SHARED_PREF";
    private AppRepository repository;
    private List<Integer> runningNotificationPID = new ArrayList<>();
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        this.repository = new AppRepository(context);
        this.sharedPreferences = context.getSharedPreferences(NOTIFY_SHARED_PREF, Context.MODE_PRIVATE);
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        Log.d(TAG, "onReceive: alarmIssue starting");

        try {
            cancelAll();
            Log.d(TAG, "onReceive: alarmIssue all canceled");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "onReceive: alarmIssue failed to cancel");
            return;  // don't active notifications if failed;
        }

        try {
            activateAllOfToday();
            Log.d(TAG, "onReceive: alarmIssue activating today");
        } catch (ExecutionException | InterruptedException e) {
            Log.d(TAG, "scheduleNotifications: failed to activate");
            e.printStackTrace();
        }

        Log.d(TAG, "onReceive: alarmIssue done");
    }

    private List<Notify> getAllNotifications() throws ExecutionException, InterruptedException {
        List<Schedule> scheduleList = repository.getAllSchedules();
        List<Notify> notifyList = new ArrayList<>();

        for (Schedule schedule:
             scheduleList) {
            notifyList.addAll(schedule.getNotifyList());
        }

        Log.d(TAG, "getAllNotifications: alarmIssue allNotification.size() " + notifyList.size());
        return notifyList;
    }

    private List<Notify> getNotificationsOfToday() throws ExecutionException, InterruptedException {
        Log.d(TAG, "getNotifications: operation started");
        List<Schedule> scheduleList = repository.getSchedulesOfToday();
        List<Notify> notifyList = new ArrayList<>();

        for (Schedule schedule :
                scheduleList) {
            notifyList.addAll(schedule.getNotifyList());
        }
        Log.d(TAG, "getNotifications: operation ended");
        Log.d(TAG, "getNotifications: operation size " + notifyList.size());

        Log.d(TAG, "getAllNotifications: alarmIssue todayNotifications.size() " + notifyList.size());
        return notifyList;
    }

    private void activateAllOfToday() throws ExecutionException, InterruptedException {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        for (Notify notify:
             getNotificationsOfToday()) {
            // Check if time already passed or not
            if (hour > notify.notificationHour) continue;
            else if (hour == notify.notificationHour && minute >= notify.notificationMinute) continue;

            Intent intent = new Intent(mContext, NotyFire.class);
            intent.putExtra(NotyFire.NOTIFICATION_TITLE, notify.scheduleTitle);
            intent.putExtra(NotyFire.NOTIFICATION_MESSAGE, notify.message);
            intent.putExtra(NotyFire.NOTIFICATION_ID, notify.uniqueID);

            calendar.set(Calendar.HOUR_OF_DAY, notify.notificationHour);
            calendar.set(Calendar.MINUTE, notify.notificationMinute - notify.beforeMinute);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, notify.uniqueID,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (notify.repeatMinute == 0) {
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), notify.repeatMinute * Notify.MINUTE, pendingIntent);
            }

            savePendingID(notify.message, notify.uniqueID);
            Log.d(TAG, "activateAllOfToday: alarmIssue Activated for " + notify.toString());
        }
    }

    private void cancelAll() throws ExecutionException, InterruptedException {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        for (Notify notify:
                getAllNotifications()) {
            Intent intent = new Intent(mContext, NotyFire.class);
            /*intent.putExtra(NotyFire.NOTIFICATION_TITLE, notify.scheduleTitle);
            intent.putExtra(NotyFire.NOTIFICATION_MESSAGE, notify.message);
            intent.putExtra(NotyFire.NOTIFICATION_ID, notify.uniqueID);*/

            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, notify.uniqueID,
                    intent, PendingIntent.FLAG_NO_CREATE);

            if (pendingIntent != null) alarmManager.cancel(pendingIntent);
            Log.d(TAG, "cancelAll: alarmIssue canceled notify " + notify.toString());
        }

        // double check(Debugging)
        cancelPIdFromSharedPref();

    }
    private void savePendingID(String message, int pendingIntentID) {
        Log.d(TAG, "savePendingID: alarmIssue saving PID " + pendingIntentID);

        Set<String> runningPIdSet = sharedPreferences.getStringSet(RUNNING_PID, new HashSet<>());
        String value = message + "<<:>>" + pendingIntentID;
        runningPIdSet.add(value);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(RUNNING_PID, runningPIdSet);
        editor.apply();
        Log.d(TAG, "savePendingID: alarmIssue DONE...");
    }

    private void cancelPIdFromSharedPref() {
        Set<String> runningPIdSet = sharedPreferences.getStringSet(RUNNING_PID, new HashSet<>());
        Log.d(TAG, "cancelPIdFromSharedPref: alarmIssue before cancel runningPIdSet.size() " + runningPIdSet.size());
        for (String value : runningPIdSet) {
            Log.d(TAG, "cancelPIdFromSharedPref: alarmIssue canceling with sharedPref");
            Log.d(TAG, "cancelPIdFromSharedPref: alarmIssue value " + value);

            String message = value.split("<<:>>")[0];
            int uniqueID = Integer.parseInt(value.split("<<:>>")[1]);

            Intent intent = new Intent(mContext, NotyFire.class);
            /*intent.putExtra(NotyFire.NOTIFICATION_TITLE, "Mohaasaba");
            intent.putExtra(NotyFire.NOTIFICATION_MESSAGE, message);
            intent.putExtra(NotyFire.NOTIFICATION_ID, uniqueID);*/
            Log.d(TAG, "cancelPIdFromSharedPref: alarmIssue intent created " + intent.toString());

            Log.d(TAG, "cancelPIdFromSharedPref: alarmIssue pendingIntentID " + uniqueID);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, uniqueID, intent, PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null) Log.d(TAG, "cancelPIdFromSharedPref: alarmIssue pendingIntent created " + pendingIntent.toString());

            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                Log.d(TAG, "cancelPIdFromSharedPref: alarmIssue alarm Canceled ");
            }

            runningPIdSet.remove(value);
            Log.d(TAG, "cancelPIdFromSharedPref: alarmIssue id removed from sharedPref");
        }
        Log.d(TAG, "cancelPIdFromSharedPref: alarmIssue after cancel runningPIdSet.size() " + runningPIdSet.size());

        sharedPreferences.edit()
                .putStringSet(RUNNING_PID, runningPIdSet)
                .apply();
        Log.d(TAG, "cancelPIdFromSharedPref: alarmIssue sharedPref committed");
    }
}
