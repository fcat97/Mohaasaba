package com.example.mohaasaba.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ReminderDao {
    @Insert
    void insertReminder(Reminder reminder);

    @Update
    void updateReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);

    @Query("DELETE FROM reminder_table WHERE reminderID is :reminderID")
    void deleteReminder(String reminderID);

    @Query("SELECT * FROM reminder_table WHERE reminderID is :reminderID")
    Reminder getReminder(String reminderID);

    //TODO: How to get a single column from SQLite operation?
    @Query("SELECT pendingIntentID FROM reminder_table WHERE reminderID is :reminderID")
    int getPendingIntentID(String reminderID);
}
