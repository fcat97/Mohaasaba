package com.example.mohaasaba.ibadah.tasbih;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TasbihDao {

    @Insert
    void insert(Tasbih tasbih);

    @Update
    void update(Tasbih tasbih);

    @Delete
    void delete(Tasbih tasbih);

    @Query("SELECT * FROM tasbih_table")
    LiveData<List<Tasbih>> getAllTasbih();

    @Query("SELECT * FROM tasbih_table WHERE tasbihID IS :tasbihID")
    Tasbih getTasbih(String tasbihID);

    @Query("SELECT * FROM tasbih_table")
    List<Tasbih> getTasbihList();
}
