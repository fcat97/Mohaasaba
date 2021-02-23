package com.example.mohaasaba.ibadah.tasbih;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.mohaasaba.R;

import java.util.List;


public class FragmentTasbih extends Fragment {
    private RecyclerView recyclerView;
    private TasbihAdapter tasbihAdapter;
    private Toolbar toolbar;
    private FragmentTasbihListener listener;

    private ImageButton addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tasbih, null, false);

        recyclerView = view.findViewById(R.id.recyclerView_FragmentTasbih);
        addButton = view.findViewById(R.id.addButton_FragmentTasbih);
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
        tasbihAdapter = new TasbihAdapter()
                .setItemClickListener(tasbih -> {
                    if (listener != null) listener.onClick(tasbih);
                });
        recyclerView.setAdapter(tasbihAdapter);

        TasbihRepository tasbihRepository = new TasbihRepository(getContext());
        LiveData<List<Tasbih>> tasbihLiveData = tasbihRepository.getAllTasbih();

        tasbihLiveData.observe(getViewLifecycleOwner(), tasbihAdapter::submitList);
    }


    public FragmentTasbih setListener(FragmentTasbihListener listener) {
        this.listener = listener;
        return this;
    }

    public interface FragmentTasbihListener {
        void onClick(Tasbih tasbih);
    }
}