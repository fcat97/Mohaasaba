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

    @Query("SELECT * FROM tasbih_table WHERE tasbihType LIKE '%MUSTAHAB%'")
    LiveData<List<Tasbih>> getTasbihMustahab();

    @Query("SELECT * FROM tasbih_table WHERE tasbihType LIKE '%AFTER_SALAT%'")
    LiveData<List<Tasbih>> getTasbihPrayer();

    @Query("SELECT * FROM tasbih_table WHERE tasbihType LIKE '%AFTER_JUHR%'")
    LiveData<List<Tasbih>> getTasbihJuhr();

    @Query("SELECT * FROM tasbih_table WHERE tasbihType LIKE '%AFTER_ASR%'")
    LiveData<List<Tasbih>> getTasbihAsr();

    @Query("SELECT * FROM tasbih_table WHERE tasbihType LIKE '%AFTER_MAGRIB%'")
    LiveData<List<Tasbih>> getTasbihMagrib();

    @Query("SELECT * FROM tasbih_table WHERE tasbihType LIKE '%AFTER_ESHA%'")
    LiveData<List<Tasbih>> getTasbihEsha();

    @Query("SELECT * FROM tasbih_table WHERE tasbihType LIKE '%BEFORE_SLEEP%'")
    LiveData<List<Tasbih>> getTasbihSleep();

    @Query("SELECT * FROM tasbih_table WHERE tasbihType LIKE '%MORNING_TASBIH%'")
    LiveData<List<Tasbih>> getTasbihMorning();

    @Query("SELECT * FROM tasbih_table WHERE tasbihType LIKE '%EVENING_TASBIH%'")
    LiveData<List<Tasbih>> getTasbihEvening();
}
