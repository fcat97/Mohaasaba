package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;

import java.io.Serializable;

public class Transaction implements Parcelable, Serializable {
    public static final String DEFAULT_PAGE = "Untitled";

    public String entryKey; // Final Primary Key
    public String commitTime; // Default is Entry Time
    public String note;
    public String tags;
    public float amount; // expend is < 0 & income is > 0
    public String unit; // Default is BDT
    public String account; // Default is Cash;
    public String page; // Default is Untitled;

    /**
     * Make a Deep copy of Transaction.class
     * @param t object on which new copy will be made
     */
    public Transaction(Transaction t) {
        this.entryKey = t.entryKey;
        this.commitTime = t.commitTime;
        this.note = t.note;
        this.tags = t.tags;
        this.amount = t.amount;
        this.unit = t.unit;
        this.account = t.account;
        this.page = t.page;
    }

    public Transaction(float amount) {
        this.entryKey = IdGenerator.getNewID();
        this.commitTime = entryKey;
        this.tags = "";
        this.amount = amount;
        this.note = "";
        this.unit = "৳";
        this.account = TransactionAccount.DEFAULT_ACCOUNT;
        this.page = DEFAULT_PAGE;
    }

    protected Transaction(Parcel in) {
        entryKey = in.readString();
        commitTime = in.readString();
        note = in.readString();
        tags = in.readString();
        amount = in.readFloat();
        unit = in.readString();
        account = in.readString();
        page = in.readString();
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
        dest.writeString(account);
        dest.writeString(page);
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
