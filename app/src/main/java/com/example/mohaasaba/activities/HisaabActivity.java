package com.example.mohaasaba.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mohaasaba.R;
import com.example.mohaasaba.fragment.FragmentTrxLists;
import com.example.mohaasaba.models.TransactionPage;

public class HisaabActivity extends AppCompatActivity {
    static int clks;
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

        FragmentTrxLists fragmentTrxLists = new FragmentTrxLists();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView_HisaabActivity, fragmentTrxLists)
                .commit();

        fragmentTrxLists.setCallbacks(new FragmentTrxLists.Callbacks() {
            @Override
            public void onFABClicked() {
                openTransactionPageEditFragment(null);
            }

            @Override
            public void onItemClicked(TransactionPage transactionPage) {
                Log.d("HisaabActivity", "onItemClicked: I'm here");
                openTransactionPageEditFragment(transactionPage);
            }
        });

    }

    private void openTransactionPageEditFragment(TransactionPage transactionPage) {

        Toast.makeText(this, "Clicked " + clks++, Toast.LENGTH_SHORT).show();
    }
}