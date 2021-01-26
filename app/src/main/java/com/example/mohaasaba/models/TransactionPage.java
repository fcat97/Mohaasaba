package com.example.mohaasaba.models;

import java.util.List;

public class TransactionPage {
    public List<Transaction> transactionList;
    public String pageLabel;

    public TransactionPage(List<Transaction> transactionList, String pageLabel) {
        this.transactionList = transactionList;
        this.pageLabel = pageLabel;
    }

    public float getTotal() {
        float total = 0f;
        for (Transaction t :
                transactionList) {
            total += t.amount;
        }
        return total;
    }
}
