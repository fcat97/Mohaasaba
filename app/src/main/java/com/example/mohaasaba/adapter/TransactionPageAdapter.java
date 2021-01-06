package com.example.mohaasaba.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionPage;

import java.util.List;

public class TransactionPageAdapter extends ListAdapter<TransactionPage, TransactionPageAdapter.ViewHolder> {
    private OnItemClicked onItemClicked;
    private OnAddButtonClicked onAddButtonClicked;

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
            return oldItem.transactions.equals(newItem.transactions);
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_page, parent, false);
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
        adapter.setCallbacks(() -> {
            if (onItemClicked != null) onItemClicked.onItemClicked(transactionPage);
        });
        adapter.submitList(transactionPage.transactions);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView label;
        private RecyclerView itemListsRecyclerView;
        private TextView totalAmount;
        private ImageView addTransactionButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label_TransactionPage_TextView_HisaabActivity);
            itemListsRecyclerView = itemView.findViewById(R.id.recyclerView_TransactionPage_TextView_HisaabActivity);
            totalAmount = itemView.findViewById(R.id.totalAmount_TransactionPage_TextView_HisaabActivity);
            addTransactionButton = itemView.findViewById(R.id.addTransactionButton_ImageView_HisaabActivity);

            addTransactionButton.setOnClickListener(v -> {
                if (onAddButtonClicked != null) onAddButtonClicked.onClick();
            });
            itemView.setOnClickListener(v -> {
                if (onItemClicked != null) onItemClicked.onItemClicked(getItem(getAdapterPosition()));
            });
        }
    }

    public TransactionPageAdapter setOnItemClickedCallback(OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
        return this;
    }
    public TransactionPageAdapter setOnAddButtonClickListener(OnAddButtonClicked onAddButtonClickListener) {
        this.onAddButtonClicked = onAddButtonClickListener;
        return this;
    }
    public interface OnItemClicked {
        void onItemClicked(TransactionPage transactionPage);
    }
    public interface OnAddButtonClicked {
        void onClick();
    }
}
