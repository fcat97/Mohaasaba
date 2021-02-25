package com.example.mohaasaba.ibadah.tasbih;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.mohaasaba.ibadah.IbadahDB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TasbihRepository {
    private IbadahDB ibadahDB;
    private TasbihDao tasbihDao;

    public TasbihRepository(Context context) {
        this.ibadahDB = IbadahDB.getInstance(context);
        this.tasbihDao = ibadahDB.tasbihDao();
    }


    private Tasbih getTasbih(String tasbihID) {
        ExecutorService service = Executors.newSingleThreadExecutor();

        Future<Tasbih> result = service.submit(() -> tasbihDao.getTasbih(tasbihID));

        Tasbih tasbih = null;
        try {
            tasbih = result.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return tasbih;
    }
    public LiveData<List<Tasbih>> getAllTasbih() {
        return tasbihDao.getAllTasbih();
    }
    public List<Tasbih> getTasbihList() {
        ExecutorService service = Executors.newSingleThreadExecutor();

        Future<List<Tasbih>> result = service.submit(() -> tasbihDao.getTasbihList());
        List<Tasbih> tasbihList = new ArrayList<>();

        try {
            tasbihList = result.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return tasbihList;
    }
    public void updateTasbih(Tasbih tasbih) {
        Tasbih tasbih1 = getTasbih(tasbih.tasbihID);

        Thread thread = new Thread(() -> {
            if (tasbih1 != null) {
                tasbihDao.update(tasbih);
            } else {
                tasbihDao.insert(tasbih);
            }
        });

        thread.start();
    }
    public void deleteTasbih(Tasbih tasbih) {
        Thread thread = new Thread(() -> {
            tasbihDao.delete(tasbih);
        });

        thread.start();
    }
}
