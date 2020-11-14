package com.example.mohaasaba.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * History.class
 * This class contains 2 sub classes:
 *  1. Progress History
 *  2. Todo_History
 * and a field that indicates if progress is still counting or not
 * This field is useful when user decide not to count progress history but
 * don't wish to loss all the previous records
 * */
public class History implements Parcelable {
    private static final String TAG = "History";
    private boolean countingProgress;
    private HashMap<Long, Progress> progressHistory;
    private HashMap<Long, Todo> todoHash;

    public History() {
        this.progressHistory = new HashMap<>();
        this.todoHash = new HashMap<>();
    }

    /**
     * Commit should be done before saving Schedule */
    public void commitTodo(Calendar calendar, Todo todo) {
        todoHash.put(getDateInMillis(calendar), todo);
    }

    public Todo getTodo(Calendar calendar) {
        if (todoHash.containsKey(getDateInMillis(calendar))) return todoHash.get(getDateInMillis(calendar));
        return getPastTodoCommit(calendar);
    }


    /* Progress related methods */
    public HashMap<Long, Progress> getProgressHistory() {
        return progressHistory;
    }
    public void setProgressHistory(HashMap<Long, Progress> progressHistory) {
        this.progressHistory = progressHistory;
    }
    public Progress getProgressOf(Calendar calendar) {
        if (! countingProgress) return null;
        if (progressHistory.containsKey(getDateInMillis(calendar))) return progressHistory.get(getDateInMillis(calendar));
        else return getPastProgressCommit(calendar);
    }

    public void commitProgress(Calendar calendar, Progress progress) {
        progressHistory.remove(getDateInMillis(calendar));
        progressHistory.put(getDateInMillis(calendar), progress);
        countingProgress = true;
    }
    public void resetProgressOf(Calendar calendar) {
        long date = getDateInMillis(calendar);
        progressHistory.remove(date);
    }

    /**
     *  Private Methods
     */
    private Long getDateInMillis(Calendar calendar) {
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int y = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(y, m, d);
        return calendar.getTimeInMillis();
    }
    private Todo getPastTodoCommit(Calendar calendar) {
        long date = getDateInMillis(calendar);
        List<Long> keyList = new ArrayList<>(todoHash.keySet());
        if (! keyList.contains(date)) keyList.add(date);

        Collections.sort(keyList);
        int index = keyList.indexOf(date);
        if (index == 0) return new Todo();  /* if this date is first date */

        ArrayList<Boolean> newStates = new ArrayList<>();
        ArrayList<String> newTodoText = new ArrayList<>();
        newTodoText.addAll(todoHash.get(keyList.get(index - 1)).getTodoTexts());
        for (int i = 0; i < newTodoText.size(); i++) {
            newStates.add(false);
        }

        return new Todo(newStates, newTodoText);
    }
    private Progress getPastProgressCommit(Calendar calendar) {
        long date = getDateInMillis(calendar);
        List<Long> keyList = new ArrayList<>(progressHistory.keySet());
        if (! keyList.contains(date)) keyList.add(date);

        Collections.sort(keyList);
        int index = keyList.indexOf(date);
        if (index == 0) return new Progress();  /* if this date is first date */

        Progress previous = progressHistory.get(keyList.get(index - 1));

        Progress newProgress = new Progress();
        assert previous != null;
        newProgress.maxProgress = previous.maxProgress;
        newProgress.progressStep = previous.progressStep;
        newProgress.unit = previous.unit;
        newProgress.onTodo = previous.onTodo;
        newProgress.currentProgress = 0;

        return newProgress;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (this.countingProgress != ((History)obj).countingProgress) return false;
        if (this.progressHistory.equals(((History) obj).progressHistory)) return false;
        if (this.todoHash.equals(((History) obj).todoHash)) return false;
        return super.equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.countingProgress ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.progressHistory);
        dest.writeSerializable(this.todoHash);
    }

    protected History(Parcel in) {
        this.countingProgress = in.readByte() != 0;
        this.progressHistory = (HashMap<Long, Progress>) in.readSerializable();
        this.todoHash = (HashMap<Long, Todo>) in.readSerializable();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel source) {
            return new History(source);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    public static final class Progress implements Parcelable, Serializable {
        public int maxProgress;
        public int progressStep = 1;
        public String unit = "";
        public boolean onTodo;
        public int currentProgress;

        public Progress() {

        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null) return false;
            if (this.maxProgress != (((Progress) obj).maxProgress)) return false;
            if (this.progressStep != ((Progress) obj).progressStep) return false;
            if (this.currentProgress != ((Progress) obj).currentProgress) return false;
            if (! this.unit.equals(((Progress) obj).unit)) return false;
            if (this.onTodo != (((Progress) obj).onTodo)) return false;
            else return super.equals(obj);
        }

        @NonNull
        @Override
        public String toString() {
            return "maxProgress " + maxProgress + "\t" +
                    "progressStep " + progressStep + "\t" +
                    "currentProgress " + currentProgress + "\t" +
                    "unit " + unit + "\t" +
                    "onTodo " + onTodo;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.maxProgress);
            dest.writeInt(this.progressStep);
            dest.writeString(this.unit);
            dest.writeByte(this.onTodo ? (byte) 1 : (byte) 0);
            dest.writeInt(this.currentProgress);
        }

        protected Progress(Parcel in) {
            this.maxProgress = in.readInt();
            this.progressStep = in.readInt();
            this.unit = in.readString();
            this.onTodo = in.readByte() != 0;
            this.currentProgress = in.readInt();
        }

        public static final Creator<Progress> CREATOR = new Creator<Progress>() {
            @Override
            public Progress createFromParcel(Parcel source) {
                return new Progress(source);
            }

            @Override
            public Progress[] newArray(int size) {
                return new Progress[size];
            }
        };
    }

}
