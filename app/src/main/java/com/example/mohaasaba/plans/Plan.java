package com.example.mohaasaba.plans;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;
import com.example.mohaasaba.ibadah.tasbih.Tasbih;
import com.example.mohaasaba.models.Notify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "plan_table")
public class Plan implements Parcelable {
    private static final String TAG = Plan.class.getCanonicalName();
    @PrimaryKey
    @NonNull
    public String planID;

    public String label = "";
    public String note = "";
    public String tags = "";
    public PlanPeriod period = PlanPeriod.DAILY;
    public int intervalDate = 1;
    public HashSet<Integer> selectedWeekDays = new HashSet<>();
    public long planCreationTime = Calendar.getInstance().getTimeInMillis();

    public HashMap<Long, List<SubPlan>> subPlans = new HashMap<>();

    public List<Notify> notifyList = new ArrayList<>();

    public Plan() {
        planID = IdGenerator.getNewID();
    }

    // SubPlan Related Methods =====================================================================
    private long getKey(Calendar calendar) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        int queryYear = calendar.get(Calendar.YEAR);
        int queryMonth = calendar.get(Calendar.MONTH);
        int queryDate = calendar.get(Calendar.DAY_OF_MONTH);
        int queryDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (period == PlanPeriod.DAILY || period == PlanPeriod.CUSTOM_DATES || period == PlanPeriod.WEEK_DAYS) {
            c.set(Calendar.YEAR, queryYear);
            c.set(Calendar.MONTH, queryMonth);
            c.set(Calendar.DATE, queryDate);
            return c.getTimeInMillis();
        }
        else if (period == PlanPeriod.WEEKLY) {
            c.set(Calendar.YEAR, queryYear);
            c.set(Calendar.MONTH, queryMonth);
            c.set(Calendar.DATE, queryDate);
            c.add(Calendar.DATE, 1 - queryDayOfWeek);
            return c.getTimeInMillis();
        }
        else if (period == PlanPeriod.MONTHLY) {
            c.set(Calendar.YEAR, queryYear);
            c.set(Calendar.MONTH, queryMonth);
            c.set(Calendar.DAY_OF_MONTH , 1);
            return c.getTimeInMillis();
        }
        else if (period == PlanPeriod.YEARLY) {
            c.set(Calendar.YEAR, queryYear);
            c.set(Calendar.DAY_OF_YEAR , 1);
            return c.getTimeInMillis();
        }
        // If period is on Interval Basis
        else {
            Calendar creationTime = Calendar.getInstance();
            creationTime.setTimeInMillis(planCreationTime);
            creationTime.set(Calendar.HOUR_OF_DAY, 0);
            creationTime.set(Calendar.MINUTE, 0);
            creationTime.set(Calendar.SECOND, 0);
            creationTime.set(Calendar.MILLISECOND, 0);

            // calculate difference in millis
            long diffInMillis = Math.abs(calendar.getTimeInMillis() - planCreationTime);

            // convert milliseconds to days
            long diffDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

            // Calculate how many interval passed
            int x = (int) diffDays / (int) intervalDate;

            if (x > 0) creationTime.add(Calendar.DATE, intervalDate * x);
            return creationTime.getTimeInMillis();
        }
    }

    public void commitSubPlans(Calendar calendar, List<SubPlan> sL) {
        long key = getKey(calendar);
        subPlans.remove(key);
        subPlans.put(key, sL);
    }

    public List<SubPlan> getSubPlans(Calendar calendar) {
        long key = getKey(calendar);

        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTimeInMillis(key);
        if (subPlans.containsKey(key)) return subPlans.get(key);

        List<Long> keyList = new ArrayList<>(subPlans.keySet());
        if (! keyList.contains(key)) keyList.add(key);

        Collections.sort(keyList);
        int index = keyList.indexOf(key);
        if (index == 0) return new ArrayList<>();

        List<SubPlan> previous = subPlans.get(keyList.get(index - 1));
        List<SubPlan> newList = new ArrayList<>();
        assert previous != null;
        for (SubPlan subPlan : previous) {
            SubPlan newSubPlan = new SubPlan(subPlan);
            newSubPlan.count_progress = 0;
            newSubPlan.time_progress = 0;
            newList.add(newSubPlan);
        }

        return newList;
    }

    /**
     * Get Progress as Percent
     * @param calendar date of progress query
     * @return percent value of total progress of query date
     */
    public float getProgress(Calendar calendar) {
        List<SubPlan> subPlans = getSubPlans(calendar);

        if (subPlans.size() == 0) return 0f;

        float progress = 0f;
        for (SubPlan subPlan : subPlans) {
            if (subPlan.track_time) {
                progress += (float) subPlan.time_progress / subPlan.time_goal;
            }
            else {
                progress += (float) subPlan.count_progress / subPlan.count_goal;
            }
        }
        progress /= subPlans.size();
        return 100 * progress;
    }

    public List<Float> getProgressBetween(Calendar starting, Calendar ending) {
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

    // Parcelable Implementation ===================================================================
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(planID);
        dest.writeString(label);
        dest.writeString(note);
        dest.writeString(tags);
        dest.writeInt(period.ordinal());
        dest.writeInt(intervalDate);
        dest.writeSerializable(selectedWeekDays);
        dest.writeLong(planCreationTime);
        dest.writeSerializable(subPlans);
        dest.writeTypedList(notifyList);
    }

    protected Plan(Parcel in) {
        planID = in.readString();
        label = in.readString();
        note = in.readString();
        tags = in.readString();
        period = PlanPeriod.values()[in.readInt()];
        intervalDate = in.readInt();
        selectedWeekDays = (HashSet<Integer>) in.readSerializable();
        planCreationTime = in.readLong();
        subPlans = (HashMap<Long, List<SubPlan>>) in.readSerializable();
        notifyList = in.createTypedArrayList(Notify.CREATOR);
    }

    public static final Creator<Plan> CREATOR = new Creator<Plan>() {
        @Override
        public Plan createFromParcel(Parcel in) {
            return new Plan(in);
        }

        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };
}
