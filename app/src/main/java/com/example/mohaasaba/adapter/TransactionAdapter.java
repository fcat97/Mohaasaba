package com.example.mohaasaba.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.models.Transaction;

public class TransactionAdapter extends ListAdapter<Transaction, TransactionAdapter.ViewHolder> {


    protected TransactionAdapter() {
        super(DIFF_UTILS);
    }

    private static final DiffUtil.ItemCallback<Transaction> DIFF_UTILS = new DiffUtil.ItemCallback<Transaction>() {
        @Override
        public boolean areItemsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.entryKey.equals(newItem.entryKey);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = getItem(position);
        if (transaction.note != null) holder.text.setText(transaction.note);
        String s = transaction.amount + " ৳";
        holder.amount.setText(s);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text, amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_TransactionItem_TextView_HisaabActivity);
            amount = itemView.findViewById(R.id.amount_TransactionItem_TextView_HisaabActivity);
        }
    }
}
