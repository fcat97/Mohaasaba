package com.example.mohaasaba.ibadah.bookshelf;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;
import com.example.mohaasaba.models.Notify;
import com.example.mohaasaba.models.ProgressHistory;
import com.example.mohaasaba.models.ScheduleType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity(tableName = "bookshelf")
public class Book implements Parcelable {
    public enum ReadingStatus {
        READING,
        READ,
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
    public List<Notify> notifyList = new ArrayList<>();
    public boolean hardCopyCollected = false;
    public boolean softCopyCollected = false;
    public String hardCopyLocation = "";
    public String softCopyLocation = "";

    public Book(String title) {
        this.bookID = IdGenerator.getNewID();
        this.title = title;
        this.purchaseTime = Calendar.getInstance().getTimeInMillis();
        this.scheduleType = new ScheduleType();
        this.readingHistory = new ProgressHistory();
        this.readingStatus = ReadingStatus.WISH_LISTED;
    }

    protected Book(Parcel in) {
        bookID = in.readString();
        title = in.readString();
        author = in.readString();
        publication = in.readString();
        totalPages = in.readInt();
        price = in.readFloat();
        owner = in.readString();
        purchaseTime = in.readLong();
        scheduleType = in.readParcelable(ScheduleType.class.getClassLoader());
        readingHistory = in.readParcelable(ProgressHistory.class.getClassLoader());
        readingStatus = ReadingStatus.values()[in.readInt()];
        notifyList = in.createTypedArrayList(Notify.CREATOR);
        hardCopyCollected = in.readByte() != 0;
        softCopyCollected = in.readByte() != 0;
        hardCopyLocation = in.readString();
        softCopyLocation = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookID);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(publication);
        dest.writeInt(totalPages);
        dest.writeFloat(price);
        dest.writeString(owner);
        dest.writeLong(purchaseTime);
        dest.writeParcelable(scheduleType, flags);
        dest.writeParcelable(readingHistory, flags);
        dest.writeInt(readingStatus.ordinal());
        dest.writeTypedList(notifyList);
        dest.writeByte((byte) (hardCopyCollected ? 1 : 0));
        dest.writeByte((byte) (softCopyCollected ? 1 : 0));
        dest.writeString(hardCopyLocation);
        dest.writeString(softCopyLocation);
    }
}
