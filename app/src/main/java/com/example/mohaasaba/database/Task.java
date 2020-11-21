package com.example.mohaasaba.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Calendar;

public class Task implements Parcelable, Serializable {
    public long commitDate;
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
    public String text;
    public Type taskType = Type.Mubah;
    public int maxProgress;
    public int currentProgress;
    public int step;
    public int priority;


    // Public Constructor
    public Task(String text) {
        this.text = text;
        this.maxProgress = 1;
        this.currentProgress = 0;
        this.step = 1;
        this.priority = 1;
        this.commitDate = Calendar.getInstance().getTimeInMillis();
    }

    public float getProgress() {
        return (float) currentProgress * 100 / maxProgress;
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
        if (obj == null) return false;
        else if (this.commitDate != ((Task)obj).commitDate) return false;
        else if (! this.text.equals(((Task)obj).text)) return false;
        else if (! this.taskType.equals(((Task)obj).taskType)) return false;
        else if (this.maxProgress != ((Task)obj).maxProgress) return false;
        else if (this.currentProgress != ((Task)obj).currentProgress) return false;
        else if (this.step != ((Task)obj).step) return false;
        else if (this.priority != ((Task)obj).priority) return false;
        else return super.equals(obj);
    }

    protected Task(Parcel in) {
        this.commitDate = in.readLong();
        this.taskType = Type.values()[in.readInt()];
        this.text = in.readString();
        this.maxProgress = in.readInt();
        this.currentProgress = in.readInt();
        this.step = in.readInt();
        this.priority = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.commitDate);
        dest.writeInt(this.taskType.ordinal());
        dest.writeString(this.text);
        dest.writeInt(this.maxProgress);
        dest.writeInt(this.currentProgress);
        dest.writeInt(this.step);
        dest.writeInt(this.priority);
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
