package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Calendar;

public class Task implements Parcelable, Serializable {
    public enum Type {
        Farz,
        Wazib,
        Sunnah,
        Mustahab,
        Mubah,
        Maqruh,
        SagiraGunah,
        KabiraGunah
    }

    public long commitDate;
    public String text;
    public Type taskType = Type.Mubah;
    public float maxProgress;
    public float currentProgress;
    public float step;
    public int priority;
    public String unit;
    public ScheduleType scheduleType;

    // Public Constructor
    public Task(String text) {
        this.text = text;
        this.maxProgress = 1.0f;
        this.currentProgress = 0f;
        this.unit = "";
        this.step = 1.0f;
        this.priority = 1;
        this.commitDate = Calendar.getInstance().getTimeInMillis();
        this.scheduleType = new ScheduleType();
    }

    public Task duplicate() {
        Task t = new Task(text);
        t.taskType = this.taskType;
        t.maxProgress = this.maxProgress;
        t.currentProgress = 0;
        t.step = this.step;
        t.priority = this.priority;
        t.unit = this.unit;
        t.scheduleType = this.scheduleType;

        return t;
    }
    public float getProgress() {
        return currentProgress * 100 / maxProgress;
    }
    public void progress() {
        currentProgress = Math.min(currentProgress + step, maxProgress);
    }
    public void undo() {
        currentProgress = Math.max(currentProgress - step, 0);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
        /*if (obj == null) return false;
        else if (this.commitDate != ((Task)obj).commitDate) return false;
        else if (! this.text.equals(((Task)obj).text)) return false;
        else if (! this.taskType.equals(((Task)obj).taskType)) return false;
        else if (this.maxProgress != ((Task)obj).maxProgress) return false;
        else if (this.currentProgress != ((Task)obj).currentProgress) return false;
        else if (this.step != ((Task)obj).step) return false;
        else if (this.priority != ((Task)obj).priority) return false;
        else if (! this.scheduleType.equals(obj)) return false;
        else return super.equals(obj);*/
    }

    protected Task(Parcel in) {
        this.commitDate = in.readLong();
        this.taskType = Type.values()[in.readInt()];
        this.text = in.readString();
        this.maxProgress = in.readFloat();
        this.currentProgress = in.readFloat();
        this.step = in.readFloat();
        this.priority = in.readInt();
        this.scheduleType = in.readParcelable(ScheduleType.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.commitDate);
        dest.writeInt(this.taskType.ordinal());
        dest.writeString(this.text);
        dest.writeFloat(this.maxProgress);
        dest.writeFloat(this.currentProgress);
        dest.writeFloat(this.step);
        dest.writeInt(this.priority);
        dest.writeParcelable(scheduleType, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

}
