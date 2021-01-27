package com.example.mohaasaba.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.mohaasaba.models.Transaction;

import java.util.List;


@Dao
public interface TransactionDao {
    /*
    @Insert
    public void insert(Transaction transaction);

    @Update
    public void update(Transaction transaction);

    @Delete
    public void delete(Transaction transaction);

    @Query("SELECT * FROM transaction_table")
    public LiveData<List<Transaction>> getAllTransaction();

    @RawQuery(observedEntities = Transaction.class)
    public LiveData<List<Transaction>> getAllTransaction(SimpleSQLiteQuery query);

    @Query("SELECT * FROM transaction_table WHERE entryKey IS :entryKey")
    public Transaction getTransaction(String entryKey);

    @Query("SELECT * FROM transaction_table WHERE page IS :pageName")
    public List<Transaction> getTransactionOf(String pageName);

     */
}
