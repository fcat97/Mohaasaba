package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mohaasaba.helper.IdGenerator;

import java.io.Serializable;

public class Transaction implements Parcelable, Serializable {
    public final String entryKey; // Final Primary Key
    public static final String DEFAULT_PAGE = "Untitled";
    public String commitTime; // Default is Entry Time
    public String note;
    public String tags;
    public float amount; // expend is < 0 & income is > 0
    public String unit; // Default is BDT
    public String account; // Default is Cash;
    public String page; // Default is Untitled;

    public Transaction(float amount) {
        this.entryKey = IdGenerator.getNewID();
        this.commitTime = entryKey;
        this.tags = "";
        this.amount = amount;
        this.note = "";
        this.unit = "৳";
        this.account = Account.DEFAULT_ACCOUNT;
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

    public static class Account {
        public static final String DEFAULT_ACCOUNT = "Cash";
        public static final String DEFAULT_BALANCE = "0.0";
        public static final String DELIMITER = "-->";
        public String name;
        public String balance;

        public static Account parseAccount(String string) {
            Account account1 = new Account();
            string = string.substring(0, string.length() - 1);
            account1.name = string.split(DELIMITER)[0];
            account1.balance = string.split(DELIMITER)[1];
            return account1;
        }



        public static String toString(Account account) {
            return account.name + DELIMITER + account.balance;
        }

        public static String getDefaultAccount() {
            return DEFAULT_ACCOUNT + DELIMITER + DEFAULT_BALANCE;
        }

    }
}
