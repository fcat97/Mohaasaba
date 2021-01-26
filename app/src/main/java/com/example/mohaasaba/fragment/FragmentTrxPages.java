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
import com.example.mohaasaba.adapter.TransactionPageAdapter;
import com.example.mohaasaba.database.AppRepository;
import com.example.mohaasaba.database.TransactionRepository;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionPage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentTrxPages extends Fragment {
    private RecyclerView recyclerView;
    private TransactionRepository repository;
    private TransactionPageAdapter transactionPageAdapter;
    private LiveData<List<TransactionPage>> transactionPageLiveData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transaction_page_list, null);
        recyclerView = root.findViewById(R.id.recyclerView_FragmentTransactionPageList);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = new TransactionRepository(getContext());
        transactionPageAdapter = new TransactionPageAdapter();
        recyclerView.setAdapter(transactionPageAdapter);

        transactionPageLiveData = repository.getAllTransactionPages();
        transactionPageLiveData.observe(getViewLifecycleOwner(), transactionPageAdapter::submitList);
    }
}
