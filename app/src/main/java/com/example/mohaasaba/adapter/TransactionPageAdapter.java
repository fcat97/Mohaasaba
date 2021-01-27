package com.example.mohaasaba.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.models.TransactionPage;


public class TransactionPageAdapter  extends ListAdapter<TransactionPage, TransactionPageAdapter.ViewHolder> {
    private ItemClickListener itemClickListener;

    public TransactionPageAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<TransactionPage> DIFF_CALLBACK = new DiffUtil.ItemCallback<TransactionPage>() {
        @Override
        public boolean areItemsTheSame(@NonNull TransactionPage oldItem, @NonNull TransactionPage newItem) {
            if (oldItem == null && newItem != null) return false;
            else if (oldItem != null && newItem == null) return false;
            else return oldItem.pageID.equals(newItem.pageID);
        }

        @Override
        public boolean areContentsTheSame(@NonNull TransactionPage oldItem, @NonNull TransactionPage newItem) {
            if (oldItem == null && newItem != null) return false;
            else if (oldItem != null && newItem == null) return false;
            else return oldItem.pageLabel.equals(newItem.pageLabel);
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_page2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionPage transactionPage = getItem(position);

        holder.labelTextView.setText(transactionPage.pageLabel);
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) itemClickListener.onClick(transactionPage);
        });
    }




    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView labelTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            labelTextView = itemView.findViewById(R.id.pageLabel_TextView_ItemTransactionPage2);
        }
    }

    public TransactionPageAdapter setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public interface ItemClickListener {
        void onClick(TransactionPage transactionPage);
    }
}
