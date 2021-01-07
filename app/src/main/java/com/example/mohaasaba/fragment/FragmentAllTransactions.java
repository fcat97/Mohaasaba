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
import com.example.mohaasaba.adapter.TransactionDetailAdapter;
import com.example.mohaasaba.models.Transaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentAllTransactions extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private AddButtonClickListener addButtonClickListener;
    private TransactionDetailAdapter transactionDetailAdapter;
    private OnCreateViewFinishedListener onFinishListener;
    private OnItemClickedListener onItemClickedListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_transaction, null, false);

        recyclerView = rootView.findViewById(R.id.recyclerView_FragmentAllTransaction);
        addButton = rootView.findViewById(R.id.addButton_FragmentAllTransaction);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactionDetailAdapter = new TransactionDetailAdapter()
                .setListener(transaction -> {
                   if (onItemClickedListener != null) onItemClickedListener.onClicked(transaction);
                });
        recyclerView.setAdapter(transactionDetailAdapter);
        addButton.setOnClickListener(v -> {
            if (addButtonClickListener != null) addButtonClickListener.onClick(new Transaction(0f));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onFinishListener != null) onFinishListener.onFinished();
    }

    public void updateList(List<Transaction> transactionList) {
        transactionDetailAdapter.submitList(transactionList);
    }

    public FragmentAllTransactions setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
        return this;
    }
    public FragmentAllTransactions setAddButtonClickListener(AddButtonClickListener addButtonClickListener) {
        this.addButtonClickListener = addButtonClickListener;
        return this;
    }
    public FragmentAllTransactions setOnReachListener(OnCreateViewFinishedListener listener) {
        this.onFinishListener = listener;
        return this;
    }
    public interface AddButtonClickListener {
        void onClick(Transaction transaction);
    }
    public interface OnCreateViewFinishedListener {
        void onFinished();
    }
    public interface OnItemClickedListener {
        void onClicked(Transaction transaction);
    }

}
