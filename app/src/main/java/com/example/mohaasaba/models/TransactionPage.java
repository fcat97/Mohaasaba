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
    public String pageKey;

    public List<Transaction> transactionList = new ArrayList<>();
    public String pageName;


    public TransactionPage(String pageName) {
        this.pageName = pageName;
        this.pageKey = IdGenerator.getNewID();
    }

    protected TransactionPage(Parcel in) {
        pageKey = in.readString();
        transactionList = in.createTypedArrayList(Transaction.CREATOR);
        pageName = in.readString();
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

    public float getNetTransaction() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pageKey);
        dest.writeTypedList(transactionList);
        dest.writeString(pageName);
    }
}
