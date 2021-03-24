package com.example.mohaasaba.ibadah.tasbih;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.NotifyAdapter;
import com.example.mohaasaba.fragment.FragmentEditReminder;
import com.example.mohaasaba.helper.ViewMaker;
import com.example.mohaasaba.models.Notify;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTasbihDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTasbihDetail extends Fragment {
    private static final String ARG_TASBIH = FragmentTasbihDetail.class.getCanonicalName() + "_TASBIH";
    private static final String TAG = FragmentTasbihDetail.class.getCanonicalName();
    private Tasbih tasbih;

    private EditText label_et, ar_et, bn_et, ref_et, rewards_et;
    private Toolbar toolbar;
    private ImageButton saveButton, backButton;

    private RecyclerView notify_rv;
    private TextView noNotify_tv;
    private FloatingActionButton addNotifyButton;
    private NotifyAdapter notifyAdapter;
    private FrameLayout dateSelector_fl;

    private FrameLayout progressLayout;
    private FrameLayout typeLayout;
    private Button deleteButton;


    public FragmentTasbihDetail() {
        // Required empty public constructor
    }

    public static FragmentTasbihDetail newInstance(Tasbih tasbih) {
        FragmentTasbihDetail fragment = new FragmentTasbihDetail();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASBIH, tasbih);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tasbih = getArguments().getParcelable(ARG_TASBIH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_tasbih_detail, null, false);

        toolbar = view.findViewById(R.id.toolbar_FragmentTasbihDetail);
        saveButton = view.findViewById(R.id.saveButton_FragmentTasbihDetail);
        backButton = view.findViewById(R.id.backButton_FragmentTasbihDetail);

        label_et = view.findViewById(R.id.label_EditText_FragmentTasbihDetail);
        ar_et = view.findViewById(R.id.ar_EditText_FragmentTasbihDetail);
        bn_et = view.findViewById(R.id.bn_EditText_FragmentTasbihDetail);
        ref_et = view.findViewById(R.id.ref_EditText_FragmentTasbihDetail);
        rewards_et = view.findViewById(R.id.reward_EditText_FragmentTasbihDetail);

        notify_rv = view.findViewById(R.id.recyclerView_FragmentReminder);
        noNotify_tv = view.findViewById(R.id.noItem_FragmentReminder);
        addNotifyButton = view.findViewById(R.id.addReminder_FAB_FragmentReminder);
        dateSelector_fl = view.findViewById(R.id.dateSelector_FrameLayout_FragmentTasbihDetail);

        progressLayout = view.findViewById(R.id.preogress_FrameLayout_FragmentTasbihDetail);
        typeLayout = view.findViewById(R.id.type_FrameLayout_FragmentTasbihDetail);

        deleteButton = view.findViewById(R.id.deleteButton_FragmentTasbihDetail);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle("Tasbih Detail");

        label_et.setText(tasbih.label);
        ar_et.setText(tasbih.doa_ar);
        bn_et.setText(tasbih.doa_bn);
        ref_et.setText(tasbih.references);
        rewards_et.setText(tasbih.reward);

        // Set Progress History View
        ViewMaker.ProgressHistoryView progressHistoryView = new ViewMaker.ProgressHistoryView(getContext())
                .setProgressHistory(tasbih.history);
        progressLayout.addView(progressHistoryView.getView());

        // Set TasbihType View
        ViewMaker.TasbihTypeSelector tasbihTypeSelector = new ViewMaker.TasbihTypeSelector(getContext())
                .setTasbihType(tasbih.tasbihType);
        typeLayout.addView(tasbihTypeSelector.getView());

        TasbihRepository repository = new TasbihRepository(getContext());

        saveButton.setOnClickListener(v -> {
            tasbih.label = label_et.getText().toString().trim().isEmpty() ? null : label_et.getText().toString().trim();
            tasbih.doa_ar = ar_et.getText().toString().trim().isEmpty() ? "" : ar_et.getText().toString().trim();
            tasbih.doa_bn = bn_et.getText().toString().trim().isEmpty() ? "" : bn_et.getText().toString().trim();
            tasbih.references = ref_et.getText().toString().trim().isEmpty() ? "" : ref_et.getText().toString().trim();
            tasbih.reward = rewards_et.getText().toString().trim().isEmpty() ? "" : rewards_et.getText().toString().trim();

            tasbih.tasbihType = tasbihTypeSelector.getTasbihType();

            if (tasbih.label != null) repository.updateTasbih(tasbih);
            getParentFragmentManager().popBackStack();
        });

        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        deleteButton.setOnClickListener(v -> {
            repository.deleteTasbih(tasbih);
            getParentFragmentManager().popBackStack();
        });


        // Notify Related --------------------------------------------------------------------------
        notifyAdapter = new NotifyAdapter()
                .setOnItemClickCallBack(this::openNotifyEditor)
                .setOnDeleteListener(notify -> {
                    int position = notifyAdapter.getCurrentList().indexOf(notify);
                    tasbih.notifyList.remove(notify);
                    notifyAdapter.notifyItemRemoved(position);
                    notifyAdapter.notifyItemRangeChanged(position, notifyAdapter.getItemCount());
                    if (tasbih.notifyList.size() > 0) noNotify_tv.setVisibility(View.INVISIBLE);
                    else noNotify_tv.setVisibility(View.VISIBLE);
                });
        notify_rv.setAdapter(notifyAdapter);
        notifyAdapter.submitList(tasbih.notifyList);
        if (tasbih.notifyList.size() > 0) noNotify_tv.setVisibility(View.INVISIBLE);
        else noNotify_tv.setVisibility(View.VISIBLE);

        addNotifyButton.setOnClickListener(v -> openNotifyEditor(new Notify()));


        // ScheduleType Related---------------------------------------------------------------------
        ViewMaker.DateSelectorView dateSelectorView = new ViewMaker.DateSelectorView(getContext());
        dateSelectorView.setScheduleType(tasbih.scheduleType);
        dateSelector_fl.addView(dateSelectorView.getView());

    }

    private void openNotifyEditor(Notify notify) {
        FragmentEditReminder fragmentEditReminder = new FragmentEditReminder(notify)
                .setOnConfirmListeners(this::notifyEditConfirmed);
        fragmentEditReminder.show(getParentFragmentManager(), "Fragment Edit Reminder");
    }

    public void notifyEditConfirmed(Notify notify) {
        Log.d(TAG, "notifyEditConfirmed: called");
        Log.d(TAG, "notifyEditConfirmed: notify name " + notify.message);
        notify.label = tasbih.label; // Add the label to notify
        if (! tasbih.notifyList.contains(notify)) tasbih.notifyList.add(notify);
        notifyAdapter.submitList(tasbih.notifyList);
        notifyAdapter.notifyDataSetChanged();
        if (tasbih.notifyList.size() > 0) noNotify_tv.setVisibility(View.INVISIBLE);
        else noNotify_tv.setVisibility(View.VISIBLE);
    }
}