package com.example.mohaasaba.bookshelf;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mohaasaba.R;
import com.example.mohaasaba.helper.TabPagerBinder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class TalimActivity extends AppCompatActivity {
    public static final String TAG = TalimActivity.class.getCanonicalName();
    private FloatingActionButton addButton;
    private BookRepo bookRepo;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FragmentBooks fragmentBooksReading;
    private FragmentBooks fragmentBooksCollected;
    private FragmentBooks fragmentBooksWishListed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talim);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        addButton = findViewById(R.id.fab_TalimActivity);
        viewPager = findViewById(R.id.viewPager_TalimActivity);
        tabLayout = findViewById(R.id.tabLayout_TalimActivity);
        new TabPagerBinder(tabLayout, viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle()));

        this.bookRepo = new BookRepo(this);
        LiveData<List<Book>> readingBooks = bookRepo.getAllReadingBooks();
        LiveData<List<Book>> collectedBooks = bookRepo.getAllCollectedBooks();
        LiveData<List<Book>> wishListedBooks = bookRepo.getAllWishListedBooks();

        fragmentBooksReading = new FragmentBooks(readingBooks)
                .setItemClickedListener(this::openFragmentBookEditor);
        fragmentBooksCollected = new FragmentBooks(collectedBooks)
                .setItemClickedListener(this::openFragmentBookEditor);
        fragmentBooksWishListed = new FragmentBooks(wishListedBooks)
                .setItemClickedListener(this::openFragmentBookEditor);

        addButton.setOnClickListener(v -> openFragmentBookEditor(new Book(" ")));
    }


    private void openFragmentBookEditor(Book book) {
        FragmentBookEditor fragmentBookEditor = new FragmentBookEditor()
                .setBook(book);
        fragmentBookEditor.show(getSupportFragmentManager(), "FragmentBookEditor");
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
                    return fragmentBooksReading;
                case 1:
                    return fragmentBooksCollected;
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