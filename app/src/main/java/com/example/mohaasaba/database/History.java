package com.example.mohaasaba.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class History implements Parcelable{
    private static final String TAG = "History";
    private HashMap<Long, List<Task>> taskHash = new HashMap<>();

    public History() {}

    public void commitTodo(Calendar date, List<Task> taskList) {
        long key = getKey(date);
        taskHash.put(key, taskList);
    }
    public List<Task> getTasks(Calendar date) {
        long key = getKey(date);
        Log.d(TAG, "getTasks: called date " + date.get(Calendar.DAY_OF_MONTH) +
                "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR));
        if (taskHash.containsKey(key)) return taskHash.get(key);
        Log.d(TAG, "getTasks: not found");
        return duplicateTasks(key);
    }

    // Get progress in % value;
    public final float getProgress(Calendar date) {
        Log.d(TAG, "getProgress: called date " + date.get(Calendar.DAY_OF_MONTH) +
                "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR));
        // if commit of this date not exist return with zero
        if (! taskHash.containsKey(getKey(date))) return 0;

        Log.d(TAG, "getProgress: TaskList found! calculating progress");
        List<Task> taskList = getTasks(date);
        if (taskList.size() == 0) return 0;

        float progress = 0f;
        for (Task task :
                taskList) {
            progress = progress + task.getProgress();
        }
        Log.d(TAG, "getProgress: TaskList.size() = " + taskList.size());
        return progress / taskList.size();
    }
    public final List<Float> getProgress(Calendar starting, Calendar ending) {
        Log.d(TAG, "getProgress: monthly called");
        List<Float> progressList = new ArrayList<>();

        int start = starting.get(Calendar.DAY_OF_YEAR);
        int end = ending.get(Calendar.DAY_OF_YEAR);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, starting.get(Calendar.YEAR));

        for (int i = start; i <= end; i++) {
            calendar.set(Calendar.DAY_OF_YEAR, i);
            progressList.add(getProgress(calendar));
        }

        return progressList;
    }

    private List<Task> duplicateTasks(long key) {
        if (taskHash.containsKey(key)) return taskHash.get(key);

        Log.d(TAG, "duplicateTasks: creating new List");
        List<Long> keyList = new ArrayList<>(taskHash.keySet());
        if (! keyList.contains(key)) keyList.add(key);

        Collections.sort(keyList);
        int index = keyList.indexOf(key);
        if (index == 0) return new ArrayList<>();  /* if this date is first date */
        Log.d(TAG, "duplicateTasks: getting previous list");

        List<Task> previous = taskHash.get(keyList.get(index - 1));

        List<Task> newList = new ArrayList<>();
        for (Task task :
                previous) {
            Task newTask = new Task(task.text);
            newTask.commitDate = key;
            newTask.taskType = task.taskType;
            newTask.maxProgress = task.maxProgress;
            newTask.currentProgress = 0;
            newTask.step = task.step;
            newList.add(newTask);
        }

        return newList;
    }
    private long getKey(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

        return calendar.getTimeInMillis();
    }














    @Override
    public int hashCode() {
        return super.hashCode();
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    protected History(Parcel in) {
        this.taskHash = (HashMap<Long, List<Task>>) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.taskHash);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };
}
