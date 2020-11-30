package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleType implements Parcelable {
    public static final String ACTIVE_DAYS = "ScheduleType.activeDays";
    public static final String INACTIVE_DAYS = "ScheduleType.inactiveDays";
    public static final String CONTINUOUS_DAYS = "ScheduleType.continuous";
    private static final String TAG = "ScheduleType";
    private boolean type_daily;
    private boolean type_day_specified;
    private boolean type_month_specified;
    private boolean type_month_day_specified;
    private boolean type_custom;
    private boolean type_interval;

    private boolean everySaturday;
    private boolean everySunday;
    private boolean everyMonday;
    private boolean everyTuesday;
    private boolean everyWednesday;
    private boolean everyThursday;
    private boolean everyFriday;

    private boolean everyJanuary;
    private boolean everyFebruary;
    private boolean everyMarch;
    private boolean everyApril;
    private boolean everyMay;
    private boolean everyJune;
    private boolean everyJuly;
    private boolean everyAugust;
    private boolean everySeptember;
    private boolean everyOctober;
    private boolean everyNovember;
    private boolean everyDecember;

    private List<Dates> selectedDates = new ArrayList<>();
    private Interval interval;

    public ScheduleType() {
        this.type_daily = true;
    }

    public void clearAll() {
        type_daily = false;
        type_day_specified = false;
        type_month_specified = false;
        type_month_day_specified = false;
        type_custom = false;
        type_interval = false;

        everySaturday = false;
        everySunday = false;
        everyMonday = false;
        everyTuesday = false;
        everyWednesday = false;
        everyThursday = false;
        everyFriday = false;

        everyJanuary = false;
        everyFebruary = false;
        everyMarch = false;
        everyApril = false;
        everyMay = false;
        everyJune = false;
        everyJuly = false;
        everyAugust = false;
        everySeptember = false;
        everyOctober = false;
        everyNovember = false;
        everyDecember = false;

        selectedDates = new ArrayList<>();
        interval = null;
    }


    public boolean isType_daily() {
        return type_daily;
    }
    public void setType_daily(boolean type_daily) {
        this.type_daily = type_daily;
    }
    public boolean isType_day_specified() {
        return type_day_specified;
    }
    public void setType_day_specified(boolean type_daily_mod) {
        this.type_day_specified = type_daily_mod;
    }
    public boolean isType_month_specified() {
        return type_month_specified;
    }
    public void setType_month_specified(boolean type_month_specified) {
        this.type_month_specified = type_month_specified;
    }
    public boolean isType_month_day_specified() {
        return type_month_day_specified;
    }
    public void setType_month_day_specified(boolean type_month_day_specified) {
        this.type_month_day_specified = type_month_day_specified;
    }

    public boolean isType_custom() {
        return type_custom;
    }
    public void setType_custom(boolean type_custom) {
        this.type_custom = type_custom;
    }
    public boolean isType_interval() {
        return type_interval;
    }
    public void setType_interval(boolean type_interval) {
        this.type_interval = type_interval;
    }

    public boolean isEverySaturday() {
        return everySaturday;
    }
    public void setEverySaturday(boolean everySaturday) {
        this.everySaturday = everySaturday;
    }
    public boolean isEverySunday() {
        return everySunday;
    }
    public void setEverySunday(boolean everySunday) {
        this.everySunday = everySunday;
    }
    public boolean isEveryMonday() {
        return everyMonday;
    }
    public void setEveryMonday(boolean everyMonday) {
        this.everyMonday = everyMonday;
    }
    public boolean isEveryTuesday() {
        return everyTuesday;
    }
    public void setEveryTuesday(boolean everyTuesday) {
        this.everyTuesday = everyTuesday;
    }
    public boolean isEveryWednesday() {
        return everyWednesday;
    }
    public void setEveryWednesday(boolean everyWednesday) {
        this.everyWednesday = everyWednesday;
    }
    public boolean isEveryThursday() {
        return everyThursday;
    }
    public void setEveryThursday(boolean everyThursday) {
        this.everyThursday = everyThursday;
    }
    public boolean isEveryFriday() {
        return everyFriday;
    }
    public void setEveryFriday(boolean everyFriday) {
        this.everyFriday = everyFriday;
    }

    public boolean isEveryJanuary() {
        return everyJanuary;
    }
    public void setEveryJanuary(boolean everyJanuary) {
        this.everyJanuary = everyJanuary;
    }
    public boolean isEveryFebruary() {
        return everyFebruary;
    }
    public void setEveryFebruary(boolean everyFebruary) {
        this.everyFebruary = everyFebruary;
    }
    public boolean isEveryMarch() {
        return everyMarch;
    }
    public void setEveryMarch(boolean everyMarch) {
        this.everyMarch = everyMarch;
    }
    public boolean isEveryApril() {
        return everyApril;
    }
    public void setEveryApril(boolean everyApril) {
        this.everyApril = everyApril;
    }
    public boolean isEveryMay() {
        return everyMay;
    }
    public void setEveryMay(boolean everyMay) {
        this.everyMay = everyMay;
    }
    public boolean isEveryJune() {
        return everyJune;
    }
    public void setEveryJune(boolean everyJune) {
        this.everyJune = everyJune;
    }
    public boolean isEveryJuly() {
        return everyJuly;
    }
    public void setEveryJuly(boolean everyJuly) {
        this.everyJuly = everyJuly;
    }
    public boolean isEveryAugust() {
        return everyAugust;
    }
    public void setEveryAugust(boolean everyAugust) {
        this.everyAugust = everyAugust;
    }
    public boolean isEverySeptember() {
        return everySeptember;
    }
    public void setEverySeptember(boolean everySeptember) {
        this.everySeptember = everySeptember;
    }
    public boolean isEveryOctober() {
        return everyOctober;
    }
    public void setEveryOctober(boolean everyOctober) {
        this.everyOctober = everyOctober;
    }
    public boolean isEveryNovember() {
        return everyNovember;
    }
    public void setEveryNovember(boolean everyNovember) {
        this.everyNovember = everyNovember;
    }
    public boolean isEveryDecember() {
        return everyDecember;
    }
    public void setEveryDecember(boolean everyDecember) {
        this.everyDecember = everyDecember;
    }

    public List<Dates> getSelectedDates() {
        return selectedDates;
    }
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
    public void setSelectedDates(List<Dates> selectedDates) {
        this.selectedDates = selectedDates;
    }
    public void setSelectedDatesFromCalender(List<Calendar> calenders) {
        selectedDates = new ArrayList<>();
        for (int i = 0; i < calenders.size(); i++) {
            selectedDates.add(new Dates(calenders.get(i).get(Calendar.MONTH), calenders.get(i).get(Calendar.DAY_OF_MONTH)));
        }
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

    public Interval getInterval() {
        /* Required for RoomDB */
        return interval;
    }
    public void setInterval(Interval interval) {
        /* Required for RoomDB */
        this.interval = interval;
    }

    public Map<String, Integer> getIntervalData() {
        if (interval == null) return null;
        Map<String, Integer> map = new HashMap<>();
        map.put(ACTIVE_DAYS, interval.activeDays);
        map.put(INACTIVE_DAYS, interval.inactiveDays);
        if (interval.isContinuous) map.put(CONTINUOUS_DAYS, 1);
        else map.put(CONTINUOUS_DAYS, 0);
        return map;
    }
    public void setInterval(int dayLength, int interval, boolean isContinuous) {
        Interval interval1 = new Interval(dayLength, interval, isContinuous);
        setSelectedDates(interval1);
        this.interval = interval1;
    }


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
        int activeDays;
        int inactiveDays;
        boolean isContinuous;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.type_daily ? (byte) 1 : (byte) 0);
        dest.writeByte(this.type_day_specified ? (byte) 1 : (byte) 0);
        dest.writeByte(this.type_month_specified ? (byte) 1 : (byte) 0);
        dest.writeByte(this.type_month_day_specified ? (byte) 1 : (byte) 0);
        dest.writeByte(this.type_custom ? (byte) 1 : (byte) 0);
        dest.writeByte(this.type_interval ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everySaturday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everySunday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyMonday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyTuesday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyWednesday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyThursday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyFriday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyJanuary ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyFebruary ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyMarch ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyApril ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyMay ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyJune ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyJuly ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyAugust ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everySeptember ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyOctober ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyNovember ? (byte) 1 : (byte) 0);
        dest.writeByte(this.everyDecember ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.selectedDates);
        dest.writeParcelable(this.interval, flags);
    }

    protected ScheduleType(Parcel in) {
        this.type_daily = in.readByte() != 0;
        this.type_day_specified = in.readByte() != 0;
        this.type_month_specified = in.readByte() != 0;
        this.type_month_day_specified = in.readByte() != 0;
        this.type_custom = in.readByte() != 0;
        this.type_interval = in.readByte() != 0;
        this.everySaturday = in.readByte() != 0;
        this.everySunday = in.readByte() != 0;
        this.everyMonday = in.readByte() != 0;
        this.everyTuesday = in.readByte() != 0;
        this.everyWednesday = in.readByte() != 0;
        this.everyThursday = in.readByte() != 0;
        this.everyFriday = in.readByte() != 0;
        this.everyJanuary = in.readByte() != 0;
        this.everyFebruary = in.readByte() != 0;
        this.everyMarch = in.readByte() != 0;
        this.everyApril = in.readByte() != 0;
        this.everyMay = in.readByte() != 0;
        this.everyJune = in.readByte() != 0;
        this.everyJuly = in.readByte() != 0;
        this.everyAugust = in.readByte() != 0;
        this.everySeptember = in.readByte() != 0;
        this.everyOctober = in.readByte() != 0;
        this.everyNovember = in.readByte() != 0;
        this.everyDecember = in.readByte() != 0;
        this.selectedDates = in.createTypedArrayList(Dates.CREATOR);
        this.interval = in.readParcelable(Interval.class.getClassLoader());
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
}
