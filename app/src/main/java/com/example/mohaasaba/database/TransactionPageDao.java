package com.example.mohaasaba.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionPage;

import java.util.List;
import java.util.Set;

@Dao
public interface TransactionPageDao {
    @Insert
    void insert(TransactionPage page);

    @Update
    void update(TransactionPage page);

    @Delete
    void delete(TransactionPage page);

    @Query("SELECT * FROM transaction_page")
    LiveData<List<TransactionPage>> getAllPages();

    @Query("SELECT * FROM transaction_page WHERE pageID IS :pageID")
    TransactionPage getPageWithID(String pageID);

    @Query("SELECT pageLabel FROM transaction_page")
    List<String> getPageNames();
}
