package com.example.mohaasaba.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.TransactionDetailAdapter;
import com.example.mohaasaba.database.TransactionRepository;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionPage;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class FragmentTransactionPage extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private LiveData<List<Transaction>> transactionLiveData;
    private TransactionPage transactionPage;
    private AddButtonListener addButtonListener;
    private ItemClickListener itemClickListener;
    private EditButtonListener editButtonListener;
    private TextView pageLabelTextView;
    private TextView editButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = View.inflate(getContext(), R.layout.fragment_transaction_page, container);
        recyclerView = rootView.findViewById(R.id.recyclerView_FragmentTransactionPage);
        addButton = rootView.findViewById(R.id.fab_FragmentTransactionPage);
        pageLabelTextView = rootView.findViewById(R.id.pageLabel_TextView_FragmentTransactionPage);
        editButton = rootView.findViewById(R.id.edit_TextView_FragmentTransactionPage);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pageLabelTextView.setText(transactionPage.pageLabel);

        TransactionDetailAdapter adapter = new TransactionDetailAdapter();
        recyclerView.setAdapter(adapter);

        TransactionRepository repository = new TransactionRepository(getContext());
        transactionLiveData = repository.getTransactionOf(transactionPage.pageLabel);
        transactionLiveData.observe(getViewLifecycleOwner(), adapter::submitList);

        adapter.setListener(transaction -> {
            if (itemClickListener != null) itemClickListener.onClick(transaction);
        });

        addButton.setOnClickListener(v -> {
            if (addButtonListener != null) addButtonListener.onClick(new Transaction(0f));
        });

        editButton.setOnClickListener(v -> {
            if (editButtonListener != null) editButtonListener.onClick(transactionPage);
            dismiss();
        });

    }


    public FragmentTransactionPage setTransactionPage(TransactionPage transactionPage) {
        this.transactionPage = transactionPage;
        return this;
    }

    public FragmentTransactionPage setAddButtonListener(AddButtonListener addButtonListener) {
        this.addButtonListener = addButtonListener;
        return this;
    }

    public FragmentTransactionPage setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public FragmentTransactionPage setEditButtonListener(EditButtonListener editButtonListener) {
        this.editButtonListener = editButtonListener;
        return this;
    }

    public interface AddButtonListener {
        void onClick(Transaction transaction);
    }
    public interface ItemClickListener {
        void onClick(Transaction transaction);
    }
    public interface EditButtonListener {
        void onClick(TransactionPage transactionPage);
    }
}