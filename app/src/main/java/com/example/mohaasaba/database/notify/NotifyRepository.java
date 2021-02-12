package com.example.mohaasaba.database.notify;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.mohaasaba.bookshelf.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NotifyRepository {
    private NotifyDao notifyDao;

    public NotifyRepository(Context context) {
        NotifyDatabase notifyDatabase = NotifyDatabase.getInstance(context);
        this.notifyDao = notifyDatabase.notifyDao();
    }

    private Notify getNotify(String notifyID) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Notify> result = executorService.submit(new Callable<Notify>() {
            @Override
            public Notify call() throws Exception {
                return notifyDao.getNotify(notifyID);
            }
        });

        Notify notify = null;
        try {
            notify = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return notify;
    }
    public void updateNotify(Notify notify) {
        Notify n = getNotify(notify.notifyID);
        Thread thread;
        if (n == null) {
            thread = new Thread(() -> notifyDao.insert(notify));
        } else {
            thread = new Thread(() -> notifyDao.update(notify));
        }
        thread.start();

    }
    public void deleteNotify(Notify notify) {
        Thread thread = new Thread(() -> notifyDao.delete(notify));
        thread.start();
    }
    public LiveData<List<Notify>> getNotifyOf(String ownerID) {
        return notifyDao.getNotificationOf(ownerID);
    }
    public void deleteNotifyIDs(String ownerID) {
        Thread thread = new Thread(() -> {
            notifyDao.deleteIDS(ownerID);;
        });
        thread.start();
    }
    public List<Notify> getAllNotify() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        List<Notify> notifies = new ArrayList<>();

        Future<List<Notify>> result = service.submit(new Callable<List<Notify>>() {
            @Override
            public List<Notify> call() throws Exception {
                return notifyDao.getALlNotify();
            }
        });

        try {
            notifies =  result.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return notifies;
    }
}
