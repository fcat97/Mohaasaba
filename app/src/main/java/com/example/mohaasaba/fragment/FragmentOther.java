package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.RecyclerViewAdapter;
import com.example.mohaasaba.models.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentOther extends Fragment {
    private FragmentOtherCallbacks callbacks;
    private RecyclerView recyclerView;
    private FloatingActionButton addSubScheduleButton;
    private RecyclerViewAdapter adapter;

    public FragmentOther(FragmentOtherCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other_add_schedule, container,false);
        recyclerView = rootView.findViewById(R.id.recyclerView_FragmentOther_AddScheduleActivity);
        addSubScheduleButton = rootView.findViewById(R.id.fab_FragmentOther_AddScheduleActivity);

        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (callbacks != null) callbacks.onReach();
        addSubScheduleButton.setOnClickListener(v -> {
            callbacks.onAddButtonClicked();
        });
    }

    public void submitList(List<Schedule> scheduleList) {
        adapter.submitList(scheduleList);
    }

    public interface FragmentOtherCallbacks {
        void onReach();
        void onAddButtonClicked();
    }
}
