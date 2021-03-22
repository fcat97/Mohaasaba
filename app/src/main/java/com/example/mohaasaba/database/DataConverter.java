package com.example.mohaasaba.database;

import androidx.room.TypeConverter;

import com.example.mohaasaba.ibadah.bookshelf.Book;
import com.example.mohaasaba.ibadah.tasbih.Tasbih;
import com.example.mohaasaba.models.History;
import com.example.mohaasaba.models.Notify;
import com.example.mohaasaba.models.ProgressHistory;
import com.example.mohaasaba.models.ScheduleType;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.plans.PlanPeriod;
import com.example.mohaasaba.plans.SubPlan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
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

    @TypeConverter
    public static List<Notify> fromStringToNotifyList(String string) {
        if (string == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Notify>>(){}.getType();

        return gson.fromJson(string, type);
    }

    @TypeConverter
    public static String fromNotifyToString (List<Notify> notifyList) {
        if (notifyList == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Notify>>(){}.getType();

        return gson.toJson(notifyList, type);
    }

    @TypeConverter
    public static String fromEnumToString(ScheduleType.Type type) {
        if (type == null) return null;
        Gson gson = new Gson();
        Type classType = new TypeToken<ScheduleType.Type>(){}.getType();

        return gson.toJson(type, classType);
    }

    @TypeConverter
    public static ScheduleType.Type fromStringToType(String string) {
        if (string == null ) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<ScheduleType.Type>(){}.getType();

        return gson.fromJson(string, type);
    }

    @TypeConverter
    public static String fromTransactionToString(Transaction transaction) {
        if (transaction == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<Transaction>(){}.getType();

        return gson.toJson(transaction, type);
    }

    @TypeConverter
    public static Transaction fromStringToTransaction(String string) {
        if (string == null ) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<Transaction>(){}.getType();

        return gson.fromJson(string, type);
    }

    @TypeConverter
    public static List<Transaction> fromStringToTransactionList(String string) {
        if (string == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Transaction>>(){}.getType();

        return gson.fromJson(string, type);
    }

    @TypeConverter
    public static String fromTransactionListToString(List<Transaction> transactionList) {
        if (transactionList == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Transaction>>(){}.getType();

        return gson.toJson(transactionList, type);
    }

    @TypeConverter
    public static ProgressHistory fromStringToProgressHistory(String string) {
        if (string == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<ProgressHistory>(){}.getType();

        return gson.fromJson(string, type);
    }

    @TypeConverter
    public static String fromProgressHistoryToString(ProgressHistory progressHistory) {
        if (progressHistory == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<ProgressHistory>(){}.getType();

        return gson.toJson(progressHistory, type);
    }

    @TypeConverter
    public static Book.ReadingStatus fromStringToReadingStatus(String string) {
        if (string == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<Book.ReadingStatus>(){}.getType();

        return gson.fromJson(string, type);
    }

    @TypeConverter
    public static String fromReadingStatusToString(Book.ReadingStatus readingStatus) {
        if (readingStatus == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<Book.ReadingStatus>(){}.getType();

        return gson.toJson(readingStatus, type);
    }

    @TypeConverter
    public static Notify.Priority fromStringToNotifyPriority(String string) {
        if (string == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<Notify.Priority>(){}.getType();

        return gson.fromJson(string, type);
    }

    @TypeConverter
    public static String fromNotifyPriorityToString(Notify.Priority priority) {
        if (priority == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<Notify.Priority>(){}.getType();

        return gson.toJson(priority, type);
    }

    @TypeConverter
    public static Tasbih.TasbihType fromStringToTasbihType(String string) {
        if (string == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<Tasbih.TasbihType>(){}.getType();

        return gson.fromJson(string, type);
    }

    @TypeConverter
    public static String fromTasbihTypeToString(Tasbih.TasbihType tasbihType) {
        if (tasbihType == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<Tasbih.TasbihType>(){}.getType();

        return gson.toJson(tasbihType, type);
    }

    @TypeConverter
    public static String fromPlanPeriod(PlanPeriod planPeriod) {
        if (planPeriod == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<PlanPeriod>(){}.getType();

        return gson.toJson(planPeriod, type);
    }

    @TypeConverter
    public static PlanPeriod toPlanPeriod(String json) {
        if (json == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<PlanPeriod>(){}.getType();

        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String fromSubPlanHash(HashMap<Long, List<SubPlan>> hashMap) {
        if (hashMap == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<Long, List<SubPlan>>>(){}.getType();

        return gson.toJson(hashMap, type);
    }

    @TypeConverter
    public static HashMap<Long, List<SubPlan>> toSubPlanHash(String json) {
        if (json == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<Long, List<SubPlan>>>(){}.getType();

        return gson.fromJson(json, type);
    }
}
