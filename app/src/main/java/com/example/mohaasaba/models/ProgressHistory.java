package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProgressHistory implements Parcelable {

    public HashMap<Long, Progress> progressHashMap = new HashMap<>();

    public ProgressHistory() { };

    public void commitProgress(Progress progress, Calendar calendar) {
        long key = getKey(calendar);
        progressHashMap.remove(key);
        progressHashMap.put(key, progress);
    }

    public int getDailyTarget() {
        Progress progress = getProgress(Calendar.getInstance());
        return progress.maxProgress;
    }

    public int getTotalProgress() {
        List<Long> keyList = new ArrayList<>(progressHashMap.keySet());
        Collections.sort(keyList);

        int total = 0;
        for (Long key :
                keyList) {
            total += progressHashMap.get(key).currentProgress;
        }

        return total;
    }

    private long getKey(Calendar calendar) {
        int d = calendar.get(Calendar.DAY_OF_YEAR);
        int y = calendar.get(Calendar.YEAR);

        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.DAY_OF_YEAR, d);
        c.set(Calendar.YEAR, y);

        return c.getTimeInMillis();
    }

    public final Progress getProgress(Calendar calendar) {
        long key = getKey(calendar);
        if (progressHashMap.containsKey(key)) return progressHashMap.get(key);

        List<Long> keyList = new ArrayList<>(progressHashMap.keySet());
        if (! keyList.contains(key)) keyList.add(key);

        Collections.sort(keyList);
        int index = keyList.indexOf(key);
        if (index == 0) return new Progress(key);
        else return progressHashMap.get(keyList.get(index - 1));
    }

















    protected ProgressHistory(Parcel in) {
        this.progressHashMap = (HashMap<Long, Progress>) in.readSerializable();
    }

    public static final Creator<ProgressHistory> CREATOR = new Creator<ProgressHistory>() {
        @Override
        public ProgressHistory createFromParcel(Parcel in) {
            return new ProgressHistory(in);
        }

        @Override
        public ProgressHistory[] newArray(int size) {
            return new ProgressHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.progressHashMap);
    }
}
