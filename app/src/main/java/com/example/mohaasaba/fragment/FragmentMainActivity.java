package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.RecyclerViewAdapter;
import com.example.mohaasaba.models.Schedule;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class FragmentMainActivity extends Fragment implements RecyclerViewAdapter.OnClickListener{
    private static final String TAG = "FragmentMAinActivity";
    private FragmentCallbacks callbacks;
    private RecyclerView mRecyclerView;
    private LiveData<List<Schedule>> mSchedules;
    private RecyclerViewAdapter mAdapter;
    private TextView noItemTextView;

    public FragmentMainActivity(LiveData<List<Schedule>> schedules) {
        this.mSchedules = schedules;
        setRetainInstance(true); //Crash after orientation changes solved
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new RecyclerViewAdapter();
        mAdapter.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_main_activity, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerView_FragmentManiActivity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        noItemTextView = rootView.findViewById(R.id.noItem_FragmentMainActivity);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSchedules.observe(getViewLifecycleOwner(), schedules -> {
            mAdapter.submitList(schedules);
            if (schedules.size() == 0) noItemTextView.setVisibility(View.VISIBLE);
            else noItemTextView.setVisibility(View.INVISIBLE);
        });
    }

    public void setCallbacks(FragmentCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void onItemClicked(Schedule schedule) {
        if (callbacks != null) callbacks.onItemClicked(schedule);
    }
    @Override
    public void onItemLongClicked(Schedule schedule) throws ExecutionException, InterruptedException {
        if (callbacks != null) callbacks.onItemLongClicked(schedule);
    }

    public interface FragmentCallbacks {
        void onItemClicked(Schedule schedule);
        void onItemLongClicked(Schedule schedule) throws ExecutionException, InterruptedException;
    }
}