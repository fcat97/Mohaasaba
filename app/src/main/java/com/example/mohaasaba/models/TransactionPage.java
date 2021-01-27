package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "transaction_page")
public class TransactionPage implements Parcelable, Serializable {

    @NonNull
    @PrimaryKey
    public String pageID;

    public String pageLabel;

    public TransactionPage(String pageLabel) {
        this.pageID = IdGenerator.getNewID();
        this.pageLabel = pageLabel;
    }

    protected TransactionPage(Parcel in) {
        pageID = in.readString();
        pageLabel = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pageID);
        dest.writeString(pageLabel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionPage> CREATOR = new Creator<TransactionPage>() {
        @Override
        public TransactionPage createFromParcel(Parcel in) {
            return new TransactionPage(in);
        }

        @Override
        public TransactionPage[] newArray(int size) {
            return new TransactionPage[size];
        }
    };

    public float getTotal(List<Transaction> transactionList) {
        float total = 0f;
        for (Transaction t :
                transactionList) {
            total += t.amount;
        }
        return total;
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
