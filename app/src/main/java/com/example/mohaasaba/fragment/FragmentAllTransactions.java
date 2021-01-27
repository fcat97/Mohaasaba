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
    private List<Transaction> transactionList;
    private TransactionDetailAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_transaction, null, false);

        recyclerView = rootView.findViewById(R.id.recyclerView_FragmentAllTransaction);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TransactionRepository repository = new TransactionRepository(getContext());
        LiveData<List<TransactionPage>> transactionPagesLiveData = repository.getAllTransactionPages();
        transactionPagesLiveData.observe(getViewLifecycleOwner(), this::submitList);

        adapter = new TransactionDetailAdapter()
                .setListener(transaction -> {
                    if (onItemClickedListener != null) onItemClickedListener.onClicked(transaction);
                });
        recyclerView.setAdapter(adapter);
    }


    private void submitList(List<TransactionPage> transactionPageList) {
        transactionList = new ArrayList<>();
        for (TransactionPage t :
                transactionPageList) {
            transactionList.addAll(t.transactionList);
        }
        adapter.submitList(transactionList);
    }

    public FragmentAllTransactions setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
        return this;
    }
    public interface OnItemClickedListener {
        void onClicked(Transaction transaction);
    }

}
