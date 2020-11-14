package com.example.mohaasaba.database;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;

import java.util.Calendar;

@Entity(tableName = "reminder_table")
public class Reminder implements Parcelable {
    public static final long milMinute = 60000L;
    public static final long milHour = 3600000L;
    public static final long milDay = 86400000L;
    public static final long milWeek = 604800000L;
    public static final long milMonth = 2592000000L;

    public static final int _NULL_ = -9988;
    public static final String MINUTE = "MINUTE";
    public static final String HOUR = "HOUR";
    public static final String DAY = "DAY";
    public static final String MONTH = "MONTH";

    @NonNull
    private String reminderID;
    private Calendar reminderTime;
    private int repeatingInterval = _NULL_;
    private String repeatIntervalUnit;
    private int preRemindTime = _NULL_;
    private String preReminderUnit;

    @PrimaryKey(autoGenerate = true)
    private int pendingIntentID;
    private String reminderLabel;

    public Reminder(Calendar reminderTime) {
        this.reminderID = IdGenerator.getNewID();
        this.reminderTime = reminderTime;
    }

    @NonNull
    public String getReminderID() {
        return reminderID;
    }

    public void setReminderID(String reminderID) {
        this.reminderID = reminderID;
    }

    public Calendar getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Calendar reminderTime) {
        this.reminderTime = reminderTime;
    }

    public int getRepeatingInterval() {
        return repeatingInterval;
    }

    public void setRepeatingInterval(int repeatingInterval) {
        this.repeatingInterval = repeatingInterval;
    }

    public String getRepeatIntervalUnit() {
        return repeatIntervalUnit;
    }

    public void setRepeatIntervalUnit(String repeatIntervalUnit) {
        this.repeatIntervalUnit = repeatIntervalUnit;
    }

    public int getPreRemindTime() {
        return preRemindTime;
    }

    public void setPreRemindTime(int preRemindTime) {
        this.preRemindTime = preRemindTime;
    }

    public String getPreReminderUnit() {
        return preReminderUnit;
    }

    public void setPreReminderUnit(String preReminderUnit) {
        this.preReminderUnit = preReminderUnit;
    }

    public int getPendingIntentID() {
        return pendingIntentID;
    }

    public void setPendingIntentID(int pendingIntentID) {
        this.pendingIntentID = pendingIntentID;
    }

    public String getReminderLabel() {
        return reminderLabel;
    }

    public void setReminderLabel(String reminderLabel) {
        this.reminderLabel = reminderLabel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reminderID);
        dest.writeSerializable(this.reminderTime);
        dest.writeInt(this.repeatingInterval);
        dest.writeString(this.repeatIntervalUnit);
        dest.writeInt(this.preRemindTime);
        dest.writeString(this.preReminderUnit);
        dest.writeInt(this.pendingIntentID);
        dest.writeString(this.reminderLabel);
    }

    protected Reminder(Parcel in) {
        this.reminderID = in.readString();
        this.reminderTime = (Calendar) in.readSerializable();
        this.repeatingInterval = in.readInt();
        this.repeatIntervalUnit = in.readString();
        this.preRemindTime = in.readInt();
        this.preReminderUnit = in.readString();
        this.pendingIntentID = in.readInt();
        this.reminderLabel = in.readString();
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel source) {
            return new Reminder(source);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}
