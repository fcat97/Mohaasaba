package com.example.mohaasaba.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mohaasaba.R;

import com.example.mohaasaba.activities.HisaabActivity;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionAccount;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FragmentTransactionEditor extends BottomSheetDialogFragment {
    public static final String TAG = FragmentTransactionEditor.class.getName();
    private DeleteListener deleteListener;
    private ConfirmListener confirmListener;

    private EditText amountEditText;
    private EditText noteEditText;
    private EditText tagEditText;
    private ImageView addPageButton;
    private TextView selectedPageButton;
    private TextView selectAccountButton;
    private Button deleteButton, confirmButton;
    private Transaction transaction;
    private List<TransactionAccount> transactionAccounts;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = View.inflate(getContext(), R.layout.fragment_transaction_editor, null);
        amountEditText = rootView.findViewById(R.id.amount_EditText_FragmentTransactionEditor);
        noteEditText = rootView.findViewById(R.id.note_EditText_FragmentTransactionEditor);
        tagEditText = rootView.findViewById(R.id.tag_EditText_FragmentTransactionEditor);
        addPageButton = rootView.findViewById(R.id.addPage_Button_FragmentTransactionEditor);
        selectedPageButton = rootView.findViewById(R.id.page_TextView_FragmentTransactionEditor);
        selectAccountButton = rootView.findViewById(R.id.account_TextView_FragmentTransactionEditor);
        deleteButton = rootView.findViewById(R.id.delete_Button_FragmentTransactionEditor);
        confirmButton = rootView.findViewById(R.id.confirm_Button_FragmentTransactionEditor);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // For adding new Transaction
        if (transaction == null) transaction = new Transaction(0f);

        amountEditText.setText(String.valueOf(transaction.amount));
        selectAccountButton.setOnClickListener(this::openAccountMenu);
        selectedPageButton.setOnClickListener(this::openPageMenu);

        selectedPageButton.setText(transaction.page);
        selectAccountButton.setText(transaction.account);

        deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onClick(transaction);
            dismiss();
            Toast.makeText(getContext(), "Transaction Deleted", Toast.LENGTH_SHORT).show();
        });
        confirmButton.setOnClickListener(v -> {
            if (! amountEditText.getText().toString().isEmpty())
                transaction.amount = Float.parseFloat(amountEditText.getText().toString().trim());
            if (! noteEditText.getText().toString().isEmpty())
                transaction.note = noteEditText.getText().toString().trim();
            if (! tagEditText.getText().toString().isEmpty())
                transaction.tags = tagEditText.getText().toString().trim();
            if (confirmListener != null) confirmListener.onClick(transaction);
            dismiss();
            Toast.makeText(getContext(), "Transaction Added", Toast.LENGTH_SHORT).show();
        });
    }

    private void openPageMenu(View v) {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(
                HisaabActivity.HISAAB_SHARED_PREF, Context.MODE_PRIVATE);
        Set<String> pages = sharedPreferences.getStringSet(HisaabActivity.PAGES_SHARED_PREF, new HashSet<>());
        if (pages.size() == 0) pages.add(Transaction.DEFAULT_PAGE);
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setOnMenuItemClickListener(item -> {
            transaction.page = item.getTitle().toString();
            selectedPageButton.setText(transaction.page);
            return true;
        });
        for (String s :
                pages) {
            popupMenu.getMenu().add(s);
        }
        popupMenu.show();
    }
    private void openAccountMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        for (TransactionAccount account :
                transactionAccounts) {
            popupMenu.getMenu().add(account.name);
        }
        popupMenu.setOnMenuItemClickListener(item -> {
            transaction.account = item.getTitle().toString();
            selectAccountButton.setText(transaction.account);
            return true;
        });
        popupMenu.show();
    }

    public FragmentTransactionEditor setTransactionAccounts(List<TransactionAccount> transactionAccounts) {
        this.transactionAccounts = transactionAccounts;
        return this;
    }
    public FragmentTransactionEditor setTransaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }
    public FragmentTransactionEditor setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
        return this;
    }
    public FragmentTransactionEditor setConfirmListener(ConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
        return this;
    }
    public interface ConfirmListener {
        void onClick(Transaction transaction);
    }
    public interface DeleteListener {
        void onClick(Transaction transaction);
    }
}
