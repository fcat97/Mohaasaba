package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.database.AppRepository;
import com.example.mohaasaba.helper.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class TransactionPage implements Parcelable {
    public String ID;

    public String label;
    public List<Transaction> transactions;
    public List<String> attachedPages;
    public int priority; // Default 10

    public TransactionPage() {
        this.ID = IdGenerator.getNewID();
        this.label = "Untitled";
        this.priority = 10;
        this.transactions = new ArrayList<>();
        this.attachedPages = new ArrayList<>();
    }

    // Todo: Need to improve to get attached pages total amount of transaction
    public float calculateTransactions(TransactionPage transactionPage) {
        float transaction = 0;
        for (Transaction t: transactionPage.transactions) {
            transaction += t.amount;
        }
        return transaction;
    }


    protected TransactionPage(Parcel in) {
        ID = in.readString();
        label = in.readString();
        transactions = in.createTypedArrayList(Transaction.CREATOR);
        attachedPages = in.createStringArrayList();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(label);
        dest.writeTypedList(transactions);
        dest.writeStringList(attachedPages);
    }
}
