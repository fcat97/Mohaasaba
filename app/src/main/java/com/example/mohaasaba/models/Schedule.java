package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "schedule_table")
public class Schedule implements Parcelable {

    @NonNull
    @PrimaryKey
    private String scheduleID;

    @Embedded
    private ScheduleType scheduleType;

    private List<String> subSchedulesID = new ArrayList<>();
    private String title;
    private String noteID;
    private List<String> tags = new ArrayList<>();
    private String renewInterval;
    private List<Notify> notifyList = new ArrayList<>();
    private History history = new History();
    private int themeID = -1001;
    private int startingMinute; // starting minute of day; default 0
    private int endingMinute; // ending minute of day; default 24*60-1
    private int priority;

    public Schedule(String title) {
        this.scheduleID = IdGenerator.getNewID();
        this.title = title;
        this.setScheduleType(new ScheduleType());
        this.startingMinute = 0;
        this.endingMinute = 24*60 - 1;
        this.priority = 1;
    }

    @NonNull
    public String getScheduleID() {
        return scheduleID;
    }
    public void setScheduleID(@NonNull String scheduleID) {
        this.scheduleID = scheduleID;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getNoteID() {
        return noteID;
    }
    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public String getRenewInterval() {
        return renewInterval;
    }
    public void setRenewInterval(String renewInterval) {
        this.renewInterval = renewInterval;
    }
    public ScheduleType getScheduleType() {
        return scheduleType;
    }
    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }
    public History getHistory() {
        return history;
    }
    public void setHistory(History history) {
        this.history = history;
    }
    public int getThemeID() {
        return themeID;
    }
    public void setThemeID(int themeID) {
        this.themeID = themeID;
    }
    public List<String> getSubSchedulesID() {
        return subSchedulesID;
    }
    public void setSubSchedulesID(List<String> subSchedulesID) {
        this.subSchedulesID = subSchedulesID;
    }
    public List<Notify> getNotifyList() {
        return notifyList;
    }
    public void setNotifyList(List<Notify> notifyList) {
        this.notifyList = notifyList;
    }
    public void setStartingMinute(int startingMinute) {
        this.startingMinute = startingMinute;
    }
    public int getStartingMinute() {
        return startingMinute;
    }
    public int getEndingMinute() {
        return endingMinute;
    }
    public void setEndingMinute(int endingMinute) {
        this.endingMinute = endingMinute;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.scheduleID);
        dest.writeParcelable(this.scheduleType, flags);
        dest.writeStringList(this.subSchedulesID);
        dest.writeString(this.title);
        dest.writeString(this.noteID);
        dest.writeStringList(this.tags);
        dest.writeString(this.renewInterval);
        dest.writeParcelable(this.history, flags);
        dest.writeInt(this.themeID);
        dest.writeTypedList(this.notifyList);
        dest.writeInt(this.startingMinute);
        dest.writeInt(this.endingMinute);
        dest.writeInt(this.priority);
    }

    protected Schedule(Parcel in) {
        this.scheduleID = in.readString();
        this.scheduleType = in.readParcelable(ScheduleType.class.getClassLoader());
        this.subSchedulesID = in.createStringArrayList();
        this.title = in.readString();
        this.noteID = in.readString();
        this.tags = in.createStringArrayList();
        this.renewInterval = in.readString();
        this.history = in.readParcelable(History.class.getClassLoader());
        this.themeID = in.readInt();
        this.notifyList = in.createTypedArrayList(Notify.CREATOR);
        this.startingMinute = in.readInt();
        this.endingMinute = in.readInt();
        this.priority = in.readInt();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel source) {
            return new Schedule(source);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };
}
