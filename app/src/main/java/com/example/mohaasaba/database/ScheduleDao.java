package com.example.mohaasaba.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert
    void insertSchedule(Schedule schedule);

    @Update
    void updateSchedule(Schedule schedule);

    @Delete
    void deleteSchedule(Schedule schedule);

    @Query("SELECT * FROM schedule_table ORDER BY scheduleID DESC")
    LiveData<List<Schedule>> getAllSchedule();

    @Query("SELECT * FROM schedule_table WHERE scheduleID IN (:scheduleIDs)")
    LiveData<List<Schedule>> getSchedule(List<String> scheduleIDs);

    @RawQuery(observedEntities = Schedule.class)
    LiveData<List<Schedule>> getSchedule(SupportSQLiteQuery query);
}
