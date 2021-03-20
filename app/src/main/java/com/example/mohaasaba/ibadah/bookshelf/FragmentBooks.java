package com.example.mohaasaba.ibadah.bookshelf;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.receivers.NotificationScheduler;

import java.util.List;
import java.util.Objects;

public class FragmentBooks extends Fragment {
    private static final String TAG = FragmentBooks.class.getCanonicalName();
    private RecyclerView readingRecyclerView;
    private RecyclerView readRecyclerView;
    private RecyclerView wishlistRecyclerView;
    private BookAdapter readingAdapter;
    private BookAdapter readAdapter;
    private BookAdapter wishListAdapter;
    private ItemClickedListener itemClickedListener;
    private AddButtonListener addButtonListener;
    private ImageButton addButton;
    private ImageButton backButton;
    private Toolbar toolbar;

    private LiveData<List<Book>> readingBooks;
    private LiveData<List<Book>> readBooks;
    private LiveData<List<Book>> wishListBooks;

    public FragmentBooks() {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_books, null);
        readingRecyclerView = view.findViewById(R.id.reading_RecyclerView_FragmentBook);
        readRecyclerView = view.findViewById(R.id.read_RecyclerView_FragmentBook);
        wishlistRecyclerView = view.findViewById(R.id.wishlist_RecyclerView_FragmentBook);
        addButton = view.findViewById(R.id.addButton_FragmentBook);
        backButton = view.findViewById(R.id.backButton_FragmentBook);
        toolbar = view.findViewById(R.id.toolbar_FragmentBooks);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle("Books");

        readingAdapter = new BookAdapter()
                .setItemClickListener(book -> {
                    if (itemClickedListener != null) itemClickedListener.onClick(book);
                });
        readingRecyclerView.setAdapter(readingAdapter);
        if (readingBooks != null) readingBooks.observe(getViewLifecycleOwner(), readingAdapter::submitList);

        readAdapter = new BookAdapter()
                .setItemClickListener(book -> {
                    if (itemClickedListener != null) itemClickedListener.onClick(book);
                });
        readRecyclerView.setAdapter(readAdapter);
        if (readBooks != null) readBooks.observe(getViewLifecycleOwner(), readAdapter::submitList);

        wishListAdapter = new BookAdapter()
                .setItemClickListener(book -> {
                    if (itemClickedListener != null) itemClickedListener.onClick(book);
                });
        wishlistRecyclerView.setAdapter(wishListAdapter);
        if (wishListBooks != null) wishListBooks.observe(getViewLifecycleOwner(), wishListAdapter::submitList);

        addButton.setOnClickListener(v -> {
            if (addButtonListener != null) addButtonListener.onClick(new Book(" "));
        });

        backButton.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).onBackPressed();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");
        Log.d(TAG, "rescheduleNotification: called");
        Intent intent = new Intent(getContext(), NotificationScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), NotificationScheduler.MIDNIGHT_REQUEST_PID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            pendingIntent.send(Objects.requireNonNull(getContext()).getApplicationContext(), 10099, intent);
            Log.d(TAG, "rescheduleNotification: done");
        } catch (PendingIntent.CanceledException e) {
            Log.d(TAG, "rescheduleNotification: failed");
            e.printStackTrace();
        }
    }

    public FragmentBooks setItemClickedListener(ItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
        return this;
    }

    public FragmentBooks setReadingBooks(LiveData<List<Book>> readingBooks) {
        this.readingBooks = readingBooks;
        return this;
    }

    public FragmentBooks setReadBooks(LiveData<List<Book>> readBooks) {
        this.readBooks = readBooks;
        return this;
    }

    public FragmentBooks setWishListBooks(LiveData<List<Book>> wishListBooks) {
        this.wishListBooks = wishListBooks;
        return this;
    }

    public interface ItemClickedListener {
        void onClick(Book book);
    }

    public FragmentBooks setAddButtonListener(AddButtonListener addButtonListener) {
        this.addButtonListener = addButtonListener;
        return this;
    }

    public interface AddButtonListener {
        void onClick(Book book);
    }
}
