package com.example.mohaasaba.plans;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.mohaasaba.R;

import java.util.Calendar;
import java.util.HashSet;

public class ViewWeekDays implements View.OnClickListener{
    private View rootView;
    private HashSet<Integer> selectedWeekDays;
    private final int selectedColor;
    private final int deselectedColor;
    private OnWeekDateChangedListener listener;

    private TextView sat;
    private TextView sun;
    private TextView mon;
    private TextView tue;
    private TextView wed;
    private TextView thu;
    private TextView fri;

    public boolean saturday = false;
    public boolean sunday = false;
    public boolean monday = false;
    public boolean tuesday = false;
    public boolean wednesday = false;
    public boolean thursday = false;
    public boolean friday = false;

    public ViewWeekDays(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.view_days_selector, null);

        sat = rootView.findViewById(R.id.saturdayLabel_DaysSelectorView);
        sun = rootView.findViewById(R.id.sundayLabel_DaysSelectorView);
        mon = rootView.findViewById(R.id.mondayLabel_DaysSelectorView);
        tue = rootView.findViewById(R.id.tuesdayLabel_DaysSelectorView);
        wed = rootView.findViewById(R.id.wednesdayLabel_DaysSelectorView);
        thu = rootView.findViewById(R.id.thursdayLabel_DaysSelectorView);
        fri = rootView.findViewById(R.id.fridayLabel_DaysSelectorView);

        // Set onClickListener to all views
        sat.setOnClickListener(this);
        sun.setOnClickListener(this);
        mon.setOnClickListener(this);
        tue.setOnClickListener(this);
        wed.setOnClickListener(this);
        thu.setOnClickListener(this);
        fri.setOnClickListener(this);

        // Get theme color from Resource
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int themeColor = typedValue.data;
        // Set alpha(transparency) to theme color
        selectedColor = Color.argb(185, Color.red(themeColor), Color.green(themeColor), Color.blue(themeColor));
        
        deselectedColor = Color.argb(155,230,230,230);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.saturdayLabel_DaysSelectorView) {
            saturday = !saturday;
            if (saturday) {
                selectedWeekDays.add(Calendar.SATURDAY);
                ((TextView)v).setTextColor(Color.WHITE);
                v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            }
            else {
                selectedWeekDays.remove(Calendar.SATURDAY);
                ((TextView)v).setTextColor(Color.BLACK);
                v.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
            }

            if (listener != null) listener.dateChanged(selectedWeekDays);
        }
        else if (v.getId() == R.id.sundayLabel_DaysSelectorView) {
            sunday = !sunday;
            if (sunday) {
                selectedWeekDays.add(Calendar.SUNDAY);
                v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                ((TextView)v).setTextColor(Color.WHITE);
            }
            else {
                selectedWeekDays.remove(Calendar.SUNDAY);
                ((TextView)v).setTextColor(Color.BLACK);
                v.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
            }

            if (listener != null) listener.dateChanged(selectedWeekDays);
        }
        else if (v.getId() == R.id.mondayLabel_DaysSelectorView) {
            monday = !monday;
            if (monday) {
                selectedWeekDays.add(Calendar.MONDAY);
                v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                ((TextView)v).setTextColor(Color.WHITE);
            }
            else {
                selectedWeekDays.remove(Calendar.MONDAY);
                v.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
                ((TextView)v).setTextColor(Color.BLACK);
            }

            if (listener != null) listener.dateChanged(selectedWeekDays);
        }
        else if (v.getId() == R.id.tuesdayLabel_DaysSelectorView) {
            tuesday = !tuesday;
            if (tuesday) {
                selectedWeekDays.add(Calendar.TUESDAY);
                v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                ((TextView)v).setTextColor(Color.WHITE);
            }
            else {
                selectedWeekDays.remove(Calendar.TUESDAY);
                v.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
                ((TextView)v).setTextColor(Color.BLACK);
            }

            if (listener != null) listener.dateChanged(selectedWeekDays);
        }
        else if (v.getId() == R.id.wednesdayLabel_DaysSelectorView) {
            wednesday = !wednesday;
            if (wednesday) {
                selectedWeekDays.add(Calendar.WEDNESDAY);
                v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                ((TextView)v).setTextColor(Color.WHITE);
            }
            else {
                selectedWeekDays.remove(Calendar.WEDNESDAY);
                v.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
                ((TextView)v).setTextColor(Color.BLACK);
            }

            if (listener != null) listener.dateChanged(selectedWeekDays);
        }
        else if (v.getId() == R.id.thursdayLabel_DaysSelectorView) {
            thursday = !thursday;
            if (thursday) {
                selectedWeekDays.add(Calendar.THURSDAY);
                v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                ((TextView)v).setTextColor(Color.WHITE);
            }
            else {
                selectedWeekDays.remove(Calendar.THURSDAY);
                v.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
                ((TextView)v).setTextColor(Color.BLACK);
            }

            if (listener != null) listener.dateChanged(selectedWeekDays);
        }
        else if (v.getId() == R.id.fridayLabel_DaysSelectorView) {
            friday = !friday;
            if (friday) {
                selectedWeekDays.add(Calendar.FRIDAY);
                v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                ((TextView)v).setTextColor(Color.WHITE);
            }
            else {
                selectedWeekDays.remove(Calendar.FRIDAY);
                v.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
                ((TextView)v).setTextColor(Color.BLACK);
            }

            if (listener != null) listener.dateChanged(selectedWeekDays);
        }
    }

    public View getView() {
        return rootView;
    }
    public ViewWeekDays setSelectedWeekDays(HashSet<Integer> selectedWeekDays) {
        this.selectedWeekDays = selectedWeekDays;

        saturday = selectedWeekDays.contains(Calendar.SATURDAY);
        sunday = selectedWeekDays.contains(Calendar.SUNDAY);
        monday = selectedWeekDays.contains(Calendar.MONDAY);
        tuesday = selectedWeekDays.contains(Calendar.TUESDAY);
        wednesday = selectedWeekDays.contains(Calendar.WEDNESDAY);
        thursday = selectedWeekDays.contains(Calendar.THURSDAY);
        friday = selectedWeekDays.contains(Calendar.FRIDAY);

        if (saturday) {
            sat.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            sat.setTextColor(Color.WHITE);
        }
        else {
            sat.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
            sat.setTextColor(Color.BLACK);
        }

        if (sunday) {
            sun.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            sun.setTextColor(Color.WHITE);
        }
        else {
            sun.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
            sun.setTextColor(Color.BLACK);
        }

        if (monday) {
            mon.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            mon.setTextColor(Color.WHITE);
        }
        else {
            mon.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
            mon.setTextColor(Color.BLACK);
        }

        if (tuesday) {
            tue.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            tue.setTextColor(Color.WHITE);
        }
        else {
            tue.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
            tue.setTextColor(Color.BLACK);
        }

        if (wednesday) {
            wed.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            wed.setTextColor(Color.WHITE);
        }
        else {
            wed.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
            wed.setTextColor(Color.BLACK);
        }

        if (thursday) {
            thu.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            thu.setTextColor(Color.WHITE);
        }
        else {
            thu.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
            thu.setTextColor(Color.BLACK);
        }

        if (friday) {
            fri.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            fri.setTextColor(Color.WHITE);
        }
        else {
            fri.setBackgroundTintList(ColorStateList.valueOf(deselectedColor));
            fri.setTextColor(Color.BLACK);
        }

        return this;
    }

    public ViewWeekDays setListener(OnWeekDateChangedListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnWeekDateChangedListener {
        void dateChanged(HashSet<Integer> selectedWeekDays);
    }
}
