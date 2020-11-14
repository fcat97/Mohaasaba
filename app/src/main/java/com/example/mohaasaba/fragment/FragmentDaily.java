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

    private CheckBox cbJanuary;
    private CheckBox cbFebruary;
    private CheckBox cbMarch;
    private CheckBox cbApril;
    private CheckBox cbMay;
    private CheckBox cbJune;
    private CheckBox cbJuly;
    private CheckBox cbAugust;
    private CheckBox cbSeptember;
    private CheckBox cbOctober;
    private CheckBox cbNovember;
    private CheckBox cbDecember;

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

        cbJanuary = rootView.findViewById(R.id.cb_january_FragmentDaily_TypeActivity);
        cbFebruary = rootView.findViewById(R.id.cb_february_FragmentDaily_TypeActivity);
        cbMarch = rootView.findViewById(R.id.cb_march_FragmentDaily_TypeActivity);
        cbApril = rootView.findViewById(R.id.cb_april_FragmentDaily_TypeActivity);
        cbMay = rootView.findViewById(R.id.cb_may_FragmentDaily_TypeActivity);
        cbJune = rootView.findViewById(R.id.cb_june_FragmentDaily_TypeActivity);
        cbJuly = rootView.findViewById(R.id.cb_july_FragmentDaily_TypeActivity);
        cbAugust = rootView.findViewById(R.id.cb_august_FragmentDaily_TypeActivity);
        cbSeptember = rootView.findViewById(R.id.cb_september_FragmentDaily_TypeActivity);
        cbOctober = rootView.findViewById(R.id.cb_october_FragmentDaily_TypeActivity);
        cbNovember = rootView.findViewById(R.id.cb_november_FragmentDaily_TypeActivity);
        cbDecember = rootView.findViewById(R.id.cb_december_FragmentDaily_TypeActivity);

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

    public CheckBox getCbJanuary() {
        return cbJanuary;
    }

    public CheckBox getCbFebruary() {
        return cbFebruary;
    }

    public CheckBox getCbMarch() {
        return cbMarch;
    }

    public CheckBox getCbApril() {
        return cbApril;
    }

    public CheckBox getCbMay() {
        return cbMay;
    }

    public CheckBox getCbJune() {
        return cbJune;
    }

    public CheckBox getCbJuly() {
        return cbJuly;
    }

    public CheckBox getCbAugust() {
        return cbAugust;
    }

    public CheckBox getCbSeptember() {
        return cbSeptember;
    }

    public CheckBox getCbOctober() {
        return cbOctober;
    }

    public CheckBox getCbNovember() {
        return cbNovember;
    }

    public CheckBox getCbDecember() {
        return cbDecember;
    }

    public void setCallbacks(TypeActivity.OnFragmentCreatedCallbacks callbacks) {
        this.callbacks = callbacks;
    }
}
