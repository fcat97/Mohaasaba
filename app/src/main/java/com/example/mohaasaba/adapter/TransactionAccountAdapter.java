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
import com.example.mohaasaba.models.TransactionAccount;

public class TransactionAccountAdapter extends ListAdapter<TransactionAccount, TransactionAccountAdapter.ViewHolder> {
    private ItemClickListener itemClickListener;


    public TransactionAccountAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<TransactionAccount> DIFF_CALLBACK = new DiffUtil.ItemCallback<TransactionAccount>() {
        @Override
        public boolean areItemsTheSame(@NonNull TransactionAccount oldItem, @NonNull TransactionAccount newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull TransactionAccount oldItem, @NonNull TransactionAccount newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_account, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionAccount account = getItem(position);

        holder.accountName.setText(account.name);
        holder.balance.setText(String.valueOf(account.balance));

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) itemClickListener.onClick(account);
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView accountName;
        private TextView balance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            accountName = itemView.findViewById(R.id.accountName_TextView_ItemTransactionAccount);
            balance = itemView.findViewById(R.id.balance_TextView_ItemTransactionAccount);
        }
    }


    public TransactionAccountAdapter setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }
    public interface ItemClickListener {
        void onClick(TransactionAccount account);
    }
}
