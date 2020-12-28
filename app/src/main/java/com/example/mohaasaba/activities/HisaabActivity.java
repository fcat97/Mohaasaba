package com.example.mohaasaba.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.mohaasaba.R;
import com.example.mohaasaba.fragment.FragmentTrxEditor;
import com.example.mohaasaba.fragment.FragmentTrxLists;
import com.example.mohaasaba.fragment.FragmentTrxPageEditor;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionPage;

public class HisaabActivity extends AppCompatActivity {
    private FragmentTrxLists fragmentTrxLists;
    private FragmentTrxPageEditor fragmentTrxPageEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisaab);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        Toolbar toolbar = findViewById(R.id.toolbar_HisaabActivity);
        setSupportActionBar(toolbar);

        showFragmentTrxList();
    }

    private void showFragmentTrxList() {
        fragmentTrxLists = new FragmentTrxLists();
        fragmentTrxLists.setCallbacks(this::openTransactionPageEditFragment);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView_HisaabActivity, fragmentTrxLists)
                .commit();
    }

    private void openTransactionPageEditFragment(TransactionPage transactionPage) {
        fragmentTrxPageEditor = new FragmentTrxPageEditor(transactionPage);
        fragmentTrxPageEditor.setCallbacks(this::openFragmentTrxEditor);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom,
                        R.anim.enter_from_bottom, R.anim.exit_to_bottom)
                .add(R.id.fragmentContainerView_HisaabActivity, fragmentTrxPageEditor)
                .addToBackStack(null)
                .commit();
    }

    private void openFragmentTrxEditor(Transaction transaction) {
        FragmentTrxEditor fragmentTrxEditor = new FragmentTrxEditor(transaction);
        fragmentTrxEditor.setCallbacks(this::saveTransaction);
        fragmentTrxEditor.show(getSupportFragmentManager(), "FragmentTrxEditor");
    }

    private void saveTransaction(Transaction transaction) {
        fragmentTrxPageEditor.addTransaction(transaction);
    }
}