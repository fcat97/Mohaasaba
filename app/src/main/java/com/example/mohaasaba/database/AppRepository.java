package com.example.mohaasaba.database;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.mohaasaba.models.Note;
import com.example.mohaasaba.models.Reminder;
import com.example.mohaasaba.models.Schedule;
import com.example.mohaasaba.models.ScheduleType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AppRepository {
    public static final String TAG = "AppRepository";
    public static final String TAG2 = "ActiveReminder";
    private ScheduleDao mScheduleDao;
    private NoteDao mNoteDao;
    private ReminderDao mReminderDao;

    public AppRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        this.mScheduleDao = appDatabase.scheduleDao();
    }

    public AppRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        this.mScheduleDao = appDatabase.scheduleDao();
        this.mNoteDao = appDatabase.noteDao();
        this.mReminderDao = appDatabase.reminderDao();
    }

    public void insertSchedule(Schedule schedule) {
        new InsertScheduleAsyncTask(mScheduleDao).execute(schedule);
    }
    public void updateSchedule(Schedule schedule) {
        new UpdateScheduleAsyncTask(mScheduleDao).execute(schedule);
    }
    public void deleteSchedule(Schedule schedule) {
        new DeleteScheduleAsyncTask(mScheduleDao).execute(schedule);
    }

    public void insertNote(Note note) {
        new InsertNoteAsyncTask(mNoteDao).execute(note);
    }
    public void updateNote(Note note) {
        new UpdateNoteAsyncTask(mNoteDao).execute(note);
    }
    public void deleteNote(String noteID) {
        new DeleteNoteAsyncTask(mNoteDao).execute(noteID);
    }

    public int insertReminder(Reminder reminder) throws ExecutionException, InterruptedException {
        InsertReminderHandler insertReminder = new InsertReminderHandler(mReminderDao, reminder);
        return insertReminder.getPendingIntentID();

    }
    public void updateReminder(Reminder reminder) {
        new UpdateReminderAsyncTask(mReminderDao).execute(reminder);
    }
    public void deleteReminder(String reminderID) {
        new DeleteReminderAsyncTask(mReminderDao).execute(reminderID);
    }


    public LiveData<List<Schedule>> getAllSchedule() {
        return mScheduleDao.getAllSchedule();
    }
    public LiveData<List<Schedule>> getScheduleOfToday() {
        String dayOfWeek = getDayOfWeek();
        String monthOfYear = getMonthOfYear();

        List<ScheduleType.Dates> datesList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        datesList.add(new ScheduleType.Dates(calendar));

        String conditions = getQueryString(datesList);
        Log.i(TAG, "getScheduleOfToday: " + conditions);

        String query = "SELECT * FROM schedule_table WHERE " +
                "(type_daily IS 1) OR " +
                "((type_day_specified IS 1) AND (" +  dayOfWeek + " IS 1)) OR " +
                "((type_month_specified IS 1) AND (" + monthOfYear + " IS 1)) OR " +
                "((type_month_day_specified IS 1) AND (" + dayOfWeek + " IS 1) AND (" + monthOfYear + " IS 1)) OR " +
                conditions + " ORDER BY scheduleID DESC";
        Log.d(TAG, "getScheduleOfToday: " + query);
        return mScheduleDao.getSchedule(new SimpleSQLiteQuery(query));
    }
    public List<Schedule> getSchedulesOfToday() throws ExecutionException, InterruptedException {
        String dayOfWeek = getDayOfWeek();
        String monthOfYear = getMonthOfYear();

        List<ScheduleType.Dates> datesList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        datesList.add(new ScheduleType.Dates(calendar));

        String conditions = getQueryString(datesList);
        Log.i(TAG, "getScheduleOfToday: " + conditions);

        String query = "SELECT * FROM schedule_table WHERE " +
                "(type_daily IS 1) OR " +
                "((type_day_specified IS 1) AND (" +  dayOfWeek + " IS 1)) OR " +
                "((type_month_specified IS 1) AND (" + monthOfYear + " IS 1)) OR " +
                "((type_month_day_specified IS 1) AND (" + dayOfWeek + " IS 1) AND (" + monthOfYear + " IS 1)) OR " +
                conditions + " ORDER BY scheduleID DESC";
        Log.d(TAG, "getScheduleOfToday: " + query);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Schedule>> result = executorService.submit(new Callable<List<Schedule>>() {
            @Override
            public List<Schedule> call() throws Exception {
                return mScheduleDao.getScheduleList(new SimpleSQLiteQuery(query));
            }
        });

        return result.get();
    }

    public List<Schedule> getAllSchedules() throws ExecutionException, InterruptedException {
        String query = "SELECT * FROM schedule_table";

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Schedule>> result = executorService.submit(new Callable<List<Schedule>>() {
            @Override
            public List<Schedule> call() throws Exception {
                return mScheduleDao.getScheduleList(new SimpleSQLiteQuery(query));
            }
        });

        return result.get();
    }
    public LiveData<List<Schedule>> getSchedulesOfThisWeek() {
        String monthOfYear = getMonthOfYear();

        Calendar calendar = Calendar.getInstance();
        List<ScheduleType.Dates> datesList = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, i);
            datesList.add(new ScheduleType.Dates(calendar));
        }
        for (ScheduleType.Dates date:
             datesList) {
            Log.i(TAG, "getSchedulesOfThisWeek: " + date.toString());
        }
        String customDates = getQueryString(datesList);
        Log.i(TAG, "getSchedulesOfThisWeek: " + customDates);


        String query = "SELECT * FROM schedule_table WHERE " +
                "(type_daily IS 1) OR (" + monthOfYear + " IS 1)" + " OR " + "(type_day_specified IS 1)" + " OR " +
                customDates + " ORDER BY scheduleID DESC";
        Log.i(TAG, "getSchedulesOfThisWeek: " + query);
        return mScheduleDao.getSchedule(new SimpleSQLiteQuery(query));
    }
    public LiveData<List<Schedule>> getSchedulesOfThisMonth() {
        String monthOfYear = getMonthOfYear();

        Calendar calendar = Calendar.getInstance();
        List<ScheduleType.Dates> datesList = new ArrayList<>();

        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            datesList.add(new ScheduleType.Dates(calendar));
        }
        for (ScheduleType.Dates date:
                datesList) {
            Log.i(TAG, "getSchedulesOfThisMonth: " + date.toString());
        }
        String customDates = getQueryString(datesList);
        String query = "SELECT * FROM schedule_table WHERE " +
                "(type_daily IS 1) OR (" + monthOfYear + " IS 1)" + " OR " + "(type_day_specified IS 1)" + " OR " +
                customDates + " ORDER BY scheduleID DESC";
        Log.i(TAG, "getSchedulesOfThisMonth: " + query);
        return mScheduleDao.getSchedule(new SimpleSQLiteQuery(query));
    }
    public LiveData<List<Schedule>> getSchedule(List<String> scheduleIDs) {
        return mScheduleDao.getSchedule(scheduleIDs);
    }

    public LiveData<Note> getNote(String noteID) {
        return mNoteDao.getNote(noteID);
    }
    public Reminder getReminder(final String reminderID) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Reminder> result = executor.submit(new Callable<Reminder>() {
            @Override
            public Reminder call() throws Exception {
                return mReminderDao.getReminder(reminderID);
            }
        });

        return result.get();
    }
    private String getMonthOfYear() {
        Calendar c = Calendar.getInstance();
        String monthOfYear;
        switch (c.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                monthOfYear = "everyJanuary";
                break;
            case Calendar.FEBRUARY:
                monthOfYear = "everyFebruary";
                break;
            case Calendar.MARCH:
                monthOfYear = "everyMarch";
                break;
            case Calendar.APRIL:
                monthOfYear = "everyApril";
                break;
            case Calendar.MAY:
                monthOfYear = "everyMay";
                break;
            case Calendar.JUNE:
                monthOfYear = "everyJune";
                break;
            case Calendar.JULY:
                monthOfYear = "everyJuly";
                break;
            case Calendar.AUGUST:
                monthOfYear = "everyAugust";
                break;
            case Calendar.SEPTEMBER:
                monthOfYear = "everySeptember";
                break;
            case Calendar.OCTOBER:
                monthOfYear = "everyOctober";
                break;
            case Calendar.NOVEMBER:
                monthOfYear = "everyNovember";
                break;
            case Calendar.DECEMBER:
                monthOfYear = "everyDecember";
                break;
            default:
                monthOfYear = "type_monthly";
                break;
        }
        return monthOfYear;
    }
    private String getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        String dayOfWeek;
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SATURDAY:
                dayOfWeek = "everySaturday";
                break;
            case Calendar.SUNDAY:
                dayOfWeek = "everySunday";
                break;
            case Calendar.MONDAY:
                dayOfWeek = "everyMonday";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "everyTuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "everyWednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "everyThursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "everyFriday";
                break;
            default:
                dayOfWeek = "type_weekly";
                break;
        }
        return dayOfWeek;
    }
    private String getQueryString(List<ScheduleType.Dates> datesList) {
        StringBuilder builder = new StringBuilder();

        String prefix = "selectedDates LIKE '%";
        String suffix = "%' OR ";

        for (ScheduleType.Dates date:
             datesList) {
            builder.append(prefix);
            builder.append(date.toString());
            builder.append(suffix);
        }

        int len = builder.length();
        builder.delete(len - 4, len - 1);
        Log.i(TAG, "getQueryString: " + builder);

        return builder.toString();
    }

    private static class InsertScheduleAsyncTask extends AsyncTask<Schedule, Void, Void> {
        private ScheduleDao dao;
        public InsertScheduleAsyncTask(ScheduleDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Schedule... schedules) {
            dao.insertSchedule(schedules[0]);
            return null;
        }
    }
    private static class UpdateScheduleAsyncTask extends AsyncTask<Schedule, Void, Void> {
        private ScheduleDao dao;
        public UpdateScheduleAsyncTask(ScheduleDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Schedule... schedules) {
            dao.updateSchedule(schedules[0]);
            return null;
        }
    }
    private static class DeleteScheduleAsyncTask extends AsyncTask<Schedule, Void, Void> {
        private ScheduleDao dao;
        public DeleteScheduleAsyncTask(ScheduleDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Schedule... schedules) {
            dao.deleteSchedule(schedules[0]);
            return null;
        }
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao dao;
        public InsertNoteAsyncTask(NoteDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            dao.insertNote(notes[0]);
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao dao;
        public UpdateNoteAsyncTask(NoteDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            dao.updateNote(notes[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<String, Void, Void> {
        private NoteDao dao;
        public DeleteNoteAsyncTask(NoteDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(String... noteIDs) {
            dao.deleteNote(noteIDs[0]);
            return null;
        }
    }

    private static class UpdateReminderAsyncTask extends AsyncTask<Reminder, Void, Void> {
        private ReminderDao dao;
        public UpdateReminderAsyncTask(ReminderDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Reminder... reminders) {
            dao.updateReminder(reminders[0]);
            return null;
        }
    }
    private static class DeleteReminderAsyncTask extends AsyncTask<String, Void, Void> {
        private ReminderDao dao;

        public DeleteReminderAsyncTask(ReminderDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(String... reminderIDs) {
            dao.deleteReminder(reminderIDs[0]);
            return null;
        }
    }
    private static class InsertReminderHandler {
        private ReminderDao dao;
        private Reminder reminder;
        ExecutorService executor;

        public InsertReminderHandler(ReminderDao dao, Reminder reminder) {
            this.dao = dao;
            this.reminder = reminder;
            this.executor = Executors.newSingleThreadExecutor();
        }

        Future<Integer> taskResult() {
            return executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    dao.insertReminder(reminder);
                    return dao.getPendingIntentID(reminder.getReminderID());
                }
            });
        }

        public int getPendingIntentID() throws ExecutionException, InterruptedException {
            return taskResult().get();
        }
    }

}
