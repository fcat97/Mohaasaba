package com.example.mohaasaba.bookshelf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mohaasaba.R;
import com.example.mohaasaba.helper.TabPagerBinder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class FragmentTalimMain extends Fragment {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton addButton;

    private FragmentBooks fragmentBooksRead;
    private FragmentBooks fragmentBooksReading;
    private FragmentBooks fragmentBooksWishListed;

    private BookRepo bookRepo;
    private ItemListener itemListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talim_main, container, false);
        this.viewPager = view.findViewById(R.id.viewPager_FragmentTalimMain);
        this.tabLayout = view.findViewById(R.id.tabLayout_FragmentTalimMain);
        this.addButton = view.findViewById(R.id.fab_FragmentTalimMain);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bookRepo = new BookRepo(getContext());
        LiveData<List<Book>> readingBooks = bookRepo.getAllReadingBooks();
        LiveData<List<Book>> readBooks = bookRepo.getAllReadBooks();
        LiveData<List<Book>> wishListedBooks = bookRepo.getAllWishListedBooks();

        new TabPagerBinder(tabLayout, viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getParentFragmentManager(), getLifecycle()));
        tabLayout.selectTab(tabLayout.getTabAt(1)); // default selection

        fragmentBooksReading = new FragmentBooks(readingBooks)
                .setItemClickedListener(this::openFragmentBookEditor);
        fragmentBooksRead = new FragmentBooks(readBooks)
                .setItemClickedListener(this::openFragmentBookEditor);
        fragmentBooksWishListed = new FragmentBooks(wishListedBooks)
                .setItemClickedListener(this::openFragmentBookEditor);

        addButton.setOnClickListener(v -> openFragmentBookEditor(new Book(" ")));

    }

    private void openFragmentBookEditor(Book book) {
        if (itemListener != null) itemListener.onClick(book);
    }

    public FragmentTalimMain setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
        return this;
    }

    public interface ItemListener {
        void onClick(Book book);
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
