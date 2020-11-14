package com.example.mohaasaba.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Schedule.class, Note.class, Reminder.class}, version = 39)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase appDatabaseInstance;

    public abstract ScheduleDao scheduleDao();
    public abstract NoteDao noteDao();
    public abstract ReminderDao reminderDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabaseInstance == null) {
            appDatabaseInstance = Room.databaseBuilder(context, AppDatabase.class, "appDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return appDatabaseInstance;
    }

}
