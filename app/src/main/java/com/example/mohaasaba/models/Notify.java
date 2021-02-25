package com.example.mohaasaba.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;

public class Notify implements Parcelable {
    public static final long MINUTE = 60 * 1000;
    public static final long HOUR = 60 * 60 * 1000;
    public enum Priority {
        DEFAULT,
        LOW,
        MEDIUM,
        HIGH
    }

    public String notifyID;
    public String label;
    public String message;
    public int notificationHour;
    public int notificationMinute;

    public int beforeMinute;
    public int repeatMinute;

    public int uniqueID;  // must be unique; Pending Intent ID
    public Priority priority;

    @NonNull
    @Embedded
    public ScheduleType scheduleType;

    public Notify(int notificationHour, int notificationMinute) {
        this.notifyID = IdGenerator.getNewID();
        this.notificationHour = notificationHour;
        this.notificationMinute = notificationMinute;
        this.priority = Priority.DEFAULT;
        this.uniqueID = IdGenerator.getShortID();
        this.message = "";
        this.scheduleType = new ScheduleType();
    }

    protected Notify(Parcel in) {
        label = in.readString();
        message = in.readString();
        notificationHour = in.readInt();
        notificationMinute = in.readInt();
        beforeMinute = in.readInt();
        repeatMinute = in.readInt();
        uniqueID = in.readInt();
        priority = Priority.values()[in.readInt()];
        notifyID = in.readString();
        scheduleType = in.readParcelable(ScheduleType.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(message);
        dest.writeInt(notificationHour);
        dest.writeInt(notificationMinute);
        dest.writeInt(beforeMinute);
        dest.writeInt(repeatMinute);
        dest.writeInt(uniqueID);
        dest.writeInt(priority.ordinal());
        dest.writeString(notifyID);
        dest.writeParcelable(this.scheduleType, flags);
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
                " Title " + label +
                " Message: " + message +
                " Hour: " + notificationHour +
                " Minute: " + notificationMinute +
                " Before: " + beforeMinute +
                " Interval: " + repeatMinute;
    }
}
