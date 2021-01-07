package com.example.mohaasaba.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.mohaasaba.models.TransactionAccount;

import java.util.List;

@Dao
public interface TransactionAccountDao {
    @Insert
    public void insert(TransactionAccount account);

    @Update
    public void update(TransactionAccount account);

    @Delete
    public void delete(TransactionAccount account);

    @Query("SELECT * FROM transaction_account")
    public LiveData<TransactionAccount> getAllAccount();

    @RawQuery(observedEntities = TransactionAccount.class)
    public LiveData<List<TransactionAccount>> getAllAccounts(SimpleSQLiteQuery query);
}
