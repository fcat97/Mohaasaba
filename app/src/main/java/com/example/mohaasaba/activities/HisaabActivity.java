package com.example.mohaasaba.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.mohaasaba.R;
import com.example.mohaasaba.fragment.FragmentAllTransactions;
import com.example.mohaasaba.fragment.FragmentTransactionEditor;
import com.example.mohaasaba.models.Transaction;
import com.google.android.material.tabs.TabLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HisaabActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private SharedPreferences sharedPreferences;

    public static final String HISAAB_SHARED_PREF = "com.mohaasaba.HISAAB_SHARED_PREF";
    public static final String PAGES_SHARED_PREF = "com.mohaasaba.HISAB_PAGES";
    public static final String ACCOUNT_SHARED_PREF = "com.mohaasaba.HISAB_ACCOUNTS";

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

        // Get shared pref of Page names
        sharedPreferences = getSharedPreferences(HISAAB_SHARED_PREF, MODE_PRIVATE);
        Set<String> accounts = sharedPreferences.getStringSet(ACCOUNT_SHARED_PREF, new HashSet<>());
        if (accounts.size() == 0) {
            // remember to add balance at end when initializing account
            accounts.add(Transaction.Account.getDefaultAccount());
            sharedPreferences.edit()
                    .putStringSet(ACCOUNT_SHARED_PREF, accounts)
                    .apply();
        }
        Set<String> pages = sharedPreferences.getStringSet(PAGES_SHARED_PREF, new HashSet<>());
        if (pages.size() == 0) {
            pages.add(Transaction.DEFAULT_PAGE);
            sharedPreferences.edit()
                    .putStringSet(PAGES_SHARED_PREF, pages)
                    .apply();
        }


        // initiate tabLayout and viewPager
        tabLayout = findViewById(R.id.tabLayout_HisaabActivity);
        viewPager = findViewById(R.id.viewPager_HisaabActivity);

        // Instantiate FragmentAllTransactions
        fragmentAllTransactions = new FragmentAllTransactions()
                .setAddButtonClickListener(this::openTransactionEditor); //set add button listener

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
                .setTransaction(transaction)
                .setConfirmListener(transaction1 -> {})
                .setDeleteListener(transaction1 -> {});

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
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

}