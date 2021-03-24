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
import com.example.mohaasaba.models.Progress;
import com.example.mohaasaba.receivers.NotificationScheduler;

import java.util.Calendar;
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

    private Toolbar toolbar;
    private FragmentTasbihListener listener;

    private ImageButton addButton, backButton;

    private TasbihRepository tasbihRepository;

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

        tasbihRepository = new TasbihRepository(getContext());

        prayerAdapter = new TasbihAdapter()
                .setItemClickListener(this::onAdapterItemClick)
                .setItemLongClickListener(tasbihRepository::updateTasbih);
        prayerRecyclerView.setAdapter(prayerAdapter);
        LiveData<List<Tasbih>> fazrLiveData = tasbihRepository.getTasbihPrayer();
        fazrLiveData.observe(getViewLifecycleOwner(), prayerAdapter::submitList);

        sleepAdapter = new TasbihAdapter()
                .setItemClickListener(this::onAdapterItemClick)
                .setItemLongClickListener(tasbihRepository::updateTasbih);
        sleepRecyclerView.setAdapter(sleepAdapter);
        LiveData<List<Tasbih>> sleepLiveData = tasbihRepository.getTasbihSleep();
        sleepLiveData.observe(getViewLifecycleOwner(), sleepAdapter::submitList);

        morningAdapter = new TasbihAdapter()
                .setItemClickListener(this::onAdapterItemClick)
                .setItemLongClickListener(tasbihRepository::updateTasbih);
        morningRecyclerView.setAdapter(morningAdapter);
        LiveData<List<Tasbih>> morningLiveData = tasbihRepository.getTasbihMorning();
        morningLiveData.observe(getViewLifecycleOwner(), morningAdapter::submitList);

        eveningAdapter = new TasbihAdapter()
                .setItemClickListener(this::onAdapterItemClick)
                .setItemLongClickListener(tasbihRepository::updateTasbih);
        eveningRecyclerView.setAdapter(eveningAdapter);
        LiveData<List<Tasbih>> eveningLiveData = tasbihRepository.getTasbihEvening();
        eveningLiveData.observe(getViewLifecycleOwner(), eveningAdapter::submitList);

        mustahabAdapter = new TasbihAdapter()
                .setItemClickListener(this::onAdapterItemClick)
                .setItemLongClickListener(tasbihRepository::updateTasbih);
        mustahabRecyclerView.setAdapter(mustahabAdapter);
        LiveData<List<Tasbih>> mustahabLiveData = tasbihRepository.getTasbihMustahab();
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

    private void onAdapterItemClick(Tasbih tasbih) {
        if (listener != null) listener.onClick(tasbih);
    }


    public FragmentTasbih setListener(FragmentTasbihListener listener) {
        this.listener = listener;
        return this;
    }

    public interface FragmentTasbihListener {
        void onClick(Tasbih tasbih);
    }
}