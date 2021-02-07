package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Progress implements Parcelable, Serializable {
    private static final String TAG = Progress.class.getCanonicalName();
    public long commitDate;

    public int maxProgress;
    public int currentProgress;
    public int progressStep;
    public String progressUnit = "";

    public Progress(long commitDate) {
        this.commitDate = commitDate;
    }

    public Progress doProgress() {
        currentProgress = currentProgress + 1;
        return this;
    }

    protected Progress(Parcel in) {
        commitDate = in.readLong();
        maxProgress = in.readInt();
        currentProgress = in.readInt();
        progressStep = in.readInt();
        progressUnit = in.readString();
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
        dest.writeInt(maxProgress);
        dest.writeInt(currentProgress);
        dest.writeInt(progressStep);
        dest.writeString(progressUnit);
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
