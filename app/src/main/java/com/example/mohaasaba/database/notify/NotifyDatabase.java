package com.example.mohaasaba.database.notify;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mohaasaba.database.DataConverter;

@Database(entities = {Notify.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class NotifyDatabase extends RoomDatabase {
    public static NotifyDatabase notifyDatabaseInstance;
    public abstract NotifyDao notifyDao();

    public static synchronized NotifyDatabase getInstance(Context context) {
        if (notifyDatabaseInstance == null) {
            notifyDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), NotifyDatabase.class, "notifyDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallbacks)
                    .build();
        }

        return notifyDatabaseInstance;
    }

    private static RoomDatabase.Callback roomCallbacks = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Thread thread = new Thread(NotifyDatabase::populateDatabase);
            thread.start();
        }
    };

    public static void populateDatabase() {}

}
