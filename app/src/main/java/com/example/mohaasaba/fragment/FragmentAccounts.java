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
import com.example.mohaasaba.adapter.TransactionAccountAdapter;
import com.example.mohaasaba.database.TransactionRepository;
import com.example.mohaasaba.models.TransactionAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentAccounts extends Fragment {
    private FloatingActionButton addButton;
    private RecyclerView recyclerView;
    private AddButtonListener addButtonListener;
    private ItemClickListener itemClickListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_account, null, false);

        addButton = rootView.findViewById(R.id.fab_FragmentTransactionAccount);
        recyclerView = rootView.findViewById(R.id.recyclerView_FragmentTransactionAccount);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TransactionAccountAdapter adapter = new TransactionAccountAdapter()
                .setItemClickListener(account -> {
                    if (itemClickListener != null) itemClickListener.onClick(account);
                });
        recyclerView.setAdapter(adapter);

        TransactionRepository repository = new TransactionRepository(getContext());
        LiveData<List<TransactionAccount>> listLiveData = repository.getAllTransactionAccounts();
        listLiveData.observe(getViewLifecycleOwner(), adapter::submitList);

        addButton.setOnClickListener(v -> {
            if (addButtonListener != null) addButtonListener.onClick(new TransactionAccount());
        });
    }



    public FragmentAccounts setAddButtonListener(AddButtonListener addButtonListener) {
        this.addButtonListener = addButtonListener;
        return this;
    }
    public FragmentAccounts setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public interface ItemClickListener {
        void onClick(TransactionAccount account);
    }
    public interface AddButtonListener {
        void onClick(TransactionAccount account);
    }
}
