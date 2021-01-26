package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mohaasaba.R;
import com.example.mohaasaba.models.TransactionAccount;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentAccountEditor extends BottomSheetDialogFragment {
    private EditText accountNameEditText;
    private EditText balanceEditText;
    private TextView confirmButton, deleteButton;
    private ConfirmListener confirmListener;
    private DeleteListener deleteListener;
    private TransactionAccount transactionAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_account_editor, null, false);

        accountNameEditText = rootView.findViewById(R.id.accountLabel_EditText_FragmentAccountEditor);
        balanceEditText = rootView.findViewById(R.id.balance_EditText_FragmentAccountEditor);
        confirmButton = rootView.findViewById(R.id.confirm_Button_FragmentAccountEditor);
        deleteButton = rootView.findViewById(R.id.delete_Button_FragmentAccountEditor);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (transactionAccount != null) {
            accountNameEditText.setText(transactionAccount.name);
            balanceEditText.setText(String.valueOf(transactionAccount.balance));
        } else {
            accountNameEditText.setText(TransactionAccount.DEFAULT_ACCOUNT);
            balanceEditText.setText(String.valueOf(TransactionAccount.DEFAULT_BALANCE));
        }


        // Set Confirm Button Click Actions
        confirmButton.setOnClickListener(v -> {
            if (accountNameEditText.getText().toString().isEmpty()) return;
            transactionAccount.name = accountNameEditText.getText().toString();
            transactionAccount.balance = Float.parseFloat(balanceEditText.getText().toString());
            if (confirmListener != null) confirmListener.onClick(transactionAccount);
            dismiss();
            Toast.makeText(getContext(), "Account Edited", Toast.LENGTH_SHORT).show();
        });

        // Set Delete Button Click Action
        deleteButton.setOnClickListener(v -> {
            if (transactionAccount.name.equals(TransactionAccount.DEFAULT_ACCOUNT)) {
                Toast.makeText(getContext(), "Can't Delete This Account", Toast.LENGTH_SHORT).show();
                dismiss();
                return;
            }
            if (deleteListener != null) deleteListener.onClick(transactionAccount);
            dismiss();
            Toast.makeText(getContext(), "Transaction Deleted", Toast.LENGTH_SHORT).show();
        });

    }

    public FragmentAccountEditor setAccount(TransactionAccount account) {
        this.transactionAccount = account;
        return this;
    }



    public FragmentAccountEditor setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
        return this;
    }
    public FragmentAccountEditor setConfirmListener(ConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
        return this;
    }
    public interface ConfirmListener {
        void onClick(TransactionAccount account);
    }
    public interface DeleteListener {
        void onClick(TransactionAccount account);
    }

}
