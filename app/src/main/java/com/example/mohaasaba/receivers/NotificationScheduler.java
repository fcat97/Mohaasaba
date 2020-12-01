package com.example.mohaasaba.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mohaasaba.database.AppRepository;
import com.example.mohaasaba.models.Notify;
import com.example.mohaasaba.models.Schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class NotificationScheduler extends BroadcastReceiver{
    private static final String TAG = NotificationScheduler.class.getSimpleName();
    public static final int MIDNIGHT_REQUEST_PID = 70057;
    public static final String RUNNING_PID = "com.mohaasaba.RUNNING_PENDING_INTENTS";
    public static final String NOTIFY_SHARED_PREF = "com.mohaasaba.NOTIFY_SHARED_PREF";

    private AppRepository repository;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: called");
        this.mContext = context;
        this.repository = new AppRepository(context);
        this.sharedPreferences = context.getSharedPreferences(NOTIFY_SHARED_PREF, Context.MODE_PRIVATE);
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        
        scheduleNow();
    }

    private void scheduleNow() {
        try {
            cancelAll();
            Log.d(TAG, "scheduleNotifications: all canceled");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "scheduleNotifications: canceled failed");
        }

        try {
            activateAllOfToday();
            Log.d(TAG, "scheduleNotifications: all activated");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "scheduleNotifications: activation failed");
        }
        
        activeNextMidnight(); //  active for next midnight
    }
    
    private void activeNextMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.DATE,1);

        
        Intent intent = new Intent(mContext.getApplicationContext(), NotificationScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), MIDNIGHT_REQUEST_PID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAll() throws ExecutionException, InterruptedException {
        for (Notify notify:
                getAllNotifications()) {
            Intent intent = new Intent(mContext.getApplicationContext(), NotyFire.class);
            /*intent.putExtra(NotyFire.NOTIFICATION_TITLE, notify.scheduleTitle);
            intent.putExtra(NotyFire.NOTIFICATION_MESSAGE, notify.message);
            intent.putExtra(NotyFire.NOTIFICATION_ID, notify.uniqueID);*/

            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), notify.uniqueID,
                    intent, PendingIntent.FLAG_NO_CREATE);

            if (pendingIntent != null) alarmManager.cancel(pendingIntent);
            Log.d(TAG, "cancelAll:  canceled notify " + notify.toString());
        }

        // double check(Debugging)
        cancelPIdFromSharedPref();

    }
    private void activateAllOfToday() throws ExecutionException, InterruptedException {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        for (Notify notify:
             getNotificationsOfToday()) {
            // Check if time already passed or not
            if (hour > notify.notificationHour) continue;
            else if (hour == notify.notificationHour && minute >= notify.notificationMinute) continue;

            Intent intent = new Intent(mContext.getApplicationContext(), NotyFire.class);
            intent.putExtra(NotyFire.NOTIFICATION_TITLE, notify.scheduleTitle);
            intent.putExtra(NotyFire.NOTIFICATION_MESSAGE, notify.message);
            intent.putExtra(NotyFire.NOTIFICATION_ID, notify.uniqueID);

            calendar.set(Calendar.HOUR_OF_DAY, notify.notificationHour);
            calendar.set(Calendar.MINUTE, notify.notificationMinute - notify.beforeMinute);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), notify.uniqueID,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (notify.repeatMinute == 0) {
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                        notify.repeatMinute * Notify.MINUTE, pendingIntent);
            }

            savePendingID(notify.message, notify.uniqueID);
            Log.d(TAG, "activateAllOfToday:  Activated for " + notify.toString());
        }
    }

    /**
     * Called when Notification scheduled correctly
     *
     * @param message The message of Notification
     * @param pendingIntentID pending intent ID used to create the Pending Intent
     * */
    private void savePendingID(String message, int pendingIntentID) {
        Log.d(TAG, "savePendingID:  saving PID " + pendingIntentID);

        Set<String> runningPIdSet = sharedPreferences.getStringSet(RUNNING_PID, new HashSet<>());
        String value = message + "<<:>>" + pendingIntentID;
        runningPIdSet.add(value);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(RUNNING_PID, runningPIdSet);
        editor.apply();
        Log.d(TAG, "savePendingID:  DONE...");
    }

    /**
     * Used for Debugging purpose.
     * To ensure all pending Notifications are canceled correctly.
     * */
    private void cancelPIdFromSharedPref() {
        Set<String> runningPIdSet = sharedPreferences.getStringSet(RUNNING_PID, new HashSet<>());
        Log.d(TAG, "cancelPIdFromSharedPref:  before cancel runningPIdSet.size() " + runningPIdSet.size());
        for (String value : runningPIdSet) {
            Log.d(TAG, "cancelPIdFromSharedPref:  canceling with sharedPref");
            Log.d(TAG, "cancelPIdFromSharedPref:  value " + value);

            String message = value.split("<<:>>")[0];
            int uniqueID = Integer.parseInt(value.split("<<:>>")[1]);

            Intent intent = new Intent(mContext.getApplicationContext(), NotyFire.class);
            /*intent.putExtra(NotyFire.NOTIFICATION_TITLE, "Mohaasaba");
            intent.putExtra(NotyFire.NOTIFICATION_MESSAGE, message);
            intent.putExtra(NotyFire.NOTIFICATION_ID, uniqueID);*/
            Log.d(TAG, "cancelPIdFromSharedPref:  intent created " + intent.toString());

            Log.d(TAG, "cancelPIdFromSharedPref:  pendingIntentID " + uniqueID);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(),
                    uniqueID, intent, PendingIntent.FLAG_NO_CREATE);

            if (pendingIntent != null) {
                Log.d(TAG, "cancelPIdFromSharedPref:  pendingIntent created " + pendingIntent.toString());
                alarmManager.cancel(pendingIntent);
                Log.d(TAG, "cancelPIdFromSharedPref:  alarm Canceled ");
            }

            runningPIdSet.remove(value);
            Log.d(TAG, "cancelPIdFromSharedPref:  id removed from sharedPref");
        }
        Log.d(TAG, "cancelPIdFromSharedPref:  after cancel runningPIdSet.size() " + runningPIdSet.size());

        sharedPreferences.edit()
                .putStringSet(RUNNING_PID, runningPIdSet)
                .apply();
        Log.d(TAG, "cancelPIdFromSharedPref:  sharedPref committed");
        int size = sharedPreferences.getStringSet(RUNNING_PID, new HashSet<>()).size();
        Log.d(TAG, "cancelPIdFromSharedPref: size after " +size);
    }

    private List<Notify> getAllNotifications() throws ExecutionException, InterruptedException {
        List<Schedule> scheduleList = repository.getAllSchedules();
        List<Notify> notifyList = new ArrayList<>();

        for (Schedule schedule:
                scheduleList) {
            notifyList.addAll(schedule.getNotifyList());
        }

        Log.d(TAG, "getAllNotifications:  allNotification.size() " + notifyList.size());
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

        Log.d(TAG, "getAllNotifications:  todayNotifications.size() " + notifyList.size());
        return notifyList;
    }
}
