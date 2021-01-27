package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.TransactionDetailAdapter;
import com.example.mohaasaba.database.TransactionRepository;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionPage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentAllTransactions extends Fragment {
    private RecyclerView recyclerView;
    private OnItemClickedListener onItemClickedListener;
    private TransactionDetailAdapter adapter;
    private FloatingActionButton addButton;
    private AddButtonListener addButtonListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_transaction, null, false);

        recyclerView = rootView.findViewById(R.id.recyclerView_FragmentAllTransaction);
        addButton = rootView.findViewById(R.id.fab_FragmentAllTransaction);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addButton.setOnClickListener(v -> {
            if (addButtonListener != null) addButtonListener.onClick(new Transaction(0f));
        });

        adapter = new TransactionDetailAdapter()
                .setListener(transaction -> {
                    if (onItemClickedListener != null) onItemClickedListener.onClicked(transaction);
                });
        recyclerView.setAdapter(adapter);

        TransactionRepository repository = new TransactionRepository(getContext());
        LiveData<List<Transaction>> transactionLiveData = repository.getAllTransactions();
        transactionLiveData.observe(getViewLifecycleOwner(), adapter::submitList);
    }

    public FragmentAllTransactions setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
        return this;
    }
    public FragmentAllTransactions setAddButtonListener(AddButtonListener addButtonListener) {
        this.addButtonListener = addButtonListener;
        return this;
    }

    public interface OnItemClickedListener {
        void onClicked(Transaction transaction);
    }
    public interface AddButtonListener {
        void onClick(Transaction transaction);
    }

}
