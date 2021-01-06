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

import java.util.Calendar;

public class TransactionDetailAdapter extends ListAdapter<Transaction, TransactionDetailAdapter.ViewHolder> {


    public TransactionDetailAdapter() {
        super(DIFF_UTILS);
    }

    private static DiffUtil.ItemCallback<Transaction> DIFF_UTILS = new DiffUtil.ItemCallback<Transaction>() {
        @Override
        public boolean areItemsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.entryKey.equals(newItem.entryKey);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.amount == newItem.amount &&
                    oldItem.tags.equals(newItem.tags) &&
                    oldItem.note.equals(newItem.note) &&
                    oldItem.unit.equals(newItem.unit);
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = getItem(position);
        holder.label.setText(transaction.note);
        holder.tags.setText(transaction.tags);
        holder.amount.setText(String.valueOf(transaction.amount));

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(Long.parseLong(transaction.commitTime, 16));

        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        m++;
        String date = d + "/" + m;
        holder.date.setText(date);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView label;
        private TextView tags;
        private TextView amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_TextView_ItemTransactionDetails);
            label = itemView.findViewById(R.id.label_TextView_ItemTransactionDetails);
            tags = itemView.findViewById(R.id.tag_TextView_ItemTransactionDetails);
            amount = itemView.findViewById(R.id.amount_TextView__ItemTransactionDetails);
        }
    }
}
