package com.example.mohaasaba.database.notify;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotifyDao {
    @Insert
    void insert(Notify notify);

    @Update
    void update(Notify notify);

    @Delete
    void delete(Notify notify);

    @Query("DELETE FROM notification_table WHERE notifyOwnerID IS :ownerID")
    void deleteIDS(String ownerID);

    @Query("SELECT * FROM notification_table WHERE notifyOwnerID IS :ownerID")
    LiveData<List<Notify>> getNotificationOf(String ownerID);

    @Query("SELECT * FROM notification_table")
    List<Notify> getALlNotify();
}
