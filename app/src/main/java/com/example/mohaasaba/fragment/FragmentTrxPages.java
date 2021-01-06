package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.TransactionPageAdapter;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionPage;

import java.util.ArrayList;
import java.util.List;

public class FragmentTrxPages extends Fragment {
    private RecyclerView recyclerView;
    private OnItemClickedCallback onItemClickedCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_transaction_page_list, null);

        recyclerView = root.findViewById(R.id.recyclerView_FragmentTransactionPageList);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<TransactionPage> pages = new ArrayList<>();

        TransactionPage tp1 = new TransactionPage();
        tp1.label = "All Transactions";
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        tp1.transactions.add(new Transaction(23f));
        pages.add(tp1);


        TransactionPage tp2 = new TransactionPage();
        tp2.label = "All Transactions";
        tp2.transactions.add(new Transaction(23f));
        tp2.transactions.add(new Transaction(23f));
        tp2.transactions.add(new Transaction(23f));
        tp2.transactions.add(new Transaction(23f));
        tp2.transactions.add(new Transaction(23f));
        tp2.transactions.add(new Transaction(23f));
        tp2.transactions.add(new Transaction(23f));
        tp2.transactions.add(new Transaction(23f));
        tp2.transactions.add(new Transaction(23f));
        tp2.transactions.add(new Transaction(23f));
        pages.add(tp2);

        TransactionPageAdapter adapter = new TransactionPageAdapter();
        adapter.submitList(pages);
        recyclerView.setAdapter(adapter);

        // set adapter listener
        adapter.setOnItemClickedCallback(transactionPage -> {
                    if (onItemClickedCallback != null) onItemClickedCallback.onItemClicked(transactionPage);
                });
    }


    public FragmentTrxPages setOnItemClickedCallback(OnItemClickedCallback onItemClickedCallback) {
        this.onItemClickedCallback = onItemClickedCallback;
        return this;
    }
    public interface OnItemClickedCallback {
        void onItemClicked(TransactionPage transactionPage);
    }
}
