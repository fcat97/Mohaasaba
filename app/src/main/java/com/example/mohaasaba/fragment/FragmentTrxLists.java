package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentTrxLists extends Fragment {
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    Callbacks callbacks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_transaction_page_list, null);

        recyclerView = root.findViewById(R.id.recyclerView_FragmentTransactionPageList);
        floatingActionButton = root.findViewById(R.id.fab_FragmentTransactionPageList);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingActionButton.setOnClickListener(v -> {
            if (callbacks != null) callbacks.onFABClicked();
            else throw new ClassCastException("Must implement Callbacks");
        });

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
        adapter.setCallbacks(transactionPage -> {
            if (callbacks != null) callbacks.onItemClicked(transactionPage);
        });
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }
    public interface Callbacks {
        void onFABClicked();
        void onItemClicked(TransactionPage transactionPage);
    }
}
