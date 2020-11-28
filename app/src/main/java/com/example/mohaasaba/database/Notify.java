package com.example.mohaasaba.database;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.mohaasaba.helper.IdGenerator;
import com.example.mohaasaba.helper.NotifyHelper;
import com.example.mohaasaba.receivers.NotyFire;

public class Notify implements Parcelable {
    public static final long MINUTE = 60 * 1000;
    public static final long HOUR = 60 * 60 * 1000;
    public enum Priority {
        DEFAULT,
        LOW,
        MEDIUM,
        HIGH
    }

    public String message;
    public int notificationHour;
    public int notificationMinute;

    public int beforeMinute;
    public int repeatMinute;

    public int uniqueID;  // must be unique; Pending Intent ID
    public Priority priority;

    public Notify(int notificationHour, int notificationMinute) {
        this.notificationHour = notificationHour;
        this.notificationMinute = notificationMinute;
        this.priority = Priority.DEFAULT;
        this.uniqueID = IdGenerator.getShortID();
        this.message = " ";
    }

    protected Notify(Parcel in) {
        message = in.readString();
        notificationHour = in.readInt();
        notificationMinute = in.readInt();
        beforeMinute = in.readInt();
        repeatMinute = in.readInt();
        uniqueID = in.readInt();
        priority = Priority.values()[in.readInt()];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeInt(notificationHour);
        dest.writeInt(notificationMinute);
        dest.writeInt(beforeMinute);
        dest.writeInt(repeatMinute);
        dest.writeInt(uniqueID);
        dest.writeInt(priority.ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notify> CREATOR = new Creator<Notify>() {
        @Override
        public Notify createFromParcel(Parcel in) {
            return new Notify(in);
        }

        @Override
        public Notify[] newArray(int size) {
            return new Notify[size];
        }
    };

    public long getMinuteInMillis() { return this.notificationMinute * MINUTE; }
    public long getHourInMillis() { return this.notificationHour * HOUR; }

    @NonNull
    @Override
    public String toString() {
        return " unique_ID: " + uniqueID +
                " Message: " + message +
                " Hour: " + notificationHour +
                " Minute: " + notificationMinute +
                " Before: " + beforeMinute +
                " Interval: " + repeatMinute;
    }
}
