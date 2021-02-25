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
    private RecyclerView recyclerView;
    private LiveData<List<Book>> bookList;
    private BookAdapter adapter;
    private ItemClickedListener itemClickedListener;
    private AddButtonListener addButtonListener;
    private ImageButton addButton;
    private Toolbar toolbar;

    public FragmentBooks(LiveData<List<Book>> bookList) {
        this.bookList = bookList;
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_books, null);
        recyclerView = view.findViewById(R.id.recyclerView_FragmentBooks);
        addButton = view.findViewById(R.id.addButton_FragmentBook);
        toolbar = view.findViewById(R.id.toolbar_FragmentBooks);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle("Books");

        adapter = new BookAdapter()
                .setItemClickListener(book -> {
                    if (itemClickedListener != null) itemClickedListener.onClick(book);
                });
        recyclerView.setAdapter(adapter);
        bookList.observe(getViewLifecycleOwner(), adapter::submitList);

        addButton.setOnClickListener(v -> {
            if (addButtonListener != null) addButtonListener.onClick(new Book(" "));
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
