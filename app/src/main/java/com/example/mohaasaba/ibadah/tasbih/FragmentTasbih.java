package com.example.mohaasaba.ibadah.tasbih;

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


public class FragmentTasbih extends Fragment {
    private static final String TAG = FragmentTasbih.class.getCanonicalName();
    private RecyclerView prayerRecyclerView;
    private RecyclerView sleepRecyclerView;
    private RecyclerView morningRecyclerView;
    private RecyclerView eveningRecyclerView;
    private RecyclerView mustahabRecyclerView;

    private TasbihAdapter prayerAdapter;
    private TasbihAdapter sleepAdapter;
    private TasbihAdapter morningAdapter;
    private TasbihAdapter eveningAdapter;
    private TasbihAdapter mustahabAdapter;

    private LiveData<List<Tasbih>> prayerLiveData;
    private LiveData<List<Tasbih>> sleepLiveData;
    private LiveData<List<Tasbih>> morningLiveData;
    private LiveData<List<Tasbih>> eveningLiveData;
    private LiveData<List<Tasbih>> mustahabLiveData;

    private Toolbar toolbar;
    private FragmentTasbihListener listener;

    private ImageButton addButton, backButton;

    private ItemLongClickListener longClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tasbih, null, false);

        prayerRecyclerView = view.findViewById(R.id.fazr_RecyclerView_FragmentTasbih);
        sleepRecyclerView = view.findViewById(R.id.sleep_RecyclerView_FragmentTasbih);
        morningRecyclerView = view.findViewById(R.id.morning_RecyclerView_FragmentTasbih);
        eveningRecyclerView = view.findViewById(R.id.evening_RecyclerView_FragmentTasbih);
        mustahabRecyclerView = view.findViewById(R.id.mustahab_RecyclerView_FragmentTasbih);

        addButton = view.findViewById(R.id.addButton_FragmentTasbih);
        backButton = view.findViewById(R.id.backButton_FragmentTasbih);
        toolbar = view.findViewById(R.id.toolbar_FragmentTasbih);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setTitle("Tasbih");

        addButton.setOnClickListener(v -> {
            if (listener != null) listener.onClick(new Tasbih());
        });

        backButton.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        prayerAdapter = new TasbihAdapter()
                .setItemClickListener(this::onAdapterItemClick)
                .setItemLongClickListener(tasbih -> {
                        if (longClickListener != null) longClickListener.onLongClick(tasbih);
                });
        prayerRecyclerView.setAdapter(prayerAdapter);
        prayerLiveData.observe(getViewLifecycleOwner(), prayerAdapter::submitList);

        sleepAdapter = new TasbihAdapter()
                .setItemClickListener(this::onAdapterItemClick)
                .setItemLongClickListener(tasbih -> {
                    if (longClickListener != null) longClickListener.onLongClick(tasbih);
                });
        sleepRecyclerView.setAdapter(sleepAdapter);
        sleepLiveData.observe(getViewLifecycleOwner(), sleepAdapter::submitList);

        morningAdapter = new TasbihAdapter()
                .setItemClickListener(this::onAdapterItemClick)
                .setItemLongClickListener(tasbih -> {
                    if (longClickListener != null) longClickListener.onLongClick(tasbih);
                });
        morningRecyclerView.setAdapter(morningAdapter);
        morningLiveData.observe(getViewLifecycleOwner(), morningAdapter::submitList);

        eveningAdapter = new TasbihAdapter()
                .setItemClickListener(this::onAdapterItemClick)
                .setItemLongClickListener(tasbih -> {
                    if (longClickListener != null) longClickListener.onLongClick(tasbih);
                });
        eveningRecyclerView.setAdapter(eveningAdapter);
        eveningLiveData.observe(getViewLifecycleOwner(), eveningAdapter::submitList);

        mustahabAdapter = new TasbihAdapter()
                .setItemClickListener(this::onAdapterItemClick)
                .setItemLongClickListener(tasbih -> {
                    if (longClickListener != null) longClickListener.onLongClick(tasbih);
                });
        mustahabRecyclerView.setAdapter(mustahabAdapter);
        mustahabLiveData.observe(getViewLifecycleOwner(), mustahabAdapter::submitList);
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

    public FragmentTasbih setPrayerLiveData(LiveData<List<Tasbih>> prayerLiveData) {
        this.prayerLiveData = prayerLiveData;
        return this;
    }

    public FragmentTasbih setSleepLiveData(LiveData<List<Tasbih>> sleepLiveData) {
        this.sleepLiveData = sleepLiveData;
        return this;
    }

    public FragmentTasbih setMorningLiveData(LiveData<List<Tasbih>> morningLiveData) {
        this.morningLiveData = morningLiveData;
        return this;
    }

    public FragmentTasbih setEveningLiveData(LiveData<List<Tasbih>> eveningLiveData) {
        this.eveningLiveData = eveningLiveData;
        return this;
    }

    public FragmentTasbih setMustahabLiveData(LiveData<List<Tasbih>> mustahabLiveData) {
        this.mustahabLiveData = mustahabLiveData;
        return this;
    }

    private FragmentTasbih onAdapterItemClick(Tasbih tasbih) {
        if (listener != null) listener.onClick(tasbih);
        return this;
    }


    public FragmentTasbih setListener(FragmentTasbihListener listener) {
        this.listener = listener;
        return this;
    }

    public FragmentTasbih setLongClickListener(ItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
        return this;
    }

    public interface FragmentTasbihListener {
        void onClick(Tasbih tasbih);
    }

    public interface ItemLongClickListener {
        void onLongClick(Tasbih tasbih);
    }
}