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

    private String title;
    private String note;
    private List<String> tags = new ArrayList<>();
    private List<Notify> notifyList = new ArrayList<>();
    private History history = new History();
    private int themeID = -1001;
    private int priority;

    public Schedule(String title) {
        this.scheduleID = IdGenerator.getNewID();
        this.title = title;
        this.setScheduleType(new ScheduleType());
        this.priority = 1;
        this.note = "";
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
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
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
    public List<Notify> getNotifyList() {
        return notifyList;
    }
    public void setNotifyList(List<Notify> notifyList) {
        this.notifyList = notifyList;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Schedule duplicate() {
        Schedule item = new Schedule(title);
        item.setScheduleType(getScheduleType());
        item.setNote(getNote());
        item.setTags(getTags());
        item.setNotifyList(getNotifyList());
        item.setHistory(getHistory().duplicate());  // Get New History with existing tasksList
        item.setThemeID(getThemeID());
        item.setPriority(getPriority());

        return item;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.scheduleID);
        dest.writeParcelable(this.scheduleType, flags);
        dest.writeString(this.title);
        dest.writeString(this.note);
        dest.writeStringList(this.tags);
        dest.writeParcelable(this.history, flags);
        dest.writeInt(this.themeID);
        dest.writeTypedList(this.notifyList);
        dest.writeInt(this.priority);
    }

    protected Schedule(Parcel in) {
        this.scheduleID = in.readString();
        this.scheduleType = in.readParcelable(ScheduleType.class.getClassLoader());
        this.title = in.readString();
        this.note = in.readString();
        this.tags = in.createStringArrayList();
        this.history = in.readParcelable(History.class.getClassLoader());
        this.themeID = in.readInt();
        this.notifyList = in.createTypedArrayList(Notify.CREATOR);
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
