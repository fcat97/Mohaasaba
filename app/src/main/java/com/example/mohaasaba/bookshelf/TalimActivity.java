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

    private FragmentBooks fragmentBooksReading;
    private BookRepo bookRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talim);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.colorWhite));
        }

        bookRepo = new BookRepo(this);
        LiveData<List<Book>> allBooks = bookRepo.getAllBooks();

        fragmentBooksReading = new FragmentBooks(allBooks)
                .setItemClickedListener(this::openBookEditor)
                .setAddButtonListener(this::openBookEditor);
        openFragmentBooks();
    }

    private void openBookEditor(Book book) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_TalimActivity, FragmentBookDetail.newInstance(book)
                    .setSaveButtonListener(book1 -> {
                        bookRepo.updateBook(book1);
                        getSupportFragmentManager().popBackStack();
                    }))
                .addToBackStack("Fragment Book Detail")
                .commit();
    }
    
    private void openFragmentBooks() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout_TalimActivity, fragmentBooksReading)
                .commit();
    }
}