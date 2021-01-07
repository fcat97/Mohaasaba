package com.example.mohaasaba.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;

@Entity(tableName = "transaction_account")
public class TransactionAccount implements Parcelable{
    public static final String DEFAULT_ACCOUNT = "Cash";
    public static final float DEFAULT_BALANCE = 0.0f;

    @PrimaryKey
    @NonNull
    public String accountID;
    public String name;
    public float balance;

    public TransactionAccount() {
        this.accountID = IdGenerator.getNewID();
    }

    protected TransactionAccount(Parcel in) {
        accountID = in.readString();
        name = in.readString();
        balance = in.readFloat();
    }

    public static final Creator<TransactionAccount> CREATOR = new Creator<TransactionAccount>() {
        @Override
        public TransactionAccount createFromParcel(Parcel in) {
            return new TransactionAccount(in);
        }

        @Override
        public TransactionAccount[] newArray(int size) {
            return new TransactionAccount[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountID);
        dest.writeString(name);
        dest.writeFloat(balance);
    }
}
