package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Progress implements Parcelable, Serializable {
    private static final String TAG = Progress.class.getCanonicalName();
    public long commitDate;

    public int target;
    public int progress;
    public int step = 1;
    public String unit = "";

    public Progress(long commitDate) {
        this.commitDate = commitDate;
    }

    public Progress doProgress() {
        progress = progress + step;
        return this;
    }

    public Progress undoProgress() {
        progress = Math.max(progress - step, 0);
        return this;
    }

    // Do all the steps..
    public Progress allDone() {
        progress = target;
        return this;
    }

    protected Progress(Parcel in) {
        commitDate = in.readLong();
        target = in.readInt();
        progress = in.readInt();
        step = in.readInt();
        unit = in.readString();
    }

    public static final Creator<Progress> CREATOR = new Creator<Progress>() {
        @Override
        public Progress createFromParcel(Parcel in) {
            return new Progress(in);
        }

        @Override
        public Progress[] newArray(int size) {
            return new Progress[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(commitDate);
        dest.writeInt(target);
        dest.writeInt(progress);
        dest.writeInt(step);
        dest.writeString(unit);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
