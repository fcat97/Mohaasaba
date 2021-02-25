package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleType implements Parcelable, Serializable {
    private static final String TAG = "ScheduleType";

    public enum Type {
        WeekDays,
        CustomDates,
        Intervals
    }

    public Type type;

    public boolean everySaturday;
    public boolean everySunday;
    public boolean everyMonday;
    public boolean everyTuesday;
    public boolean everyWednesday;
    public boolean everyThursday;
    public boolean everyFriday;

    private List<Dates> selectedDates = new ArrayList<>();
    private Interval interval;


    public int startingMinute; // starting minute of day; default 0
    public int endingMinute; // ending minute of day; default 24*60-1

    public boolean disposable = false;
    public long disposeTime = -1001; // calender.timeInMillisecond();

    public ScheduleType() {
        this.type = Type.WeekDays;
        this.everySaturday = true;
        this.everySunday = true;
        this.everyMonday = true;
        this.everyTuesday = true;
        this.everyWednesday = true;
        this.everyThursday = true;
        this.everyFriday = true;

        this.startingMinute = 0;
        this.endingMinute = 24*60 - 1;
    }

    public void initialize() {
        this.type = Type.WeekDays;

        this.everySaturday = true;
        this.everySunday = true;
        this.everyMonday = true;
        this.everyTuesday = true;
        this.everyWednesday = true;
        this.everyThursday = true;
        this.everyFriday = true;

        this.selectedDates = new ArrayList<>();
        this.interval = null;

        this.startingMinute = 0;
        this.endingMinute = 24*60 - 1;

        this.disposable = false;
        this.disposeTime = -1001; // randomTime with negative value
    }

    // Class related Getter and Setter -------------------------------------------------------------
    public List<Dates> getSelectedDates() {
        return selectedDates;
    }
    public void setSelectedDates(List<Dates> selectedDates) {
        this.selectedDates = selectedDates;
    }
    public Interval getInterval() {
        /* Required for RoomDB */
        // Do not edit this method //
        return interval;
    }
    public void setInterval(Interval interval) {
        /* Required for RoomDB */
        // Do not edit this method //
        this.interval = interval;
    }
    private void setSelectedDates(Interval interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR,2020); /* To get february, 29; a leap year selected */
        selectedDates = new ArrayList<>();

        int j = 0;
        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_YEAR); i++) {
            calendar.set(Calendar.DAY_OF_YEAR, i);
            if (j < interval.activeDays) selectedDates.add(new Dates(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
            j ++;
            if (j == interval.activeDays + interval.inactiveDays) j = 0;
        }
    }

    // Methods to be used outside ------------------------------------------------------------------
    public List<Calendar> getSelectedDatesAsCalender() {
        List<Calendar> calendars = new ArrayList<>();

        for (Dates selected: selectedDates
             ) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            calendar.clear();
            calendar.set(year, selected.month, selected.dayOfMonth);
            calendars.add(calendar);
        }

        return calendars;
    }
    public void setSelectedDatesFromCalender(List<Calendar> calenders) {
        selectedDates = new ArrayList<>();
        for (int i = 0; i < calenders.size(); i++) {
            selectedDates.add(new Dates(calenders.get(i).get(Calendar.MONTH), calenders.get(i).get(Calendar.DAY_OF_MONTH)));
        }
    }
    public boolean isToday() {
        if (disposable && disposeTime < Calendar.getInstance().getTimeInMillis()) return false;
        else if (type == Type.WeekDays) {
            Calendar calendar = Calendar.getInstance();
            int d_of_week = calendar.get(Calendar.DAY_OF_WEEK);
            Log.d(TAG, "isToday: called");
            Log.d(TAG, "isToday: day of week " + d_of_week);

            if (d_of_week == Calendar.SUNDAY && everySunday) return true;
            else if (d_of_week == Calendar.MONDAY && everyMonday) return true;
            else if (d_of_week == Calendar.TUESDAY && everyTuesday) return true;
            else if (d_of_week == Calendar.WEDNESDAY && everyWednesday) return true;
            else if (d_of_week == Calendar.THURSDAY && everyThursday) return true;
            else if (d_of_week == Calendar.FRIDAY && everyFriday) return true;
            else if (d_of_week == Calendar.SATURDAY && everySaturday) return true;
            else return false;
        }
        else {
            // Check if any selectedDates contains today or not
            Calendar c = Calendar.getInstance();
            boolean state = false;
            for (Dates d : selectedDates) {
                if (d.month == c.get(Calendar.MONTH) && d.dayOfMonth == c.get(Calendar.DAY_OF_MONTH)) {
                    state = true;
                    break;
                }
            }
            return state;
        }
    }

    public void setInterval(int dayLength, int interval, boolean isContinuous) {
        Interval interval1 = new Interval(dayLength, interval, isContinuous);
        setSelectedDates(interval1);
        this.interval = interval1;
    }


    // Inner Classes -------------------------------------------------------------------------------
    public static class Dates implements Parcelable, Serializable {
        int month; /* Month is in format of Calendar.MONTH i.e. January = 0*/
        int dayOfMonth;

        public Dates(int month, int dayOfMonth) {
            this.month = month;
            this.dayOfMonth = dayOfMonth;
        }
        public Dates(Calendar calendar) {
            this.month = calendar.get(Calendar.MONTH);
            this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null) return false;
            if (dayOfMonth != ((Dates) obj).dayOfMonth) return false;
            else if (month != ((Dates)obj).month) return false;
            else return super.equals(obj);
        }

        @NonNull
        @Override
        public String toString() {
            return "{\"dayOfMonth\":" + dayOfMonth + ",\"month\":" + month + "}";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.month);
            dest.writeInt(this.dayOfMonth);
        }

        protected Dates(Parcel in) {
            this.month = in.readInt();
            this.dayOfMonth = in.readInt();
        }

        public static final Creator<Dates> CREATOR = new Creator<Dates>() {
            @Override
            public Dates createFromParcel(Parcel source) {
                return new Dates(source);
            }

            @Override
            public Dates[] newArray(int size) {
                return new Dates[size];
            }
        };
    }
    public static class Interval implements Parcelable {
        public int activeDays;
        public int inactiveDays;
        public boolean isContinuous;

        public Interval(int activeDays, int inactiveDays, boolean isContinuous) {
            this.activeDays = activeDays;
            this.inactiveDays = inactiveDays;
            this.isContinuous = isContinuous;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.activeDays);
            dest.writeInt(this.inactiveDays);
            dest.writeByte(this.isContinuous ? (byte) 1 : (byte) 0);
        }

        protected Interval(Parcel in) {
            this.activeDays = in.readInt();
            this.inactiveDays = in.readInt();
            this.isContinuous = in.readByte() != 0;
        }

        public static final Creator<Interval> CREATOR = new Creator<Interval>() {
            @Override
            public Interval createFromParcel(Parcel source) {
                return new Interval(source);
            }

            @Override
            public Interval[] newArray(int size) {
                return new Interval[size];
            }
        };
    }


    // Parcelable Classes and Methods --------------------------------------------------------------
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type.ordinal());
        dest.writeByte(this.everySaturday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everySunday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyMonday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyTuesday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyWednesday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyThursday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyFriday ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.selectedDates);
        dest.writeParcelable(this.interval, flags);
        dest.writeInt(this.startingMinute);
        dest.writeInt(this.endingMinute);
        dest.writeByte(this.disposable ? (byte) 1 : (byte) 0);
        dest.writeLong(this.disposeTime);
    }

    protected ScheduleType(Parcel in) {
        this.type = Type.values()[in.readInt()];
        this.everySaturday = in.readByte() != 0;
        this.everySunday = in.readByte() != 0;
        this.everyMonday = in.readByte() != 0;
        this.everyTuesday = in.readByte() != 0;
        this.everyWednesday = in.readByte() != 0;
        this.everyThursday = in.readByte() != 0;
        this.everyFriday = in.readByte() != 0;
        this.selectedDates = in.createTypedArrayList(Dates.CREATOR);
        this.interval = in.readParcelable(Interval.class.getClassLoader());
        this.startingMinute = in.readInt();
        this.endingMinute = in.readInt();
        this.disposable = in.readByte() != 0;
        this.disposeTime = in.readLong();
    }

    public static final Creator<ScheduleType> CREATOR = new Creator<ScheduleType>() {
        @Override
        public ScheduleType createFromParcel(Parcel source) {
            return new ScheduleType(source);
        }

        @Override
        public ScheduleType[] newArray(int size) {
            return new ScheduleType[size];
        }
    };

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
