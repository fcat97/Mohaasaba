package com.example.mohaasaba.plans;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.mohaasaba.R;
import com.example.mohaasaba.receivers.NotificationScheduler;

import java.util.List;
import java.util.Objects;


public class FragmentPlan extends Fragment {
    private static final String TAG = FragmentPlan.class.getCanonicalName();
    private LiveData<List<Plan>> plansLiveData;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ImageButton backButton;
    private ImageButton addButton;
    private OnAddButtonClicked onAddButtonClicked;
    private ItemClickListener itemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = View.inflate(getContext(), R.layout.fragment_plan, null);
        recyclerView = rootView.findViewById(R.id.recyclerView_FragmentPlan);
        toolbar = rootView.findViewById(R.id.toolbar_FragmentPlan);
        backButton = rootView.findViewById(R.id.backButton_FragmentPlan);
        addButton = rootView.findViewById(R.id.addButton_FragmentPlan);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle("Plans");

        PlanAdapter adapter = new PlanAdapter()
                .setOnItemClickListener(plan -> {
                    if (itemClickListener != null) itemClickListener.onItemClicked(plan);
                })
                .setLongClickListener(plan -> {
                    FragmentPlanOptions fragmentPlanOptions = FragmentPlanOptions.getInstance(plan);
                    fragmentPlanOptions.show(getParentFragmentManager(), "Fragment Plan Options");
                });
        recyclerView.setAdapter(adapter);
        if (plansLiveData != null) {
            Log.d(TAG + "Plan", "onViewCreated: called");
            plansLiveData.observe(getViewLifecycleOwner(), adapter::submitList);
        }

        backButton.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).onBackPressed();
        });

        addButton.setOnClickListener(v -> {
            if (onAddButtonClicked != null) onAddButtonClicked.onClicked(new Plan());
        });
    }

    public FragmentPlan setPlansLiveData(LiveData<List<Plan>> plansLiveData) {
        this.plansLiveData = plansLiveData;
        return this;
    }

    public FragmentPlan setOnAddButtonClicked(OnAddButtonClicked onAddButtonClicked) {
        this.onAddButtonClicked = onAddButtonClicked;
        return this;
    }

    public FragmentPlan setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
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

    public interface ItemClickListener {
        void onItemClicked(Plan plan);
    }
    public interface OnAddButtonClicked {
        void onClicked(Plan plan);
    }

}