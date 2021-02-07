package com.example.mohaasaba.bookshelf;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;
import com.example.mohaasaba.models.ProgressHistory;
import com.example.mohaasaba.models.ScheduleType;

import java.util.Calendar;

@Entity(tableName = "bookshelf")
public class Book {
    public enum ReadingStatus {
        READING,
        COLLECTED,
        WISH_LISTED
    }


    @NonNull
    @PrimaryKey
    public String bookID;

    public String title;
    public String author = "";
    public String publication = "";
    public int totalPages = 0;
    public float price = 0;
    public String owner = "";
    public long purchaseTime;

    @Embedded public ScheduleType scheduleType;
    public ProgressHistory readingHistory;
    public ReadingStatus readingStatus;

    public Book(String title) {
        this.bookID = IdGenerator.getNewID();
        this.title = title;
        this.purchaseTime = Calendar.getInstance().getTimeInMillis();
        this.scheduleType = new ScheduleType();
        this.readingHistory = new ProgressHistory();
        this.readingStatus = ReadingStatus.WISH_LISTED;
    }
}
