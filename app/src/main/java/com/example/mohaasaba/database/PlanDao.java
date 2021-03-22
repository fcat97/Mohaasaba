package com.example.mohaasaba.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mohaasaba.plans.Plan;

import java.util.List;

@Dao
public interface PlanDao {
    @Insert
    void insert(Plan plan);

    @Update
    void update(Plan plan);

    @Delete
    void delete(Plan plan);

    @Query("SELECT * FROM plan_table")
    LiveData<List<Plan>> getAllPlans();

    @Query("SELECT * FROM plan_table WHERE planID IS :planID")
    Plan getPlan(String planID);
}
