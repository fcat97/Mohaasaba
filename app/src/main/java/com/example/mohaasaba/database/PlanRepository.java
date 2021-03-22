package com.example.mohaasaba.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.mohaasaba.ibadah.bookshelf.Book;
import com.example.mohaasaba.plans.Plan;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PlanRepository {
    private static final String TAG = PlanRepository.class.getCanonicalName();
    private PlanDao planDao;

    public PlanRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        this.planDao = appDatabase.planDao();
    }

    public void update(Plan plan) {
        Plan p = getPlan(plan.planID);
        Thread thread;
        if (p == null) {
            thread = new Thread(() -> planDao.insert(plan));
        } else thread = new Thread(() -> planDao.update(plan));

        thread.start();
    }

    public void delete(Plan plan) {
        Thread thread = new Thread(() -> planDao.delete(plan));
        thread.start();
    }

    private Plan getPlan(String planID) {
        ExecutorService service = Executors.newSingleThreadExecutor();

        Future<Plan> result = service.submit(new Callable<Plan>() {
            @Override
            public Plan call() throws Exception {
                return planDao.getPlan(planID);
            }
        });

        Plan plan = null;
        try {
            plan = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return plan;
    }

    public LiveData<List<Plan>> getAllPlans() {
        Log.d(TAG + "Plan", "getAllPlans: called");
        return planDao.getAllPlans();
    }
}
