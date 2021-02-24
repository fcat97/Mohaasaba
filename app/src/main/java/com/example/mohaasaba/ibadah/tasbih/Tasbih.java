package com.example.mohaasaba.ibadah.tasbih;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;
import com.example.mohaasaba.models.History;
import com.example.mohaasaba.models.Notify;
import com.example.mohaasaba.models.ScheduleType;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "tasbih_table")
public class Tasbih implements Parcelable {
    @PrimaryKey @NonNull
    public String tasbihID;
    public String label;
    public String reward;
    public String hadith_ar;
    public String hadith_bn;
    public String references;
    public String tags;

    public History history;

    @Embedded
    public ScheduleType scheduleType;
    public List<Notify> notifyList;

    public Tasbih() {
        this.tasbihID = IdGenerator.getNewID();
        this.label = "";
        this.hadith_ar = "";
        this.hadith_bn = "";
        this.reward = "";
        this.references = "";
        this.tags = "";
        this.history = new History();
        this.scheduleType = new ScheduleType();
        this.notifyList = new ArrayList<>();
    }


    protected Tasbih(Parcel in) {
        tasbihID = in.readString();
        label = in.readString();
        reward = in.readString();
        hadith_ar = in.readString();
        hadith_bn = in.readString();
        references = in.readString();
        tags = in.readString();
        history = in.readParcelable(History.class.getClassLoader());
        scheduleType = in.readParcelable(ScheduleType.class.getClassLoader());
        notifyList = in.createTypedArrayList(Notify.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tasbihID);
        dest.writeString(label);
        dest.writeString(reward);
        dest.writeString(hadith_ar);
        dest.writeString(hadith_bn);
        dest.writeString(references);
        dest.writeString(tags);
        dest.writeParcelable(history, flags);
        dest.writeParcelable(scheduleType, flags);
        dest.writeTypedList(notifyList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tasbih> CREATOR = new Creator<Tasbih>() {
        @Override
        public Tasbih createFromParcel(Parcel in) {
            return new Tasbih(in);
        }

        @Override
        public Tasbih[] newArray(int size) {
            return new Tasbih[size];
        }
    };
}
