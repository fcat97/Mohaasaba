package com.example.mohaasaba.ibadah.tasbih;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;
import com.example.mohaasaba.models.Notify;
import com.example.mohaasaba.models.Progress;
import com.example.mohaasaba.models.ProgressHistory;
import com.example.mohaasaba.models.ScheduleType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity(tableName = "tasbih_table")
public class Tasbih implements Serializable, Parcelable {
    public enum TasbihType {
        MUSTAHAB,
        AFTER_SALAT,
        BEFORE_SLEEP,
        MORNING_TASBIH,
        EVENING_TASBIH
    }

    @PrimaryKey @NonNull
    public String tasbihID;
    public String label;
    public String reward;
    public String doa_ar;
    public String doa_bn;
    public String references;
    public String tags;
    public TasbihType tasbihType;

    public ProgressHistory history;

    @Embedded
    public ScheduleType scheduleType;
    public List<Notify> notifyList;

    public Tasbih() {
        this.tasbihID = IdGenerator.getNewID();
        this.label = "";
        this.doa_ar = "";
        this.doa_bn = "";
        this.reward = "";
        this.references = "";
        this.tags = "";
        this.history = new ProgressHistory();
        this.scheduleType = new ScheduleType();
        this.notifyList = new ArrayList<>();
        this.tasbihType = TasbihType.MUSTAHAB;

        // Initialize daily target to 1
        Progress p = history.getProgress(Calendar.getInstance());
        p.target = 1;
        history.commitProgress(p, Calendar.getInstance());
    }


    protected Tasbih(Parcel in) {
        tasbihID = in.readString();
        label = in.readString();
        reward = in.readString();
        doa_ar = in.readString();
        doa_bn = in.readString();
        references = in.readString();
        tags = in.readString();
        history = in.readParcelable(ProgressHistory.class.getClassLoader());
        scheduleType = in.readParcelable(ScheduleType.class.getClassLoader());
        notifyList = in.createTypedArrayList(Notify.CREATOR);
        tasbihType = TasbihType.values()[in.readInt()];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tasbihID);
        dest.writeString(label);
        dest.writeString(reward);
        dest.writeString(doa_ar);
        dest.writeString(doa_bn);
        dest.writeString(references);
        dest.writeString(tags);
        dest.writeParcelable(history, flags);
        dest.writeParcelable(scheduleType, flags);
        dest.writeTypedList(notifyList);
        dest.writeInt(tasbihType.ordinal());
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

    // serializable methods -----------------------------------------------------------------------

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Tasbih other = (Tasbih) obj;
        return this.tasbihID.equals(other.tasbihID)
                && this.label.equals(other.label)
                && this.reward.equals(other.reward)
                && this.doa_ar.equals(other.doa_ar)
                && this.doa_bn.equals(other.doa_bn)
                && this.references.equals(other.references)
                && this.tags.equals(other.tags)
                && this.tasbihType.equals(other.tasbihType)
                && this.history.equals(other.history)
                && this.scheduleType.equals(other.scheduleType)
                && this.notifyList.equals(other.notifyList);
    }
}
