package com.example.mohaasaba.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.ScheduleType;
import com.example.mohaasaba.fragment.FragmentCustomDates;
import com.example.mohaasaba.fragment.FragmentDaily;
import com.example.mohaasaba.fragment.FragmentInterval;
import com.google.android.material.tabs.TabLayout;

import java.util.Map;

public class TypeActivity extends AppCompatActivity {
    private static final String TAG = "TypeActivity";
    public static final String SCHEDULE_TYPE = "com.example.mohasabap.ScheduleType";
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FragmentDaily mFragmentDaily;
    private FragmentCustomDates mFragmentCustomDates;
    private FragmentInterval mFragmentInterval;

    private ScheduleType scheduleType;

    private SwitchCompat swNotify;
    private Button notificationTimeButton;
    private Button confirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int themeID = getIntent().getIntExtra(AddScheduleActivity.EXTRA_THEME_ID, -1001);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        tabLayout = findViewById(R.id.tabLayout_TypeActivity);
        viewPager = findViewById(R.id.viewPager_TypeActivity);

        swNotify = findViewById(R.id.swNotify_TypeActivity);
        notificationTimeButton = findViewById(R.id.notificationTime_Button_TypeActivity);
        confirmButton = findViewById(R.id.confirm_button_TypeActivity);


        Intent intent = getIntent();
        if (intent.hasExtra(AddScheduleActivity.EXTRA_SCHEDULE_TYPE)) {
            scheduleType = intent.getParcelableExtra(AddScheduleActivity.EXTRA_SCHEDULE_TYPE);
        } else scheduleType = new ScheduleType();


        mFragmentDaily = new FragmentDaily();
        mFragmentCustomDates = new FragmentCustomDates();
        mFragmentInterval = new FragmentInterval();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        if (scheduleType.isType_custom()) {
            viewPager.setCurrentItem(1);
            tabLayout.selectTab(tabLayout.getTabAt(1));
        }
        else if (scheduleType.isType_interval()) {
            viewPager.setCurrentItem(2);
            tabLayout.selectTab(tabLayout.getTabAt(2));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
                super.onPageSelected(position);
            }
        });

        setFragmentsData();

        confirmButton.setOnClickListener(v -> {
            onConfirm();
        });

    }

    private void setFragmentsData() {
        setDailyFragmentData();
        setCustomDateFragmentData();
        setIntervalFragmentData();
    }

    private void setDailyFragmentData() {
        mFragmentDaily.setCallbacks(() -> {
            mFragmentDaily.getCbSaturday().setChecked(scheduleType.isEverySaturday());
            mFragmentDaily.getCbSunday().setChecked(scheduleType.isEverySunday());
            mFragmentDaily.getCbMonday().setChecked(scheduleType.isEveryMonday());
            mFragmentDaily.getCbTuesday().setChecked(scheduleType.isEveryTuesday());
            mFragmentDaily.getCbWednesday().setChecked(scheduleType.isEveryWednesday());
            mFragmentDaily.getCbThursday().setChecked(scheduleType.isEveryThursday());
            mFragmentDaily.getCbFriday().setChecked(scheduleType.isEveryFriday());

            mFragmentDaily.getCbJanuary().setChecked(scheduleType.isEveryJanuary());
            mFragmentDaily.getCbFebruary().setChecked(scheduleType.isEveryFebruary());
            mFragmentDaily.getCbMarch().setChecked(scheduleType.isEveryMarch());
            mFragmentDaily.getCbApril().setChecked(scheduleType.isEveryApril());
            mFragmentDaily.getCbMay().setChecked(scheduleType.isEveryMay());
            mFragmentDaily.getCbJune().setChecked(scheduleType.isEveryJune());
            mFragmentDaily.getCbJuly().setChecked(scheduleType.isEveryJuly());
            mFragmentDaily.getCbAugust().setChecked(scheduleType.isEveryAugust());
            mFragmentDaily.getCbSeptember().setChecked(scheduleType.isEverySeptember());
            mFragmentDaily.getCbOctober().setChecked(scheduleType.isEveryOctober());
            mFragmentDaily.getCbNovember().setChecked(scheduleType.isEveryNovember());
            mFragmentDaily.getCbDecember().setChecked(scheduleType.isEveryDecember());
        });

    }
    private void setCustomDateFragmentData() {
        mFragmentCustomDates.setCallbacks(() -> {
            if (scheduleType.getSelectedDatesAsCalender() != null)
                mFragmentCustomDates.setSelectedDates(scheduleType.getSelectedDatesAsCalender());
        });
    }
    private void setIntervalFragmentData() {
        mFragmentInterval.setCallbacks(() -> {
            Map<String, Integer> intervalData = scheduleType.getIntervalData();
            if (intervalData != null) {
                int activeDays = intervalData.get(ScheduleType.ACTIVE_DAYS);
                int inactiveDays = intervalData.get(ScheduleType.INACTIVE_DAYS);
                boolean continuous;
                continuous = intervalData.get(ScheduleType.CONTINUOUS_DAYS) != 0;
                mFragmentInterval.setFragmentData(activeDays,inactiveDays,continuous);
            }

        });
    }

    private class ViewPagerAdapter extends FragmentStateAdapter {


        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return mFragmentDaily;
                case 1:
                    return mFragmentCustomDates;
                case 2:
                    return mFragmentInterval;
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    public interface OnFragmentCreatedCallbacks {
        void onFragmentCreated();
    }

    private void onConfirm() {
        scheduleType.clearAll();
        if (tabLayout.getSelectedTabPosition() == 0) {

            if (mFragmentDaily.getCbSaturday().isChecked()) scheduleType.setEverySaturday(true);
            if (mFragmentDaily.getCbSunday().isChecked()) scheduleType.setEverySunday(true);
            if (mFragmentDaily.getCbMonday().isChecked()) scheduleType.setEveryMonday(true);
            if (mFragmentDaily.getCbTuesday().isChecked()) scheduleType.setEveryTuesday(true);
            if (mFragmentDaily.getCbWednesday().isChecked()) scheduleType.setEveryWednesday(true);
            if (mFragmentDaily.getCbThursday().isChecked()) scheduleType.setEveryThursday(true);
            if (mFragmentDaily.getCbFriday().isChecked()) scheduleType.setEveryFriday(true);

            if (mFragmentDaily.getCbJanuary().isChecked()) scheduleType.setEveryJanuary(true);
            if (mFragmentDaily.getCbFebruary().isChecked()) scheduleType.setEveryFebruary(true);
            if (mFragmentDaily.getCbMarch().isChecked()) scheduleType.setEveryMarch(true);
            if (mFragmentDaily.getCbApril().isChecked()) scheduleType.setEveryApril(true);
            if (mFragmentDaily.getCbMay().isChecked()) scheduleType.setEveryMay(true);
            if (mFragmentDaily.getCbJune().isChecked()) scheduleType.setEveryJune(true);
            if (mFragmentDaily.getCbJuly().isChecked()) scheduleType.setEveryJuly(true);
            if (mFragmentDaily.getCbAugust().isChecked()) scheduleType.setEveryAugust(true);
            if (mFragmentDaily.getCbSeptember().isChecked()) scheduleType.setEverySeptember(true);
            if (mFragmentDaily.getCbOctober().isChecked()) scheduleType.setEveryOctober(true);
            if (mFragmentDaily.getCbNovember().isChecked()) scheduleType.setEveryNovember(true);
            if (mFragmentDaily.getCbDecember().isChecked()) scheduleType.setEveryDecember(true);

            if (! isAnyWeekdaySelected() && ! isAnyMonthSelected()) {
                scheduleType.setType_daily(true);
                Log.d(TAG, "onConfirm: daily");
            } else if (isAnyMonthSelected() && ! isAnyWeekdaySelected()) {
                scheduleType.setType_month_specified(true);
                Log.d(TAG, "onConfirm: month");
            } else if (isAnyWeekdaySelected() && ! isAnyMonthSelected()) {
                scheduleType.setType_day_specified(true);
                Log.d(TAG, "onConfirm: weekday");
            } else if (isAnyMonthSelected() && isAnyWeekdaySelected()) {
                scheduleType.setType_month_day_specified(true);
                Log.d(TAG, "onConfirm: month & day");
            }
            finishActivity();
        }
        else if (tabLayout.getSelectedTabPosition() == 1) {
            if (mFragmentCustomDates.getSelectedDates().size() == 0) {
                Toast.makeText(this, "No Dates Selected", Toast.LENGTH_SHORT).show();
                return;
            }
            scheduleType.setType_custom(true);
            scheduleType.setSelectedDatesFromCalender(mFragmentCustomDates.getSelectedDates());
            finishActivity();
        }
        else if (tabLayout.getSelectedTabPosition() == 2) {
            if (mFragmentInterval.getActiveDays() == 0 || mFragmentInterval.getInactiveDays() <=1) {
                Toast.makeText(this, "Invalid Interval", Toast.LENGTH_SHORT).show();
                return;
            }
            scheduleType.setType_interval(true);
            scheduleType.setInterval(mFragmentInterval.getActiveDays(), mFragmentInterval.getInactiveDays(), mFragmentInterval.isContinuous());
            finishActivity();
        }
    }

    private boolean isAnyWeekdaySelected() {
        return (scheduleType.isEverySaturday() | scheduleType.isEverySunday() | scheduleType.isEveryMonday() |
                scheduleType.isEveryTuesday() | scheduleType.isEveryWednesday() |
                scheduleType.isEveryThursday() | scheduleType.isEveryFriday());
    }

    private boolean isAnyMonthSelected() {
        return (scheduleType.isEveryJanuary() | scheduleType.isEveryFebruary() | scheduleType.isEveryMarch() |
                scheduleType.isEveryApril() | scheduleType.isEveryMay() | scheduleType.isEveryJune() | scheduleType.isEveryJuly() |
                scheduleType.isEveryAugust() | scheduleType.isEverySeptember() | scheduleType.isEveryOctober() |
                scheduleType.isEveryNovember() | scheduleType.isEveryDecember());
    }

    private void finishActivity() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddScheduleActivity.EXTRA_SCHEDULE_TYPE, scheduleType);
        intent.putExtra("TYPE_BUNDLE", bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}