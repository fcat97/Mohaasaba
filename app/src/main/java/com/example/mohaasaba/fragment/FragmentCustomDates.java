package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.example.mohaasaba.R;
import com.example.mohaasaba.activities.TypeActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentCustomDates extends Fragment {
    private static final String TAG = "FragmentCustomDates";
    private TypeActivity.OnFragmentCreatedCallbacks callbacks;
    private CalendarView calendarView;


    public FragmentCustomDates() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_custom_dates_type_activity,container,false);
        calendarView = rootView.findViewById(R.id.calendarView_FragmentCustomDates_TypeActivity);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Calendar c = Calendar.getInstance();
        List<Calendar> calendars = new ArrayList<>();
        calendars.add(c);
        calendarView.setSelectedDates(calendars);
        callbacks.onFragmentCreated();
    }

    public void setSelectedDates(List<Calendar> calendars) {
        calendarView.setSelectedDates(calendars);
        calendarView.invalidate();
    }

    public List<Calendar> getSelectedDates() {
        return calendarView.getSelectedDates();
    }

    public void setCallbacks(TypeActivity.OnFragmentCreatedCallbacks callbacks) {
        this.callbacks = callbacks;
    }
}
