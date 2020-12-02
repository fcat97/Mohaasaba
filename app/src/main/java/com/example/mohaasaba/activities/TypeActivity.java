package com.example.mohaasaba.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mohaasaba.R;
import com.example.mohaasaba.models.ScheduleType;
import com.example.mohaasaba.fragment.FragmentCustomDates;
import com.example.mohaasaba.fragment.FragmentDaily;
import com.example.mohaasaba.fragment.FragmentInterval;
import com.google.android.material.tabs.TabLayout;

public class TypeActivity extends AppCompatActivity {
    private static final String TAG = "TypeActivity";
    public static final String SCHEDULE_TYPE = "com.example.mohasabap.ScheduleType";
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FragmentDaily mFragmentDaily;
    private FragmentCustomDates mFragmentCustomDates;
    private FragmentInterval mFragmentInterval;

    private ScheduleType scheduleType;

    private Button confirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int themeID = getIntent().getIntExtra(AddScheduleActivity.EXTRA_THEME_ID, -1001);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        tabLayout = findViewById(R.id.tabLayout_TypeActivity);
        viewPager = findViewById(R.id.viewPager_TypeActivity);

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

        if (scheduleType.type == ScheduleType.Type.CustomDates) {
            viewPager.setCurrentItem(1);
            tabLayout.selectTab(tabLayout.getTabAt(1));
        }
        else if (scheduleType.type == ScheduleType.Type.Intervals) {
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
            mFragmentDaily.getCbSaturday().setChecked(scheduleType.everySaturday);
            mFragmentDaily.getCbSunday().setChecked(scheduleType.everySunday);
            mFragmentDaily.getCbMonday().setChecked(scheduleType.everyMonday);
            mFragmentDaily.getCbTuesday().setChecked(scheduleType.everyTuesday);
            mFragmentDaily.getCbWednesday().setChecked(scheduleType.everyWednesday);
            mFragmentDaily.getCbThursday().setChecked(scheduleType.everyThursday);
            mFragmentDaily.getCbFriday().setChecked(scheduleType.everyFriday);
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
            ScheduleType.Interval interval = scheduleType.getInterval();
            if (interval != null) {
                mFragmentInterval.setFragmentData(interval.activeDays, interval.inactiveDays, interval.isContinuous);
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
        scheduleType.initialize();
        if (tabLayout.getSelectedTabPosition() == 0) {
            scheduleType.everySaturday = mFragmentDaily.getCbSaturday().isChecked();
            scheduleType.everySunday = mFragmentDaily.getCbSunday().isChecked();
            scheduleType.everyMonday = mFragmentDaily.getCbMonday().isChecked();
            scheduleType.everyTuesday = mFragmentDaily.getCbTuesday().isChecked();
            scheduleType.everyWednesday = mFragmentDaily.getCbWednesday().isChecked();
            scheduleType.everyThursday = mFragmentDaily.getCbThursday().isChecked();
            scheduleType.everyFriday = mFragmentDaily.getCbFriday().isChecked();

            scheduleType.type = ScheduleType.Type.WeekDays;
            Log.d(TAG, "onConfirm: WeekDays");
            finishActivity();
        }
        else if (tabLayout.getSelectedTabPosition() == 1) {
            if (mFragmentCustomDates.getSelectedDates().size() == 0) {
                Toast.makeText(this, "No Dates Selected", Toast.LENGTH_SHORT).show();
                return;
            }
            scheduleType.type = ScheduleType.Type.CustomDates;
            scheduleType.setSelectedDatesFromCalender(mFragmentCustomDates.getSelectedDates());
            finishActivity();
        }
        else if (tabLayout.getSelectedTabPosition() == 2) {
            if (mFragmentInterval.getActiveDays() < 1 || mFragmentInterval.getInactiveDays() < 1) {
                Toast.makeText(this, "Invalid Interval", Toast.LENGTH_SHORT).show();
                return;
            }
            scheduleType.type = ScheduleType.Type.Intervals;
            scheduleType.setInterval(mFragmentInterval.getActiveDays(), mFragmentInterval.getInactiveDays(), mFragmentInterval.isContinuous());
            finishActivity();
        }
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