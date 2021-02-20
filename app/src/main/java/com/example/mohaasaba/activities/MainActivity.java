package com.example.mohaasaba.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.notify.Notify;
import com.example.mohaasaba.database.notify.NotifyRepository;
import com.example.mohaasaba.dialog.DialogAddSchedule;
import com.example.mohaasaba.fragment.FragmentScheduleOptions;
import com.example.mohaasaba.fragment.FragmentSearchMain;
import com.example.mohaasaba.helper.TabPagerBinder;
import com.example.mohaasaba.models.Schedule;
import com.example.mohaasaba.fragment.FragmentMainActivity;
import com.example.mohaasaba.receivers.BootReceiver;
import com.example.mohaasaba.receivers.NotificationScheduler;
import com.example.mohaasaba.viewModel.ScheduleViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import soup.neumorphism.NeumorphImageView;

public class MainActivity extends AppCompatActivity implements FragmentMainActivity.FragmentCallbacks{
    public static final int ADD_NEW_SCHEDULE_REQUEST = 1121;
    public static final int EDIT_SCHEDULE_REQUEST = 1122;
    private static final String TAG = "MainActivity";

    private ScheduleViewModel mScheduleViewModel;
    private AlarmManager mAlarmManager;

    private FragmentMainActivity mTodayFragment;
    private FragmentMainActivity mAllSchedulesFragment;
    private FragmentMainActivity mThisWeekFragment;
    private FragmentMainActivity mThisMonthFragment;
    private FragmentSearchMain fragmentSearchMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: called");

//        Toolbar toolbar = findViewById(R.id.toolbar_mainActivity);
//        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        mScheduleViewModel = new ScheduleViewModel(getApplication());


        /* Enable the BootReceiver Broadcast Receiver explicitly
         * This receiver is disabled in Manifest by default
         * The following action will enable it */
        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        /*
        //Implementing swipe action
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    try {
                        removeAttachments(adapter.getItemAt(viewHolder.getAdapterPosition()));
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    mScheduleViewModel.deleteSchedule(adapter.getItemAt(viewHolder.getAdapterPosition()));
                    Toast.makeText(MainActivity.this, R.string.item_removed_toast, Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(mRecyclerView);*/

        LiveData<List<Schedule>> todaySchedules = mScheduleViewModel.getSchedulesOfToday();
        LiveData<List<Schedule>> thisWeekSchedules = mScheduleViewModel.getSchedulesOfToday();
        LiveData<List<Schedule>> thisMonthSchedules = mScheduleViewModel.getSchedulesOfToday();
        LiveData<List<Schedule>> allSchedules = mScheduleViewModel.getSchedulesOfToday();

        mTodayFragment = new FragmentMainActivity(todaySchedules);
        mAllSchedulesFragment = new FragmentMainActivity(allSchedules);
        mThisWeekFragment = new FragmentMainActivity(thisWeekSchedules);
        mThisMonthFragment = new FragmentMainActivity(thisMonthSchedules);
        fragmentSearchMain = new FragmentSearchMain(allSchedules);


        mTodayFragment.setCallbacks(this);
        mAllSchedulesFragment.setCallbacks(this);
        mThisWeekFragment.setCallbacks(this);
        mThisMonthFragment.setCallbacks(this);
        fragmentSearchMain.setCallbacks(new FragmentSearchMain.FragmentCallbacks() {
            @Override
            public void onItemClicked(Schedule schedule) {
                openEditScheduleActivity(schedule);
            }

            @Override
            public void onItemLongClicked(Schedule schedule) throws ExecutionException, InterruptedException {
                openScheduleOptions(schedule);
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabLayout_MainActivity);
        ViewPager2 viewPager = findViewById(R.id.viewpager_MainActivity);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle()));
        new TabPagerBinder(tabLayout, viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rescheduleNotification();
    }

    private void rescheduleNotification() {
        Log.d(TAG, "rescheduleNotification: called");
        Intent intent = new Intent(this, NotificationScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NotificationScheduler.MIDNIGHT_REQUEST_PID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            pendingIntent.send(getApplicationContext(), 10099, intent);
            Log.d(TAG, "rescheduleNotification: done");
        } catch (PendingIntent.CanceledException e) {
            Log.d(TAG, "rescheduleNotification: failed");
            e.printStackTrace();
        }
    }

    /**
     * Method to remove all associated Database Entities when Schedule is deleted
     * @param schedule deleted schedule*/
    private void removeAttachments(Schedule schedule) {
        NotifyRepository repository = new NotifyRepository(this);
        repository.deleteNotifyIDs(schedule.getScheduleID());
        rescheduleNotification();
    }

    public void openAddScheduleActivity(View view) {
        DialogAddSchedule dialogAddSchedule = new DialogAddSchedule();
        dialogAddSchedule.show(getSupportFragmentManager(), "Dialogggg");
//        Intent intent = new Intent(this,AddScheduleActivity.class);
//        startActivityForResult(intent,ADD_NEW_SCHEDULE_REQUEST);
    }

    private void openEditScheduleActivity(Schedule schedule) {
        Intent intent = new Intent(this,AddScheduleActivity.class);
        intent.putExtra(AddScheduleActivity.EXTRA_SCHEDULE,schedule);
        Bundle typeBundle = new Bundle();
        typeBundle.putParcelable(AddScheduleActivity.EXTRA_SCHEDULE_TYPE, schedule.getScheduleType());
        intent.putExtra("TYPE_BUNDLE", typeBundle); /* intent is not passing ScheduleType; I don't know why!!!!? */
        startActivityForResult(intent,EDIT_SCHEDULE_REQUEST);
    }

    private void openScheduleOptions(Schedule schedule) {
        FragmentScheduleOptions fragmentScheduleOptions = new FragmentScheduleOptions();
        fragmentScheduleOptions.show(getSupportFragmentManager(), "Fragment Schedule Option");
        fragmentScheduleOptions.setListeners(new FragmentScheduleOptions.FragmentScheduleOptionListeners() {
            @Override
            public void onCopyButtonClicked() {
                mScheduleViewModel.copySchedule(schedule);
            }

            @Override
            public void onDeleteButtonClicked() {
                removeAttachments(schedule);
                mScheduleViewModel.deleteSchedule(schedule);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NEW_SCHEDULE_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, R.string.success_msg, Toast.LENGTH_SHORT).show();
        }

        if (requestCode == EDIT_SCHEDULE_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, R.string.success_msg, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClicked(Schedule schedule) {
        openEditScheduleActivity(schedule);
    }

    @Override
    public void onItemLongClicked(Schedule schedule) {
        openScheduleOptions(schedule);
    }

    private final class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return mTodayFragment;
                case 1:
                    return mThisWeekFragment;
                case 2:
                    return mThisMonthFragment;
                case 3:
                    return mAllSchedulesFragment;
                case 4:
                    return fragmentSearchMain;
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}