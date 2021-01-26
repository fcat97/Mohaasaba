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
import com.example.mohaasaba.fragment.FragmentTrxPages;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionPage;

public class TransactionPageAdapter extends ListAdapter<TransactionPage, TransactionPageAdapter.ViewHolder> {

    public TransactionPageAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<TransactionPage> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TransactionPage>() {
                @Override
                public boolean areItemsTheSame(@NonNull TransactionPage oldItem,
                                               @NonNull TransactionPage newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(@NonNull TransactionPage oldItem,
                                                  @NonNull TransactionPage newItem) {
                    return false;
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_page, null, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionPage transactionPage = getItem(position);

        holder.label.setText(transactionPage.pageName);
        holder.totalAmount.setText(String.valueOf(transactionPage.getNetTransaction()));

        TransactionAdapter transactionAdapter = new TransactionAdapter();
        holder.recyclerView.setAdapter(transactionAdapter);
        transactionAdapter.submitList(transactionPage.transactionList);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView label;
        private TextView totalAmount;
        private ImageView addButton;
        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label_TextView_ItemTransactionPage);
            totalAmount = itemView.findViewById(R.id.totalAmount_TextView_ItemTransactionPage);
            //addButton = itemView.findViewById(R.id.addButton_ImageView_ItemTransactionPage);
            recyclerView = itemView.findViewById(R.id.recyclerView_ItemTransactionPage);
        }
    }

}
