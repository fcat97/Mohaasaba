package com.example.mohaasaba.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.TransactionRepository;
import com.example.mohaasaba.fragment.FragmentAccountEditor;
import com.example.mohaasaba.fragment.FragmentAccounts;
import com.example.mohaasaba.fragment.FragmentAllTransactions;
import com.example.mohaasaba.fragment.FragmentTransactionEditor;
import com.example.mohaasaba.fragment.FragmentTransactionPages;
import com.example.mohaasaba.helper.TabPagerBinder;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionAccount;
import com.example.mohaasaba.models.TransactionPage;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HisaabActivity extends AppCompatActivity {
    private static final String TAG = HisaabActivity.class.getCanonicalName();

    private TransactionRepository repository;
    private List<TransactionAccount> transactionAccounts = new ArrayList<>();
    private List<TransactionPage> transactionPages = new ArrayList<>();

    private FragmentAllTransactions fragmentAllTransactions;
    private FragmentTransactionPages fragmentTransactionPages;
    private FragmentAccounts fragmentAccounts;

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


        // initiate tabLayout and viewPager
        TabLayout tabLayout = findViewById(R.id.tabLayout_HisaabActivity);
        ViewPager2 viewPager = findViewById(R.id.viewPager_HisaabActivity);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle()));
        new TabPagerBinder(tabLayout, viewPager);

        repository = new TransactionRepository(this);

        // Set Transactions Accounts
        LiveData<List<TransactionAccount>> accountLiveData = repository.getAllTransactionAccounts();
        accountLiveData.observe(this, transactionAccounts -> this.transactionAccounts = transactionAccounts);

        LiveData<List<TransactionPage>> pageLiveData = repository.getAllTransactionPages();
        pageLiveData.observe(this, transactionPageList -> this.transactionPages = transactionPageList);


        // Instantiate FragmentAllTransactions
        fragmentAllTransactions = new FragmentAllTransactions()
                .setOnItemClickedListener(this::openTransactionEditor)
                .setAddButtonListener(this::openTransactionEditor);

        // Instantiate FragmentTransactionPages
        fragmentTransactionPages = new FragmentTransactionPages()
                .setAddButtonClickListener(this::openTransactionEditor);

        // Instantiate FragmentTransactionAccount
        fragmentAccounts = new FragmentAccounts()
                .setAddButtonListener(this::openTransactionAccountEditor)
                .setItemClickListener(this::openTransactionAccountEditor);

    }

    private void openTransactionEditor(Transaction transaction) {
        FragmentTransactionEditor fragmentTransactionEditor = new FragmentTransactionEditor()
                .setTransactionAccounts(transactionAccounts)
                .setTransactionPages(transactionPages)
                .setTransaction(new Transaction(transaction))
                .setConfirmListener(repository::updateTransaction)
                .setDeleteListener(repository::deleteTransaction);

        fragmentTransactionEditor.show(getSupportFragmentManager(), "Transaction Editor");
    }

    private void openTransactionAccountEditor(TransactionAccount account) {
        FragmentAccountEditor accountEditor = new FragmentAccountEditor()
                .setAccount(account)
                .setConfirmListener(repository::updateTransactionAccount)
                .setDeleteListener(repository::deleteTransactionAccount);

        accountEditor.show(getSupportFragmentManager(), "FragmentAccountEditor");
    }

    private final class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return fragmentAllTransactions;
                case 1:
                    return fragmentTransactionPages;
                case 2:
                    return fragmentAccounts;
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

}