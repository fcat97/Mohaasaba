package com.example.mohaasaba.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.mohaasaba.database.AppRepository;
import com.example.mohaasaba.models.Notify;
import com.example.mohaasaba.models.Schedule;
import com.example.mohaasaba.models.ScheduleType;

import java.util.concurrent.ExecutionException;

public class AddScheduleViewModel extends AndroidViewModel {
    private static final String TAG = "AddScheduleViewModel";
    private AppRepository repository;
    private Schedule schedule;
    /*private Reminder mReminder;*/
    private boolean scheduleEdited = false;

    public AddScheduleViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(Application context, Schedule schedule) throws ExecutionException, InterruptedException {
        this.repository = new AppRepository(context);
        if (schedule == null) this.schedule = new Schedule("dummy_schedule");
        else {
            this.schedule = schedule;
        }
    }

    /*public Reminder getReminder() {
        return mReminder;
    }*/
    /*public void setReminder(Reminder reminder) {
        this.mReminder = reminder;
    }*/
    public void setScheduleEdited(boolean scheduleEdited) {
        this.scheduleEdited = scheduleEdited;
    }
    public void setScheduleType(ScheduleType mScheduleType) {
        schedule.setScheduleType(mScheduleType);
    }
    public ScheduleType getScheduleType() {
        return schedule.getScheduleType();
    }
    public Schedule getSchedule() {
        return schedule;
    }
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }


    /*public void activateReminder(Context context) throws ExecutionException, InterruptedException {
        AlarmReceiver alarmReceiver = new AlarmReceiver(context);
        if (mReminder == null) return; *//* mReminder can't be null *//*
        if (mReminder.getReminderTime().getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) return; *//* Time has passed *//*

        *//* If Reminder is first time created *//*
        if (schedule.getReminderID() == null) {
            schedule.setReminderID(mReminder.getReminderID());
            int pendingIntentID = repository.insertReminder(mReminder);
            alarmReceiver.activateReminder(mReminder,pendingIntentID,schedule.getTitle());
        } else {
            *//* If reminder existed but Edited*//*
            if (schedule.getReminderID().equals(mReminder.getReminderID())) {
                alarmReceiver.cancelReminder(mReminder); *//* As pendingIntentID remains same as before *//*
                alarmReceiver.activateReminder(mReminder,mReminder.getPendingIntentID(),schedule.getTitle()); *//* Cancel and update it *//*
                repository.updateReminder(mReminder);
            } else {
                *//* if previous reminder deleted and new reminder created *//*
                deleteReminder(context); *//* Deactivate & Delete old Reminder*//*
                schedule.setReminderID(mReminder.getReminderID()); *//* Set new Reminder ID to schedule *//*
                int pendingIntentID = repository.insertReminder(mReminder); *//* Insert newReminder and get pendingIntentID *//*
                alarmReceiver.activateReminder(mReminder,pendingIntentID,schedule.getTitle()); *//* Active new Reminder *//*
            }
        }
    }*/

/*    public void deleteReminder(Context context) throws ExecutionException, InterruptedException {
        if (schedule.getReminderID() == null) return; *//* If there is no reminder preExisted*//*
        AlarmReceiver alarmReceiver = new AlarmReceiver(context);
        Reminder oldReminder = repository.getReminder(schedule.getReminderID()); *//* Get the Old Reminder*//*
        alarmReceiver.cancelReminder(oldReminder); *//* Cancel the Reminder *//*
        repository.deleteReminder(oldReminder.getReminderID()); *//* Delete it from database *//*
        schedule.setReminderID(null); *//* as no reminder Attached *//*
    }*/
    public void setNotificationTitles() {
        for (Notify notify:
             schedule.getNotifyList()) {
            notify.label = schedule.getTitle();
        }
    }

    public void insertSchedule() {
        if (scheduleEdited) repository.updateSchedule(schedule);
        else repository.insertSchedule(schedule);
    }
}
