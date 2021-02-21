package com.example.mohaasaba.bookshelf;

import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentBooks extends Fragment {
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
