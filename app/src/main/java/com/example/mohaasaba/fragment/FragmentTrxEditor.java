package com.example.mohaasaba.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.mohaasaba.R;

import com.example.mohaasaba.database.DataConverter;
import com.example.mohaasaba.models.Transaction;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class FragmentTrxEditor extends BottomSheetDialogFragment {

    public static final String TAG = FragmentTrxEditor.class.getName();

    private Transaction transaction;
    private EditText amountEditText;
    private TextView incomeTextView, expenseTextView;
    private EditText noteEditText;
    private EditText tagEditText;
    private TextView fromTextView;
    private TextView toTextView;
    private Button confirmButton;
    private Callbacks callbacks;

    public FragmentTrxEditor(Transaction transaction) {
        this.transaction = transaction;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_editor, null);
        amountEditText = rootView.findViewById(R.id.amount_EditText_FragmentTransactionEditor);
        incomeTextView = rootView.findViewById(R.id.income_TextView_FragmentTransactionEditor);
        expenseTextView = rootView.findViewById(R.id.expense_TextView_FragmentTransactionEditor);
        noteEditText = rootView.findViewById(R.id.note_EditText_FragmentTransactionEditor);
        tagEditText = rootView.findViewById(R.id.tags_EditText_FragmentTransactionEditor);
        fromTextView = rootView.findViewById(R.id.from_select_TextView_FragmentTransactionEditor);
        toTextView = rootView.findViewById(R.id.to_select_TextView_FragmentTransactionEditor);
        confirmButton = rootView.findViewById(R.id.confirm_Button_FragmentTransactionEditor);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        amountEditText.setText(String.valueOf(transaction.amount));
        noteEditText.setText(transaction.note);
        tagEditText.setText(transaction.tags);

        if (Float.parseFloat(amountEditText.getText().toString()) < 0.0f) setAsExpense();
        else setAsIncome();

        confirmButton.setOnClickListener(v -> confirm());
        incomeTextView.setOnClickListener(v -> setAsIncome());
        expenseTextView.setOnClickListener(v -> setAsExpense());

        // set Expense|Income button(!) :p
    }

    private void setAsExpense() {
        expenseTextView.setBackgroundColor(Color.GREEN);
        expenseTextView.setTextColor(Color.WHITE);
        incomeTextView.setTextColor(Color.BLACK);
        incomeTextView.setBackgroundColor(Color.LTGRAY);
        if (Float.parseFloat(amountEditText.getText().toString()) > 0.0f) {
            transaction.amount = Float.parseFloat(amountEditText.getText().toString()) * (-1);
            amountEditText.setText(String.valueOf(transaction.amount));
        }
    }

    private void setAsIncome() {
            incomeTextView.setTextColor(Color.WHITE);
            incomeTextView.setBackgroundColor(Color.GREEN);
            expenseTextView.setBackgroundColor(Color.LTGRAY);
            expenseTextView.setTextColor(Color.BLACK);
            if (Float.parseFloat(amountEditText.getText().toString()) < 0.0f) {
                transaction.amount = Float.parseFloat(amountEditText.getText().toString()) * (-1);
                amountEditText.setText(String.valueOf(transaction.amount));
            }
    }

    private void confirm() {
        Log.d(TAG, "confirm: here");
        String s = amountEditText.getText().toString();
        String note = noteEditText.getText().toString();
        String tags = tagEditText.getText().toString();

        if (! s.isEmpty()) transaction.amount = Float.parseFloat(s.trim());
        if (! note.isEmpty()) transaction.note = note;
        if (! tags.isEmpty()) transaction.tags = tags;

        if (callbacks != null) callbacks.onConfirm(transaction);
        dismiss();
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }
    public interface Callbacks {
        void onConfirm(Transaction transaction);
    }

}
