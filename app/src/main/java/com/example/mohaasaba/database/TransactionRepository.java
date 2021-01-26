package com.example.mohaasaba.database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.mohaasaba.models.TransactionAccount;
import com.example.mohaasaba.models.TransactionPage;

import java.util.List;

public class TransactionRepository {
    private AppDatabase database;
    private TransactionPageDao transactionPageDao;
    private TransactionAccountDao transactionAccountDao;

    public TransactionRepository(Context context) {
        this.database = AppDatabase.getInstance(context);
        this.transactionPageDao = database.transactionPageDao();
        this.transactionAccountDao = database.transactionAccountDao();
    }


    public void insertPage(TransactionPage transactionPage) {
        Thread thread = new Thread(() -> {
            transactionPageDao.insert(transactionPage);
        });
        thread.start();
    }
    public void updatePage(TransactionPage transactionPage) {
        Thread thread = new Thread(() -> {
            transactionPageDao.update(transactionPage);
        });
        thread.start();
    }
    public void deletePage(TransactionPage transactionPage) {
        Thread thread = new Thread(() -> {
            transactionPageDao.delete(transactionPage);
        });
        thread.start();
    }
    public LiveData<List<TransactionPage>> getAllTransactionPages() {
        return transactionPageDao.getAllPages();
    }

    public void insertTransactionAccount(TransactionAccount transactionAccount) {
        Thread thread = new Thread(() -> {
            transactionAccountDao.insert(transactionAccount);
        });
        thread.start();
    }
    public void updateTransactionAccount(TransactionAccount transactionAccount) {
        Thread thread = new Thread(() -> {
            transactionAccountDao.update(transactionAccount);
        });
        thread.start();
    }
    public void deleteTransactionAccount(TransactionAccount transactionAccount) {
        Thread thread = new Thread(() -> {
            transactionAccountDao.delete(transactionAccount);
        });
        thread.start();
    }
    public LiveData<List<TransactionAccount>> getAllTransactionAccounts() {
        String query = "SELECT * FROM transaction_account";
        return transactionAccountDao.getAllAccounts(new SimpleSQLiteQuery(query));
    }
}
