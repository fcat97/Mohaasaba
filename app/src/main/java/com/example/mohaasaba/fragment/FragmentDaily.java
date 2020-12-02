package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mohaasaba.R;
import com.example.mohaasaba.activities.TypeActivity;

public class FragmentDaily extends Fragment {
    private TypeActivity.OnFragmentCreatedCallbacks callbacks;
    private CheckBox cbSaturday;
    private CheckBox cbSunday;
    private CheckBox cbMonday;
    private CheckBox cbTuesday;
    private CheckBox cbWednesday;
    private CheckBox cbThursday;
    private CheckBox cbFriday;

    public FragmentDaily() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_daily_type_activity, container, false);

        cbSaturday = rootView.findViewById(R.id.cb_saturday_FragmentDaily_TypeActivity);
        cbSunday = rootView.findViewById(R.id.cb_sunday_FragmentDaily_TypeActivity);
        cbMonday = rootView.findViewById(R.id.cb_monday_FragmentDaily_TypeActivity);
        cbTuesday = rootView.findViewById(R.id.cb_tuesday_FragmentDaily_TypeActivity);
        cbWednesday = rootView.findViewById(R.id.cb_wednesday_FragmentDaily_TypeActivity);
        cbThursday = rootView.findViewById(R.id.cb_thursday_FragmentDaily_TypeActivity);
        cbFriday = rootView.findViewById(R.id.cb_friday_FragmentDaily_TypeActivity);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callbacks.onFragmentCreated();
    }

    public CheckBox getCbSaturday() {
        return cbSaturday;
    }

    public CheckBox getCbSunday() {
        return cbSunday;
    }

    public CheckBox getCbMonday() {
        return cbMonday;
    }

    public CheckBox getCbTuesday() {
        return cbTuesday;
    }

    public CheckBox getCbWednesday() {
        return cbWednesday;
    }

    public CheckBox getCbThursday() {
        return cbThursday;
    }

    public CheckBox getCbFriday() {
        return cbFriday;
    }

    public void setCallbacks(TypeActivity.OnFragmentCreatedCallbacks callbacks) {
        this.callbacks = callbacks;
    }
}
