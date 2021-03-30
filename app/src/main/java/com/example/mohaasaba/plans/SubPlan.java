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
    public int count_step = 1;
    public int count_offset;
    public int count_progress;
    public String count_unit = "";

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
        this.count_unit = planToCopy.count_unit;
    }

    /**
     * Increment Countable Progress
     */
    public void incrementCount() {
        if (track_count) {
            count_progress = Math.min(count_progress + count_step, count_goal);
        }
    }

    /**
     * Decrement Countable Progress
     */
    public void decrementCount() {
        if (track_count) {
            count_progress = Math.max(count_progress - count_step, 0);
        }
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
        count_unit = in.readString();
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
        SubPlan other = (SubPlan) obj;
        return this.label.equals(other.label)
                && this.track_time == other.track_time
                && this.track_count == other.track_count
                && this.time_goal == other.time_goal
                && this.time_offset == other.time_offset
                && this.time_progress == other.time_progress
                && this.count_goal == other.count_goal
                && this.count_offset == other.count_offset
                && this.count_progress == other.count_progress
                && this.count_step == other.count_step
                && this.count_unit.equals(other.count_unit);
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
        dest.writeString(count_unit);
    }
}
