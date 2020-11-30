package com.example.mohaasaba.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mohaasaba.helper.ThemeUtils;
import com.example.mohaasaba.models.Note;
import com.example.mohaasaba.models.Notify;
import com.example.mohaasaba.models.Reminder;
import com.example.mohaasaba.models.Schedule;
import com.example.mohaasaba.models.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Database(entities = {Schedule.class, Note.class, Reminder.class}, version = 45)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase appDatabaseInstance;

    public abstract ScheduleDao scheduleDao();
    public abstract NoteDao noteDao();
    public abstract ReminderDao reminderDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabaseInstance == null) {
            appDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "appDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallbacks)
                    .build();
        }

        return appDatabaseInstance;
    }

    private static RoomDatabase.Callback roomCallbacks = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Thread thread = new Thread(AppDatabase::populateDatabase);
            thread.start();
        }
    };

    private static void populateDatabase() {
        ScheduleDao scheduleDao = appDatabaseInstance.scheduleDao();

        Schedule schedule = new Schedule("Quick Tutorial");
        schedule.setThemeID(ThemeUtils.THEME_GREEN);

        Notify notify1 = new Notify(0,0);
        notify1.scheduleTitle = schedule.getTitle();
        notify1.message = "You can add Reminders here!";

        Notify notify2 = new Notify(0,0);
        notify2.scheduleTitle = schedule.getTitle();
        notify2.message = "Click on the + icon in top right corner";

        schedule.getNotifyList().add(notify1);
        schedule.getNotifyList().add(notify2);

        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task("You can add new task from bottom"));
        taskList.add(new Task("Each can contain certain specification"));
        taskList.add(new Task("Progress is saved per day"));
        taskList.add(new Task("New Day New Progress"));
        taskList.add(new Task("Tap and hold to progress task"));
        taskList.add(new Task("the trash icon will delete task"));
        taskList.add(new Task("Tapping circular Progress View on top left you can go to specific date"));
        taskList.add(new Task("Click 'Daily Schedule' text to change schedule type"));
        taskList.add(new Task("Remember to save your change"));

        schedule.getHistory().commitTodo(Calendar.getInstance(), taskList);

        scheduleDao.insertSchedule(schedule);
    }
}
