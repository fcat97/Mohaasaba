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
    private RecyclerView recyclerView;
    private TasbihAdapter tasbihAdapter;
    private Toolbar toolbar;
    private FragmentTasbihListener listener;

    private ImageButton addButton, backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tasbih, null, false);

        recyclerView = view.findViewById(R.id.recyclerView_FragmentTasbih);
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

        tasbihAdapter = new TasbihAdapter()
                .setItemClickListener(tasbih -> {
                    if (listener != null) listener.onClick(tasbih);
                });
        recyclerView.setAdapter(tasbihAdapter);

        TasbihRepository tasbihRepository = new TasbihRepository(getContext());
        LiveData<List<Tasbih>> tasbihLiveData = tasbihRepository.getAllTasbih();

        tasbihLiveData.observe(getViewLifecycleOwner(), tasbihAdapter::submitList);
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


    public FragmentTasbih setListener(FragmentTasbihListener listener) {
        this.listener = listener;
        return this;
    }

    public interface FragmentTasbihListener {
        void onClick(Tasbih tasbih);
    }
}