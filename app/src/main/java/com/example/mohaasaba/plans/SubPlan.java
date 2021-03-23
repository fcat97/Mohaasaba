package com.example.mohaasaba.plans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class SubPlan implements Serializable, Parcelable {
    public String label;

    public boolean track_time = false;
    public boolean track_count = true;

    public long time_goal = 0;
    public long time_progress;
    public long time_offset;

    public int count_goal = 1;
    public int count_step;
    public int count_offset;
    public int count_progress;

    public SubPlan() {
        // Empty Constructor
    }
    /**
     * Make a deep copy
     * @param planToCopy over which deep copy made
     */
    public SubPlan(SubPlan planToCopy) {
        this.label = planToCopy.label;

        this.track_time = planToCopy.track_time;
        this.track_count = planToCopy.track_count;

        this.time_goal = planToCopy.time_goal;
        this.time_progress = planToCopy.time_progress;
        this.time_offset = planToCopy.time_offset;

        this.count_goal = planToCopy.count_goal;
        this.count_step = planToCopy.count_step;
        this.count_offset = planToCopy.count_offset;
        this.count_progress = planToCopy.count_progress;
    }

    protected SubPlan(Parcel in) {
        label = in.readString();
        track_time = in.readByte() != 0;
        track_count = in.readByte() != 0;
        time_goal = in.readLong();
        time_progress = in.readLong();
        time_offset = in.readLong();
        count_goal = in.readInt();
        count_step = in.readInt();
        count_offset = in.readInt();
        count_progress = in.readInt();
    }

    public static final Creator<SubPlan> CREATOR = new Creator<SubPlan>() {
        @Override
        public SubPlan createFromParcel(Parcel in) {
            return new SubPlan(in);
        }

        @Override
        public SubPlan[] newArray(int size) {
            return new SubPlan[size];
        }
    };

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        SubPlan newPlan = (SubPlan) obj;
        return this.label.equals(newPlan.label)
                && this.track_time == newPlan.track_time
                && this.track_count == newPlan.track_count
                && this.time_goal == newPlan.time_goal
                && this.time_offset == newPlan.time_offset
                && this.time_progress == newPlan.time_progress
                && this.count_goal == newPlan.count_goal
                && this.count_offset == newPlan.count_offset
                && this.count_progress == newPlan.count_progress
                && this.count_step == newPlan.count_step;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeByte((byte) (track_time ? 1 : 0));
        dest.writeByte((byte) (track_count ? 1 : 0));
        dest.writeLong(time_goal);
        dest.writeLong(time_progress);
        dest.writeLong(time_offset);
        dest.writeInt(count_goal);
        dest.writeInt(count_step);
        dest.writeInt(count_offset);
        dest.writeInt(count_progress);
    }
}
