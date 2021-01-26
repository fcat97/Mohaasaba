package com.example.mohaasaba.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.AppRepository;
import com.example.mohaasaba.database.TransactionRepository;
import com.example.mohaasaba.fragment.FragmentAllTransactions;
import com.example.mohaasaba.fragment.FragmentTransactionEditor;
import com.example.mohaasaba.fragment.FragmentTrxPages;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionAccount;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class HisaabActivity extends AppCompatActivity {
    private static final String TAG = HisaabActivity.class.getCanonicalName();
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TransactionRepository repository;
    private List<Transaction> transactions = new ArrayList<>();
    private LiveData<List<TransactionAccount>> accountLiveData;
    private List<TransactionAccount> transactionAccounts = new ArrayList<>();
    private FragmentTrxPages fragmentTrxPages;

    public static final String HISAAB_SHARED_PREF = "com.mohaasaba.HISAAB_SHARED_PREF";
    public static final String PAGES_SHARED_PREF = "com.mohaasaba.HISAB_PAGES";

    private FragmentAllTransactions fragmentAllTransactions;

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

        // Get Repository
        repository = new TransactionRepository(this);


        // Set Transactions Accounts
        accountLiveData = repository.getAllTransactionAccounts();
        accountLiveData.observe(this, transactionAccounts1 -> {
            transactionAccounts = transactionAccounts1;
        });

        fragmentAllTransactions = new FragmentAllTransactions()
                .setAddButtonClickListener(this::openTransactionEditor);
        fragmentTrxPages = new FragmentTrxPages();

        // initiate tabLayout and viewPager
        tabLayout = findViewById(R.id.tabLayout_HisaabActivity);
        viewPager = findViewById(R.id.viewPager_HisaabActivity);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle()));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    private void openTransactionEditor(Transaction transaction) {
        FragmentTransactionEditor fragmentTransactionEditor = new FragmentTransactionEditor()
                .setTransactionAccounts(transactionAccounts)
                .setTransaction(new Transaction(transaction));

        fragmentTransactionEditor.show(getSupportFragmentManager(), "Transaction Editor");
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
                    return fragmentTrxPages;
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}