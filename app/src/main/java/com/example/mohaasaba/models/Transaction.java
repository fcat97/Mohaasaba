package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.example.mohaasaba.helper.IdGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements Parcelable, Serializable {
    public final String entryKey; // Final Primary Key
    public String commitTime; // Default is Entry Time
    public String note;
    public String tags;
    public float amount; // expend is < 0 & income is > 0
    public String unit; // Default is BDT
    public String fromPage; // Default is Pocket;
    public String toPage; // Default is Unknown;

    public Transaction(float amount) {
        this.entryKey = IdGenerator.getNewID();
        this.commitTime = entryKey;
        this.tags = "";
        this.amount = amount;
        this.note = "";
        this.unit = "৳";
        this.fromPage = "Pocket";
        this.toPage = "Unknown";
    }

    protected Transaction(Parcel in) {
        entryKey = in.readString();
        commitTime = in.readString();
        note = in.readString();
        tags = in.readString();
        amount = in.readFloat();
        unit = in.readString();
        fromPage = in.readString();
        toPage = in.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(entryKey);
        dest.writeString(commitTime);
        dest.writeString(note);
        dest.writeString(tags);
        dest.writeFloat(amount);
        dest.writeString(unit);
        dest.writeString(fromPage);
        dest.writeString(toPage);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
