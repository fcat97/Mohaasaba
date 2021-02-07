package com.example.mohaasaba.bookshelf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;

import java.util.List;

public class FragmentBooks extends Fragment {
    private RecyclerView recyclerView;
    private LiveData<List<Book>> bookList;
    private BookAdapter adapter;
    private ItemClickedListener itemClickedListener;

    public FragmentBooks(LiveData<List<Book>> bookList) {
        this.bookList = bookList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_books, container);
        recyclerView = view.findViewById(R.id.recyclerView_FragmentBooks);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BookAdapter()
                .setItemClickListener(book -> {
                    if (itemClickedListener != null) itemClickedListener.onClick(book);
                });
        recyclerView.setAdapter(adapter);
        bookList.observe(getViewLifecycleOwner(), adapter::submitList);
    }

    public FragmentBooks setItemClickedListener(ItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
        return this;
    }

    public interface ItemClickedListener {
        void onClick(Book book);
    }
}
