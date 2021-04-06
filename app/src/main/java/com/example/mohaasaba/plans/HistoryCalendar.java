package com.example.mohaasaba.plans;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class HistoryCalendar {
    private Context context;
    private RecyclerView recyclerView;
    private CalendarAdapter adapter;

    public HistoryCalendar(Context context) {
        this.context = context;
        this.recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 7));
        adapter = new CalendarAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void submitProgress(List<Float> progress, int year, int month) {
        adapter.submitProgress(progress, year, month);
    }

    public View getView() {
        return recyclerView;
    }

    static class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayHolder> {
        private final String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        private final List<String> daysNames = new ArrayList<>(Arrays.asList(days));

        // Color Properties
        private int _weekDays_BGCOLOR = Color.TRANSPARENT;
        private int _weekDays_TEXTCOLOR = Color.BLUE;
        private int _weekend_TEXTCOLOR = Color.RED;

        /** Current Month Setting */
        private int _currentMonthDays_TEXTCOLOR = Color.BLACK;
        private int _currentMonthDays_BGCOLOR = Color.TRANSPARENT;
        private int _selectedDated_TEXTCOLOR = Color.BLACK;
        private int _selectedDated_TEXTCOLOR_ALT = Color.WHITE;
        private int _selectedDates_BGCOLOR = Color.GREEN;

        /** Other Months setting */
        private int _otherMonthDays_TEXTCOLOR = Color.LTGRAY;
        private int _otherMonthDays_BGCOLOR = Color.TRANSPARENT;

        private List<Float> progress;
        private List<Integer> alpha;

        private int _day_in_month;
        private int _starting_offset;
        private int _ending_offset;
        private List<Calendar> daysOfMonth;

        public CalendarAdapter() {
            Calendar calendar = Calendar.getInstance();

            this.progress = new ArrayList<>();
            invalidate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        }

//        public void submitProgress(List<Float> progress, int year, int month) {
//            this.progress = progress;
//            this.month = month;
//            this.year = year;
//
//            // get max_day_of_month
//            Calendar c = Calendar.getInstance();
//            c.clear();
//            c.set(year, month, 1);
//            _day_in_month = c.getActualMaximum(Calendar.DAY_OF_MONTH);
//            _starting_offset = c.get(Calendar.DAY_OF_WEEK) - 1;
//            _ending_offset = _starting_offset + _day_in_month <= 35 ?
//                    35 - (_starting_offset + _day_in_month) : 42 - (_starting_offset + _day_in_month);
//
//            // Get days that will show on Calendar
//            daysOfMonth = new ArrayList<>();
//            int _1st = c.get(Calendar.DAY_OF_YEAR);
//            for (int i = _1st - _starting_offset; i < _1st + _day_in_month + _ending_offset; i++) {
//                Calendar c1 = Calendar.getInstance();
//                c1.clear();
//                c1.set(Calendar.DAY_OF_YEAR, i);
//                daysOfMonth.add(c);
//            }
//        }
        public void invalidate(int year, int month) {
            // get max_day_of_month
            Calendar c = Calendar.getInstance();
            c.clear();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, 1);


            _day_in_month = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            _starting_offset = c.get(Calendar.DAY_OF_WEEK) - 1;
            _ending_offset = _starting_offset + _day_in_month <= 35 ?
                    35 - (_starting_offset + _day_in_month) : 42 - (_starting_offset + _day_in_month);

            // Get days that will show on Calendar
            daysOfMonth = new ArrayList<>();
            int _1st = c.get(Calendar.DAY_OF_YEAR);
            for (int i = _1st - _starting_offset; i < _1st + _day_in_month + _ending_offset; i++) {
                Calendar c1 = Calendar.getInstance();
                c1.clear();
                c1.set(Calendar.DAY_OF_YEAR, i);
                daysOfMonth.add(c1);
            }

            // Create alpha list for this month
            alpha = new ArrayList<>();
            if (progress.size() != _day_in_month) try {
                throw new Exception("Progress List Size not match with month");
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Float f : progress) {
                alpha.add(getAlpha(f));
            }
        }

        public void submitProgress(List<Float> progress, int year, int month) {
            this.progress = progress;
            invalidate(year, month);
            notifyDataSetChanged();
        }

        private int getAlpha(Float progress) {
            return (int) (progress * 255 / 100);
        }

        @NonNull
        @Override
        public DayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DayHolder(new TextView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(@NonNull DayHolder holder, int position) {
            if (position < 7) {
                holder.textView.setText(daysNames.get(position));
                holder.textView.setBackgroundColor(_weekDays_BGCOLOR);
                holder.textView.setTextColor(_weekDays_TEXTCOLOR);
            }
            else {
                /* Setting for other month days */
                if (position < 7 + _starting_offset || position > 7 + _starting_offset + _day_in_month - 1) {
                    holder.textView.setTextColor(_otherMonthDays_TEXTCOLOR);
                    holder.textView.setBackgroundColor(_otherMonthDays_BGCOLOR);
                }
                // Setting for this month
                else {
                    /* if no progress found */
                    if (alpha.get(position - 7 - _starting_offset) == 0) {
                        holder.textView.setTextColor(_currentMonthDays_TEXTCOLOR);
                        holder.textView.setBackgroundColor(_currentMonthDays_BGCOLOR);
                    }
                    /* if progress present */
                    else {
                        int a = alpha.get(position - 7 - _starting_offset);
                        ShapeDrawable drawable = new ShapeDrawable();
                        drawable.getPaint().setColor(_selectedDates_BGCOLOR);
                        drawable.getPaint().setAlpha(a);
                        holder.textView.setBackground(drawable);
                        if (a < 150) holder.textView.setTextColor(_selectedDated_TEXTCOLOR);
                        else holder.textView.setTextColor(_selectedDated_TEXTCOLOR_ALT);
                    }
                }

                /* Set All the days number to textView */
                holder.textView.setText(String.valueOf(daysOfMonth.get(position - 7).get(Calendar.DAY_OF_MONTH)));
            }
        }

        @Override
        public int getItemCount() {
            return _starting_offset + _day_in_month + _ending_offset + 7;
        }

        static class DayHolder extends RecyclerView.ViewHolder {
            private TextView textView;

            public DayHolder(@NonNull View itemView) {
                super(itemView);
                this.textView = (TextView) itemView;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setPadding(4,4,4,4);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    textView.setAutoSizeTextTypeUniformWithConfiguration(6,24,
                            1, TypedValue.COMPLEX_UNIT_DIP);
                }
            }
        }
    }
}
