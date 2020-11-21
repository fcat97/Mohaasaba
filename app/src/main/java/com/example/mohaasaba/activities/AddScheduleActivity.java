package com.example.mohaasaba.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.DataConverter;
import com.example.mohaasaba.database.Note;
import com.example.mohaasaba.database.Reminder;
import com.example.mohaasaba.database.Schedule;
import com.example.mohaasaba.database.ScheduleType;
import com.example.mohaasaba.dialog.DialogColorPicker;
import com.example.mohaasaba.dialog.DialogDatePicker;
import com.example.mohaasaba.fragment.FragmentOther;
import com.example.mohaasaba.fragment.FragmentOverview;
import com.example.mohaasaba.fragment.FragmentTodo;
import com.example.mohaasaba.helper.ThemeUtils;
import com.example.mohaasaba.viewModel.AddScheduleViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddScheduleActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {
    public static final int ADD_NEW_NOTE_REQUEST = 2131;
    public static final int EDIT_NOTE_REQUEST = 2132;
    public static final int ADD_REMINDER_REQUEST = 2141;
    public static final int EDIT_REMINDER_REQUEST = 2142;
    public static final int EDIT_SCHEDULE_TYPE_REQUEST = 2152;
    public static final String EXTRA_SCHEDULE = "com.example.mohasabap.EXTRA_SCHEDULE";
    public static final String EXTRA_THEME_ID = "com.example.mohasabap.EXTRA_SCHEDULE";
    public static final String EXTRA_SCHEDULE_TYPE = "com.example.mohasabap.EXTRA_SCHEDULE_TYPE";
    private static final String TAG = "AddScheduleActivity";

    private  boolean showAddNoteMenu = true;

    private AddScheduleViewModel mViewModel;
    private EditText mTitleEditText;
    private EditText mTagEditText;
    private TextView mScheduleTypeTextView;
    private TabLayout tabLayout;
    private ImageView mReminderButton;
    private FragmentOverview fragmentOverview;
    private FragmentOther fragmentOther;
    private FragmentTodo fragmentTodo;
    private boolean isReached = false;
    private boolean isFragmentOtherCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra(EXTRA_SCHEDULE)) {
            int themeID = ((Schedule)getIntent().getParcelableExtra(EXTRA_SCHEDULE)).getThemeID();
            setTheme(ThemeUtils.getResourceID(themeID));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }
        Toolbar mToolbar = findViewById(R.id.toolbar_addScheduleActivity);
        setSupportActionBar(mToolbar);

        ViewPager2 viewPager = findViewById(R.id.viewPager_AddScheduleActivity);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle()));
        tabLayout = findViewById(R.id.tabLayout_AddScheduleActivity);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        mTitleEditText = findViewById(R.id.title_EditText_addSchedule);
        mTagEditText = findViewById(R.id.tag_EditText_addSchedule);
        mReminderButton = findViewById(R.id.reminderButton_Header_addSchedule);
        mScheduleTypeTextView = findViewById(R.id.scheduleType_TextView_Add_Schedule);
        mReminderButton.setOnClickListener(v -> {
            openAddReminderActivity();
        });

        try {
            setIntentData(); /*For Edit Schedule*/
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        fragmentOverview = new FragmentOverview();
        fragmentOverview.setFragmentOverviewCallbacks(new FragmentOverview.FragmentOverviewCallbacks() {
            @Override
            public void onReach() {
                isReached = true;
                if (mViewModel.getReminder() != null) setReminderView(mViewModel.getReminder());
                if (mViewModel.getNote() != null) setNoteView(mViewModel.getNote());
            }
        });

        fragmentTodo = new FragmentTodo(mViewModel.getSchedule().getHistory());
        fragmentTodo.setFragmentListeners(() -> {
            openDatePickerDialog();
        });

        fragmentOther = new FragmentOther(new FragmentOther.FragmentOtherCallbacks() {
            @Override
            public void onReach() {
                isFragmentOtherCreated = true;
                if (mViewModel.getSubScheduleList() != null) setSubSchedule(mViewModel.getSubScheduleList());
            }

            @Override
            public void onAddButtonClicked() {
                //TODO: open a search activity & onclick add schedule
                // may be later some day...
            }
        });

    }

    private void setIntentData() throws ExecutionException, InterruptedException {
        Intent intent = getIntent();
        if (mViewModel != null) {
            return;
        }
        /*
         *This block checks what is the request of this Activity
         * if -> Edit Schedule Request
         * else -> Add Schedule Request
         * */
        if (intent.hasExtra(EXTRA_SCHEDULE)) {
            Schedule schedule = intent.getParcelableExtra(EXTRA_SCHEDULE);
            Bundle type = intent.getBundleExtra("TYPE_BUNDLE"); /*intent.putExtra() not working...*/
            assert schedule != null;
            schedule.setScheduleType(type.getParcelable(EXTRA_SCHEDULE_TYPE));
            mViewModel = new ViewModelProvider(this).get(AddScheduleViewModel.class);
            mViewModel.init(getApplication(), schedule);

            mViewModel.setScheduleEdited(true); /* if schedule exists flag it for update */

            mTitleEditText.setText(schedule.getTitle());
            if (schedule.getTags().size() != 0) mTagEditText.setText(
                    DataConverter.joinListToString(schedule.getTags()));
            if (mViewModel.getNoteLiveData() != null){
                showAddNoteMenu = false;
                mViewModel.getNoteLiveData().observe(this, new Observer<Note>() {
                    @Override
                    public void onChanged(Note note) {
                        if (isReached) setNoteView(note);
                        mViewModel.setNote(note);
                    }
                });
            }
            if (mViewModel.getReminder() != null) {
                mReminderButton.setVisibility(View.GONE);
                if (isReached) setReminderView(mViewModel.getReminder());
            }
            if (mViewModel.getSubScheduleLiveData() != null) {
                mViewModel.getSubScheduleLiveData().observe(this, new Observer<List<Schedule>>() {
                    @Override
                    public void onChanged(List<Schedule> schedules) {
                        mViewModel.setSubScheduleList(schedules);
                        if (isFragmentOtherCreated) setSubSchedule(schedules);
                    }
                });
            }

            /* Change the ScheduleType TextView Text */
            invalidateScheduleTypeTextView(mViewModel.getScheduleType());
        } else {
            Log.d(TAG, "setIntentData: no Extra");
            mViewModel = new ViewModelProvider(this).get(AddScheduleViewModel.class);
            mViewModel.init(getApplication(), null);
//            mViewModel = new AddScheduleViewModel(getApplication());
        }
    }

    /* Menu Related Methods */
    /* Option Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_schedule_activity, menu);

        if (!showAddNoteMenu) menu.findItem(R.id.addNote_menuItem_addSchedule).setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                /*
                 * to prevent parent activity get destroyed
                 * */
                onBackPressed();
                return true;
            case R.id.save_menuItem_addSchedule:
                try {
                    saveSchedule();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.addNote_menuItem_addSchedule:
                if (mViewModel.getNote() == null) openAddNoteActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* PopUp menu */
    public void showPopup_reminderView(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popup_add_schedule);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_menuItem_description_view:
                        openEditReminderActivity();
                        return true;
                    case R.id.delete_menuItem_description_view:
                        mViewModel.setReminder(null);
                        setReminderView(null);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
    public void showPopup_noteView(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popup_add_schedule);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_menuItem_description_view:
                        openEditNoteActivity();
                        return true;
                    case R.id.delete_menuItem_description_view:
                        mViewModel.setNote(null);
                        setNoteView(null);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
    public void showTypeActivity_header(View v) {
        openEditScheduleTypeActivity();
    }

    /** Dialog Related Methods */
    /*private void openDatePickerDialog() {
        DialogDatePicker datePickerDialog = new DialogDatePicker();
        datePickerDialog.show(getSupportFragmentManager(),"date picker");
    }*/
    /*public void openTargetPickerDialog(View v) {
        History.Progress progress = mViewModel.getSchedule().getHistory().getProgressOf(Calendar.getInstance());
        DialogTargetEditor dialogTargetEditor = new DialogTargetEditor(progress, new DialogTargetEditor.TargetEditorListeners() {
            @Override
            public void onConfirm(int maxProgress, String unit, int step, boolean onTodo) {
                History.Progress newProgress = new History.Progress();
                newProgress.maxProgress = maxProgress;
                newProgress.unit = unit;
                newProgress.progressStep = step;
                newProgress.onTodo = onTodo;

                mViewModel.updateProgress(newProgress);
                setTargetView(mViewModel.getSchedule().getHistory());
            }
        });
        dialogTargetEditor.show(getSupportFragmentManager(), "targetEditor");
    }*/
    public void openColorPickerDialog(View v) {
        DialogColorPicker colorPicker = new DialogColorPicker();
        colorPicker.setListener(themeID -> {
            mViewModel.getSchedule().setThemeID(themeID);
            if (! colorPicker.isDetached()) colorPicker.dismiss();
            startActivity(getIntent());
            this.finish();
        });
        colorPicker.showNow(getSupportFragmentManager(), "ColorPicker");
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, dayOfMonth);
        fragmentTodo.invalidateFragment(calendar);
    }
    public void openDatePickerDialog() {
        DialogDatePicker dialogDatePicker = new DialogDatePicker();
        dialogDatePicker.show(getSupportFragmentManager(), "Date Picker Dialog");
    }



    /** Activity related methods */
    private void openAddNoteActivity() {
        Intent intent = new Intent(this,NoteActivity.class);
        intent.putExtra(EXTRA_THEME_ID, mViewModel.getSchedule().getThemeID());
        startActivityForResult(intent,ADD_NEW_NOTE_REQUEST);
    }
    private void openEditNoteActivity() {
        Intent intent = new Intent(this,NoteActivity.class);
        intent.putExtra(NoteActivity.EXTRA_NOTE, mViewModel.getNote());
        intent.putExtra(EXTRA_THEME_ID, mViewModel.getSchedule().getThemeID());
        startActivityForResult(intent,EDIT_NOTE_REQUEST);
    }
    private void openAddReminderActivity() {
        Intent intent = new Intent(this,ReminderActivity.class);
        startActivityForResult(intent,ADD_REMINDER_REQUEST);
    }
    private void openEditReminderActivity() {
        Intent intent = new Intent(this,ReminderActivity.class);
        intent.putExtra(ReminderActivity.EXTRA_REMINDER,mViewModel.getReminder());
        startActivityForResult(intent,EDIT_REMINDER_REQUEST);
    }
    private void openEditScheduleTypeActivity() {
        Intent intent = new Intent(this,TypeActivity.class);
        intent.putExtra(AddScheduleActivity.EXTRA_SCHEDULE_TYPE, mViewModel.getScheduleType());
        startActivityForResult(intent, EDIT_SCHEDULE_TYPE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NEW_NOTE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            Note note = data.getParcelableExtra(NoteActivity.EXTRA_NOTE);
            assert note != null;
            setNoteView(note);
            mViewModel.setNote(note); //Don't worry note will not be null
        }

        if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            Note note = data.getParcelableExtra(NoteActivity.EXTRA_NOTE);
            mViewModel.setNote(note);
            setNoteView(note);
        }

        if (requestCode == ADD_REMINDER_REQUEST && resultCode == RESULT_OK) {
            Reminder reminder = data.getParcelableExtra(ReminderActivity.EXTRA_REMINDER);
            mViewModel.setReminder(reminder);
            setReminderView(reminder);
        }

        if (requestCode == EDIT_REMINDER_REQUEST && resultCode == RESULT_OK) {
            Reminder reminder = data.getParcelableExtra(ReminderActivity.EXTRA_REMINDER);
            mViewModel.setReminder(reminder);
            setReminderView(reminder);
        }

        if (requestCode == EDIT_SCHEDULE_TYPE_REQUEST && resultCode == RESULT_OK) {
            Bundle bundle = data.getBundleExtra("TYPE_BUNDLE");
            ScheduleType scheduleType = bundle.getParcelable(EXTRA_SCHEDULE_TYPE);
            mViewModel.setScheduleType(scheduleType);
            invalidateScheduleTypeTextView(scheduleType);
        }
    }

    /** Set Views methods*/
    private void setNoteView(Note note) {
        fragmentOverview.setNoteView(note);
        showAddNoteMenu = note == null;
        invalidateOptionsMenu();
    }
    private void setReminderView(Reminder reminder) {
        fragmentOverview.setReminderView(reminder);
        if (mViewModel.getReminder() == null)  mReminderButton.setVisibility(View.GONE);
        else mReminderButton.setVisibility(View.VISIBLE);
    }

    private void setSubSchedule(List<Schedule> scheduleList) {
        fragmentOther.submitList(scheduleList);
    }

    private void invalidateScheduleTypeTextView(ScheduleType scheduleType) {
        if (scheduleType.isType_daily()) mScheduleTypeTextView.setText(R.string.scheduleType_daily_text);
        else if (scheduleType.isType_day_specified()) mScheduleTypeTextView.setText(R.string.scheduleType_onWeekday);
        else if (scheduleType.isType_month_specified()) mScheduleTypeTextView.setText(R.string.scheduleType_onMonth);
        else if (scheduleType.isType_month_day_specified()) mScheduleTypeTextView.setText(R.string.scheduleType_onMonthAndDay);
        else if (scheduleType.isType_custom()) mScheduleTypeTextView.setText(R.string.scheduleType_custom_text);
        else if (scheduleType.isType_interval()) mScheduleTypeTextView.setText(R.string.scheduleType_interval_text);
    }

    /** On Save MenuItem Clicked */
    private void saveSchedule() throws ExecutionException, InterruptedException {

//        Set Schedule with Title and Tags
        String scheduleTitle = mTitleEditText.getText().toString().trim();
        if (scheduleTitle.isEmpty()){
//            if Title is empty return
            Toast.makeText(this, R.string.title_request_warning, Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            /* create new Schedule */
            if (mViewModel.getSchedule() == null) mViewModel.setSchedule(new Schedule(scheduleTitle));
                /* Edit existing Schedule */
            else {
                Schedule newSchedule = mViewModel.getSchedule();
                newSchedule.setTitle(scheduleTitle);
                if (!mTagEditText.getText().toString().trim().isEmpty()){
                    newSchedule.setTags(DataConverter.splitStringToList(mTagEditText.getText().toString().trim()));
                }
                mViewModel.setSchedule(newSchedule);
            }
        }
        /* Insert associated objects */
        if (mViewModel.getNote() != null) mViewModel.insertNote();
        else mViewModel.deleteNote();

        if (mViewModel.getReminder() != null) mViewModel.activateReminder(getApplicationContext());
        else mViewModel.deleteReminder(getApplicationContext());

        if (mViewModel.getSubScheduleList() != null) mViewModel.insertSubSchedules();
        if (mViewModel.getSchedule().getThemeID() == -1001) mViewModel.getSchedule().setThemeID(ThemeUtils.THEME_GREEN);

        /* Finally insert the schedule */
        mViewModel.insertSchedule();
        setResult(RESULT_OK);
        finish();
    }

    /** Classes & Interfaces */
    private class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return fragmentOverview;
                case 1:
                    return fragmentTodo;
                case 2:
                    return fragmentOther;
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}