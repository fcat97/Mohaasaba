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
import com.example.mohaasaba.database.TransactionRepository;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionPage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentTransactionPages extends Fragment {
    private RecyclerView recyclerView;
    private TransactionPageAdapter adapter;
    private AddButtonCallback callback;
    private FloatingActionButton addButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_transaction_page_list, null);

        recyclerView = root.findViewById(R.id.recyclerView_FragmentTransactionPageList);
        addButton = root.findViewById(R.id.fab_FragmentTransactionPageList);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new TransactionPageAdapter();
        recyclerView.setAdapter(adapter);

       TransactionRepository repository = new TransactionRepository(getContext());
       LiveData<List<TransactionPage>> transactionPagesLiveData = repository.getAllTransactionPages();
       transactionPagesLiveData.observe(getViewLifecycleOwner(), adapter::submitList);

       addButton.setOnClickListener(v -> {
           if (callback != null) callback.onClick(new Transaction(0f));
       });
    }

    public FragmentTransactionPages setAddButtonClickListener(AddButtonCallback callback) {
        this.callback = callback;
        return this;
    }
    public interface AddButtonCallback {
        void onClick(Transaction transaction);
    }

}
