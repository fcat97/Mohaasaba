package com.example.mohaasaba.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mohaasaba.database.AppRepository;
import com.example.mohaasaba.database.History;
import com.example.mohaasaba.database.Note;
import com.example.mohaasaba.database.Reminder;
import com.example.mohaasaba.database.Schedule;
import com.example.mohaasaba.database.ScheduleType;
import com.example.mohaasaba.database.Todo;
import com.example.mohaasaba.receivers.AlarmReceiver;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddScheduleViewModel extends AndroidViewModel {
    private static final String TAG = "AddScheduleViewModel";
    private AppRepository repository;
    private LiveData<Note> noteLiveData;   // Used when add new Schedule mode
    private Note mNote;
    private Schedule schedule;
    private Reminder mReminder;
    private List<Schedule> mSubScheduleList;
    private LiveData<List<Schedule>> subScheduleLiveData;
    private boolean scheduleEdited = false;

    public AddScheduleViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(Application context, Schedule schedule) throws ExecutionException, InterruptedException {
        this.repository = new AppRepository(context);
        if (schedule == null) this.schedule = new Schedule("dummy_schedule");
        else {
            this.schedule = schedule;
            if (schedule.getNoteID() != null) this.noteLiveData = repository.getNote(schedule.getNoteID());
            if (schedule.getReminderID() != null) this.mReminder = repository.getReminder(schedule.getReminderID());
            if (schedule.getSubSchedulesID().size() != 0) this.subScheduleLiveData = repository.getSchedule(schedule.getSubSchedulesID());
        }
    }

    /* Getter and Setter methods*/
    public void setNote(Note note) {
        this.mNote = note;
    }
    public Note getNote() {
        return this.mNote;
    }
    public Reminder getReminder() {
        return mReminder;
    }
    public void setReminder(Reminder reminder) {
        this.mReminder = reminder;
    }
    public void setScheduleEdited(boolean scheduleEdited) {
        this.scheduleEdited = scheduleEdited;
    }
    public LiveData<Note> getNoteLiveData() {
        return noteLiveData;
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
    public void updateTodo(Calendar selectedDate, Todo todo) {
        schedule.getHistory().commitTodo(selectedDate, todo);
    }
    public void updateProgress(History.Progress progress) {
        schedule.getHistory().commitProgress(Calendar.getInstance(), progress);
    }
    public void resetProgress() {
        schedule.getHistory().resetProgressOf(Calendar.getInstance());
    }
    public LiveData<List<Schedule>> getSubScheduleLiveData() {
        return subScheduleLiveData;
    }
    public List<Schedule> getSubScheduleList() {
        return mSubScheduleList;
    }
    public void setSubScheduleList(List<Schedule> mSubScheduleList) {
        /* Don't call for individual SubSchedule */
        this.mSubScheduleList = mSubScheduleList;
    }

    //    Database operations
    public void insertNote() {
        if (mNote == null) return; /* For safety */
        if (schedule.getNoteID() == null) {
            schedule.setNoteID(mNote.getNoteID());
            repository.insertNote(mNote);
        }
        else {
            if (schedule.getNoteID().equals(mNote.getNoteID())) {
                repository.updateNote(mNote);
            } else {
                repository.deleteNote(schedule.getNoteID());
                schedule.setNoteID(mNote.getNoteID());
                repository.insertNote(mNote);
            }
        }
    }
    public void deleteNote() {
        repository.deleteNote(schedule.getNoteID());
        schedule.setNoteID(null);
    }
    public void insertSubSchedules() {
        if (mSubScheduleList == null) return;
        for (Schedule sc : mSubScheduleList ) {
            String s = sc.getScheduleID();
            if (schedule.getSubSchedulesID().contains(s)) repository.updateSchedule(sc);
            else {
                schedule.getSubSchedulesID().add(s);
                repository.insertSchedule(sc);
            }
        }
    }
    public void activateReminder(Context context) throws ExecutionException, InterruptedException {
        AlarmReceiver alarmReceiver = new AlarmReceiver(context);
        if (mReminder == null) return; /* mReminder can't be null */
        if (mReminder.getReminderTime().getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) return; /* Time has passed */

        /* If Reminder is first time created */
        if (schedule.getReminderID() == null) {
            schedule.setReminderID(mReminder.getReminderID());
            int pendingIntentID = repository.insertReminder(mReminder);
            alarmReceiver.activateReminder(mReminder,pendingIntentID,schedule.getTitle());
        } else {
            /* If reminder existed but Edited*/
            if (schedule.getReminderID().equals(mReminder.getReminderID())) {
                alarmReceiver.cancelReminder(mReminder); /* As pendingIntentID remains same as before */
                alarmReceiver.activateReminder(mReminder,mReminder.getPendingIntentID(),schedule.getTitle()); /* Cancel and update it */
                repository.updateReminder(mReminder);
            } else {
                /* if previous reminder deleted and new reminder created */
                deleteReminder(context); /* Deactivate & Delete old Reminder*/
                schedule.setReminderID(mReminder.getReminderID()); /* Set new Reminder ID to schedule */
                int pendingIntentID = repository.insertReminder(mReminder); /* Insert newReminder and get pendingIntentID */
                alarmReceiver.activateReminder(mReminder,pendingIntentID,schedule.getTitle()); /* Active new Reminder */
            }
        }
    }
    public void deleteReminder(Context context) throws ExecutionException, InterruptedException {
        if (schedule.getReminderID() == null) return; /* If there is no reminder preExisted*/
        AlarmReceiver alarmReceiver = new AlarmReceiver(context);
        Reminder oldReminder = repository.getReminder(schedule.getReminderID()); /* Get the Old Reminder*/
        alarmReceiver.cancelReminder(oldReminder); /* Cancel the Reminder */
        repository.deleteReminder(oldReminder.getReminderID()); /* Delete it from database */
        schedule.setReminderID(null); /* as no reminder Attached */
    }

    public void insertSchedule() {
        if (scheduleEdited) repository.updateSchedule(schedule);
        else repository.insertSchedule(schedule);
    }
}
