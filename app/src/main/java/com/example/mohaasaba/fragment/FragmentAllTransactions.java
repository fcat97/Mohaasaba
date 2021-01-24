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
import com.example.mohaasaba.database.AppRepository;
import com.example.mohaasaba.models.Transaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentAllTransactions extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private AddButtonClickListener addButtonClickListener;
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

        AppRepository repository = new AppRepository(Objects.requireNonNull(getContext()).getApplicationContext());
        LiveData<List<Transaction>> listLiveData = repository.getAllTransactions();

        TransactionDetailAdapter transactionDetailAdapter = new TransactionDetailAdapter()
                .setListener(transaction -> {
                    if (onItemClickedListener != null) onItemClickedListener.onClicked(transaction);
                });
        recyclerView.setAdapter(transactionDetailAdapter);

        // set observer to live data
        listLiveData.observe(getViewLifecycleOwner(), transactionDetailAdapter::submitList);

        addButton.setOnClickListener(v -> {
            if (addButtonClickListener != null) addButtonClickListener.onClick(new Transaction(0f));
        });
    }


    public FragmentAllTransactions setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
        return this;
    }
    public FragmentAllTransactions setAddButtonClickListener(AddButtonClickListener addButtonClickListener) {
        this.addButtonClickListener = addButtonClickListener;
        return this;
    }
    public interface AddButtonClickListener {
        void onClick(Transaction transaction);
    }
    public interface OnItemClickedListener {
        void onClicked(Transaction transaction);
    }

}
