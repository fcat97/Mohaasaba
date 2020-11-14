package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.mohaasaba.R;
import com.example.mohaasaba.activities.TypeActivity;

public class FragmentInterval extends Fragment {
    private TypeActivity.OnFragmentCreatedCallbacks callbacks;
    private EditText activeDaysEditText, inactiveDaysEditText;
    private SwitchCompat swContinuous;

    public FragmentInterval() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_interval_type_activity,container,false);
        activeDaysEditText = rootView.findViewById(R.id.activeDays_EditText_TypeActivity);
        inactiveDaysEditText = rootView.findViewById(R.id.inactiveDays_EditText_TypeActivity);
        swContinuous = rootView.findViewById(R.id.sw_continuous_FragmentInterval_TypeActivity);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callbacks.onFragmentCreated();
    }

    public void setFragmentData(int activeDays, int inactiveDays, boolean continuous) {
        activeDaysEditText.setText(String.valueOf(activeDays));
        inactiveDaysEditText.setText(String.valueOf(inactiveDays));
        swContinuous.setChecked(continuous);
    }
    public int getActiveDays() {
        if (activeDaysEditText.getText().toString().trim().isEmpty()) return 0;
        return Integer.parseInt(activeDaysEditText.getText().toString().trim());
    }
    public int getInactiveDays() {
        if (inactiveDaysEditText.getText().toString().trim().isEmpty()) return 0;
        return Integer.parseInt(inactiveDaysEditText.getText().toString().trim());
    }
    public boolean isContinuous() {
        return swContinuous.isChecked();
    }
    public void setCallbacks(TypeActivity.OnFragmentCreatedCallbacks callbacks) {
        this.callbacks = callbacks;
    }
}
