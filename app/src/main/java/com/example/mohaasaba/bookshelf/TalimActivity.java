package com.example.mohaasaba.bookshelf;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mohaasaba.R;
import com.example.mohaasaba.helper.TabPagerBinder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class TalimActivity extends AppCompatActivity {
    public static final String TAG = TalimActivity.class.getCanonicalName();
    public static final String BOOK_BUNDLE = "TalimActivity.BOOK_BUNDLE";
    private FragmentTalimMain fragmentTalimMain;
    private FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talim);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        frameLayout = findViewById(R.id.frameLayout_TalimActivity);
        fragmentTalimMain = new FragmentTalimMain()
                .setItemListener(this::openBookEditor);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout_TalimActivity, fragmentTalimMain)
                .commit();

    }

    private void openBookEditor(Book book) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_TalimActivity, new FragmentBookEditor().setBook(book))
                .addToBackStack("FragmentBookEditor")
                .commit();
    }
}