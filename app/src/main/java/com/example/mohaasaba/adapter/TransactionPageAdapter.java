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
import com.example.mohaasaba.models.Schedule;
import com.example.mohaasaba.models.TransactionPage;

public class TransactionPageAdapter extends ListAdapter<TransactionPage, TransactionPageAdapter.ViewHolder> {

    public TransactionPageAdapter() {
        super(DIFF_UTILS);
    }

    private static final DiffUtil.ItemCallback<TransactionPage> DIFF_UTILS = new DiffUtil.ItemCallback<TransactionPage>() {
        @Override
        public boolean areItemsTheSame(@NonNull TransactionPage oldItem, @NonNull TransactionPage newItem) {
            return oldItem.ID.equals(newItem.ID);
        }

        @Override
        public boolean areContentsTheSame(@NonNull TransactionPage oldItem, @NonNull TransactionPage newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionPage transactionPage = getItem(position);

        holder.label.setText(transactionPage.label);
        String s = transactionPage.calculateTransactions(transactionPage) + " ৳";
        holder.totalAmount.setText(s);

        TransactionAdapter adapter = new TransactionAdapter();
        holder.itemListsRecyclerView.setAdapter(adapter);

        adapter.submitList(transactionPage.transactions);


    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        RecyclerView itemListsRecyclerView;
        TextView totalAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label_TransactionPage_TextView_HisaabActivity);
            itemListsRecyclerView = itemView.findViewById(R.id.recyclerView_TransactionPage_TextView_HisaabActivity);
            totalAmount = itemView.findViewById(R.id.totalAmount_TransactionPage_TextView_HisaabActivity);
        }
    }
}
