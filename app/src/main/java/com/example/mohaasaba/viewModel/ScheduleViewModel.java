package com.example.mohaasaba.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mohaasaba.database.AppRepository;
import com.example.mohaasaba.database.Reminder;
import com.example.mohaasaba.database.Schedule;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ScheduleViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<List<Schedule>> mAllSchedule;
    private LiveData<List<Schedule>> schedulesOfToday;
    private LiveData<List<Schedule>> schedulesOfThisWeek;
    private LiveData<List<Schedule>> schedulesOfThisMonth;
    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }


    public void deleteSchedule(Schedule schedule) {
        mRepository.deleteSchedule(schedule);
    }
    public void deleteNote(String noteID) {
        mRepository.deleteNote(noteID);
    }

    public LiveData<List<Schedule>> getAllSchedule() {
        mAllSchedule = mRepository.getAllSchedule();
        return mAllSchedule;
    }
    public LiveData<List<Schedule>> getSchedulesOfToday() {
        schedulesOfToday = mRepository.getScheduleOfToday();
        return schedulesOfToday;
    }
    public LiveData<List<Schedule>> getSchedulesOfThisWeek() {
        this.schedulesOfThisWeek = mRepository.getSchedulesOfThisWeek();
        return schedulesOfThisWeek;
    }
    public LiveData<List<Schedule>> getSchedulesOfThisMonth() {
        this.schedulesOfThisMonth = mRepository.getSchedulesOfThisMonth();
        return schedulesOfThisMonth;
    }
}
