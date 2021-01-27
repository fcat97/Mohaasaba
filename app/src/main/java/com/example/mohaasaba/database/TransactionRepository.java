package com.example.mohaasaba.database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.mohaasaba.models.Schedule;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionAccount;
import com.example.mohaasaba.models.TransactionPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TransactionRepository {
    private AppDatabase database;
    private TransactionAccountDao accountDao;
    private TransactionDao transactionDao;
    private TransactionPageDao transactionPageDao;

    public TransactionRepository(Context context) {
        this.database = AppDatabase.getInstance(context);
        this.transactionPageDao = database.transactionPageDao();
        this.accountDao = database.accountDao();
        this.transactionDao = database.transactionDao();
    }


    public void updateTransaction(Transaction transaction) {
        Thread thread = new Thread(() -> {
            TransactionAccount account = accountDao.getAccount(transaction.account);
            Transaction t = transactionDao.getTransaction(transaction.entryKey);
            if (t != null) {
                transactionDao.update(transaction);
                account.balance -= t.amount;
            }
            else transactionDao.insert(transaction);

            account.balance += transaction.amount;
            accountDao.update(account);
        });
        thread.start();
    }
    public void deleteTransaction(Transaction transaction) {
        Thread thread = new Thread(() -> {
            transactionDao.delete(transaction);
            TransactionAccount account = accountDao.getAccount(transaction.account);
            account.balance -= transaction.amount;
            accountDao.update(account);
        });
        thread.start();
    }

    public List<Transaction> getTransactionOf(String page) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Transaction>> result = executorService.submit(new Callable<List<Transaction>>() {
            @Override
            public List<Transaction> call() throws Exception {
                return transactionDao.getTransactionOf(page);
            }
        });

        List<Transaction> transactions = new ArrayList<>();
        try {
            transactions = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    public LiveData<List<Transaction>> getAllTransactions() {
        final String query = "SELECT * FROM transaction_table";

        return transactionDao.getAllTransaction(new SimpleSQLiteQuery(query));
    }



    public void updatePage(TransactionPage page) {
        Thread thread = new Thread(() -> {
           TransactionPage _page = transactionPageDao.getPageWithID(page.pageID);

           if (_page != null) transactionPageDao.update(page);
           else transactionPageDao.insert(page);
        });

        thread.start();
    }

    public void deletePage(TransactionPage page) {
        Thread thread = new Thread(() -> {
            transactionPageDao.delete(page);
        });

        thread.start();
    }

    public LiveData<List<TransactionPage>> getAllTransactionPages() {
        return transactionPageDao.getAllPages();
    }

    public void updateTransactionAccount(TransactionAccount transactionAccount) {
        Thread thread = new Thread(() -> {
            TransactionAccount account = accountDao.getAccount(transactionAccount.name);
            if (account != null) accountDao.update(transactionAccount);
            else accountDao.insert(transactionAccount);
        });
        thread.start();
    }
    public void deleteTransactionAccount(TransactionAccount transactionAccount) {
        Thread thread = new Thread(() -> {
            // Default account must be present
            if (transactionAccount.name.equals(TransactionAccount.DEFAULT_ACCOUNT)) return;
            accountDao.delete(transactionAccount);
        });
        thread.start();
    }
    public LiveData<List<TransactionAccount>> getAllTransactionAccounts() {
        String query = "SELECT * FROM transaction_account";
        return accountDao.getAllAccounts(new SimpleSQLiteQuery(query));
    }
}
