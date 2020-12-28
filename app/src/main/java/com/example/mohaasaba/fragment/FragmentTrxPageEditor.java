package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.TransactionAdapter;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionPage;

public class FragmentTrxPageEditor extends Fragment {
    private EditText labelEditText;
    private Button addButton;
    private RecyclerView recyclerView;
    private TextView totalTextView;
    private TransactionPage transactionPage;
    private TransactionAdapter adapter;

    private Callbacks callbacks;

    public FragmentTrxPageEditor(TransactionPage transactionPage) {
        this.transactionPage = transactionPage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_page_editor, null);
        labelEditText = view.findViewById(R.id.label_EditText_FragmentTrxPgEdit);
        addButton = view.findViewById(R.id.add_Button_FragmentTrxPgEdit);
        recyclerView = view.findViewById(R.id.recyclerView_FragmentTrxPgEdit);
        totalTextView = view.findViewById(R.id.totalAmount_TextView_FragmentTrxPgEdit);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        labelEditText.setText(transactionPage.label);
        totalTextView.setText(String.valueOf(transactionPage.calculateTransactions(transactionPage)));

        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);
        adapter.submitList(transactionPage.transactions);

        addButton.setOnClickListener(v -> {
            Transaction transaction = new Transaction(0.0f);
            if (callbacks != null) callbacks.onAddButtonClicked(transaction);
        });

    }

    public void addTransaction(Transaction transaction) {
        transactionPage.transactions.add(transaction);
        adapter.submitList(transactionPage.transactions);
        adapter.notifyDataSetChanged();
        totalTextView.setText(String.valueOf(transactionPage.calculateTransactions(transactionPage)));
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void onAddButtonClicked(Transaction transaction);
    }
}
