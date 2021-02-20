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

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton addButton;

    private FragmentBooks fragmentBooksRead;
    private FragmentBooks fragmentBooksReading;
    private FragmentBooks fragmentBooksWishListed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talim);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.colorGray));
        }

        tabLayout = findViewById(R.id.tabLayout_TalimActivity);
        viewPager = findViewById(R.id.viewPager_TalimActivity);
        addButton = findViewById(R.id.fab_TalimActivity);

        BookRepo bookRepo = new BookRepo(this);
        LiveData<List<Book>> readingBooks = bookRepo.getAllReadingBooks();
        LiveData<List<Book>> readBooks = bookRepo.getAllReadBooks();
        LiveData<List<Book>> wishListedBooks = bookRepo.getAllWishListedBooks();

        new TabPagerBinder(tabLayout, viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle()));
        tabLayout.selectTab(tabLayout.getTabAt(1)); // default selection

        fragmentBooksReading = new FragmentBooks(readingBooks)
                .setItemClickedListener(this::openBookEditor);
        fragmentBooksRead = new FragmentBooks(readBooks)
                .setItemClickedListener(this::openBookEditor);
        fragmentBooksWishListed = new FragmentBooks(wishListedBooks)
                .setItemClickedListener(this::openBookEditor);

        addButton.setOnClickListener(v -> openBookEditor(new Book(" ")));
    }

    private void openBookEditor(Book book) {
        FragmentBookEditor fragmentBookEditor = new FragmentBookEditor()
                .setBook(book);
        fragmentBookEditor.show(getSupportFragmentManager(), "Fragment BookEditor");
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
                    return fragmentBooksRead;
                case 1:
                    return fragmentBooksReading;
                case 2:
                    return fragmentBooksWishListed;
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