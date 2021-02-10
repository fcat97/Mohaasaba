package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.RecyclerViewAdapter;
import com.example.mohaasaba.models.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FragmentSearchMain extends Fragment {
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private LiveData<List<Schedule>> listLiveData;
    private List<Schedule> scheduleArrayList;
    private FragmentCallbacks callbacks;

    public FragmentSearchMain(LiveData<List<Schedule>> listLiveData) {
        this.listLiveData = listLiveData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_main, container, false);

        searchEditText = rootView.findViewById(R.id.search_EditText_SearchActivity);
        recyclerView = rootView.findViewById(R.id.recyclerView_SearchActivity);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new RecyclerViewAdapter();
        adapter.setOnClickListener(new RecyclerViewAdapter.OnClickListener() {
            @Override
            public void onItemClicked(Schedule schedule) {
                if (callbacks != null) callbacks.onItemClicked(schedule);
            }

            @Override
            public void onItemLongClicked(Schedule schedule) throws ExecutionException, InterruptedException {
                if (callbacks != null) callbacks.onItemLongClicked(schedule);
            }
        });
        recyclerView.setAdapter(adapter);
        listLiveData.observe(getViewLifecycleOwner(), schedules -> {
            scheduleArrayList = schedules;
            adapter.submitList(scheduleArrayList);
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = String.valueOf(s);
                adapter.submitList(search(string));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private List<Schedule> search(String s) {
        List<Schedule> matchedList = new ArrayList<>();
        for (Schedule schedule : scheduleArrayList) {
            if (containsIgnoreCase(schedule.getTitle(),s)) matchedList.add(schedule);
        }

        return matchedList;
    }
    private boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }





    public void setCallbacks(FragmentCallbacks callbacks) {
        this.callbacks = callbacks;
    }
    public interface FragmentCallbacks {
        void onItemClicked(Schedule schedule);
        void onItemLongClicked(Schedule schedule) throws ExecutionException, InterruptedException;
    }
}
