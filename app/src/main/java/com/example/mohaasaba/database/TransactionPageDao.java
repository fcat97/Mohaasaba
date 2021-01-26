package com.example.mohaasaba.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mohaasaba.models.TransactionPage;

import java.util.List;

@Dao
public interface TransactionPageDao {
    @Insert
    public void insert(TransactionPage transactionPage);

    @Delete
    public void delete(TransactionPage transactionPage);

    @Update
    public void update(TransactionPage transactionPage);

    @Query("SELECT * FROM transaction_page")
    public LiveData<List<TransactionPage>> getAllPages();

}
