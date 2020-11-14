package com.example.mohaasaba.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DataConverter {
    @TypeConverter
    public String fromStringList(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.toJson(stringList,type);
    }

    @TypeConverter
    public List<String> toStringList(String json) {
        if (json == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static String joinListToString(List<String> stringList) {
        String tagString = stringList.get(0);
        for (int i = 1; i < stringList.size(); i++) {
            tagString += "," + stringList.get(i);
        }
        return tagString;
    }

    public static List<String> splitStringToList(String string) {
        List<String> stringList = new ArrayList<>(Arrays.asList(string.split(",")));
        /* To know how it works visit
         * https://www.java67.com/2016/01/how-to-split-string-by-comma-in-java-with-example.html
         * */

        // removing white spaces from list items
        for (int i = 0; i < stringList.size(); i++) {
            stringList.set(i,stringList.get(i).trim());
        }
        return stringList;
    }

    @TypeConverter
    public static String fromCalender(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Calendar>(){}.getType();
        return gson.toJson(calendar,type);
    }

    @TypeConverter
    public static Calendar toCalender(String json) {
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Calendar>(){}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String fromDatesListToString(List<ScheduleType.Dates> datesList) {
        if (datesList == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<ScheduleType.Dates>>(){}.getType();
        return gson.toJson(datesList,type);
    }

    @TypeConverter
    public static List<ScheduleType.Dates> fromStringToDatesList(String string) {
        if (string == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<ScheduleType.Dates>>(){}.getType();
        return gson.fromJson(string,type);
    }

    @TypeConverter
    public static History fromStringToHistory(String string) {
        if (string == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<History>(){}.getType();

        return gson.fromJson(string, type);
    }
    @TypeConverter
    public static String fromHistoryToString(History history) {
        if (history == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<History>(){}.getType();

        return gson.toJson(history, type);
    }


    @TypeConverter
    public static ScheduleType.Interval fromStringToInterval(String string) {
        if (string == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<ScheduleType.Interval>(){}.getType();

        return gson.fromJson(string, type);
    }

    @TypeConverter
    public static String fromIntervalToString (ScheduleType.Interval interval) {
        if (interval == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<ScheduleType.Interval>(){}.getType();

        return gson.toJson(interval, type);
    }
}
