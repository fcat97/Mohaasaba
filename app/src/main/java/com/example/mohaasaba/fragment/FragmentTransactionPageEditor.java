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
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionPage;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentTransactionPageEditor extends BottomSheetDialogFragment {
    private EditText pageLabelEditText;
    private TextView confirmButton, deleteButton;
    private ConfirmButtonListener confirmButtonListener;
    private DeleteButtonListener deleteButtonListener;
    private TransactionPage transactionPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_page_editor, container, false);
        pageLabelEditText = rootView.findViewById(R.id.pageLabel_EditText_FragmentTransactionPageEditor);
        deleteButton = rootView.findViewById(R.id.delete_Button_FragmentTransactionPageEditor);
        confirmButton = rootView.findViewById(R.id.confirm_Button_FragmentTransactionPageEditor);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pageLabelEditText.setText(transactionPage.pageLabel);

        confirmButton.setOnClickListener(v -> {
            if (pageLabelEditText.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Can't create page with empty name", Toast.LENGTH_SHORT).show();
            } else {
                if (transactionPage.pageLabel.equals(Transaction.DEFAULT_PAGE)) {
                    Toast.makeText(getContext(), "Can't Edit Default Page", Toast.LENGTH_SHORT).show();
                } else {
                    transactionPage.pageLabel = pageLabelEditText.getText().toString().trim();
                    if (confirmButtonListener != null) confirmButtonListener.onClick(transactionPage);
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (transactionPage.pageLabel.equals(Transaction.DEFAULT_PAGE)) {
                Toast.makeText(getContext(), "Can't Delete Default Page", Toast.LENGTH_SHORT).show();
            } else {
                if (deleteButtonListener != null) deleteButtonListener.onClick(transactionPage);
                Toast.makeText(getContext(), "Page Deleted", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }


    public FragmentTransactionPageEditor setTransactionPage(TransactionPage transactionPage) {
        this.transactionPage = transactionPage;
        return this;
    }
    public FragmentTransactionPageEditor setConfirmButtonListener(ConfirmButtonListener confirmButtonListener) {
        this.confirmButtonListener = confirmButtonListener;
        return this;
    }
    public FragmentTransactionPageEditor setDeleteButtonListener(DeleteButtonListener deleteButtonListener) {
        this.deleteButtonListener = deleteButtonListener;
        return this;
    }

    public interface ConfirmButtonListener {
        void onClick(TransactionPage transactionPage);
    }
    public interface DeleteButtonListener {
        void onClick(TransactionPage transactionPage);
    }
}
