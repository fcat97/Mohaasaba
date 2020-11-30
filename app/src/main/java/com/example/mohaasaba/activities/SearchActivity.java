package com.example.mohaasaba.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.RecyclerViewAdapter;
import com.example.mohaasaba.database.AppRepository;
import com.example.mohaasaba.models.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {
    private List<Schedule> scheduleArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        AppRepository repository = new AppRepository(getApplication());
        LiveData<List<Schedule>> listLiveData = repository.getAllSchedule();
        EditText searchEditText = findViewById(R.id.search_EditText_SearchActivity);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_SearchActivity);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        listLiveData.observe(this, schedules -> {
            scheduleArrayList = schedules;
            adapter.submitList(scheduleArrayList);
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = String.valueOf(s);
                adapter.submitList(search(string));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter.setOnClickListener(new RecyclerViewAdapter.OnClickListener() {
            @Override
            public void onItemClicked(Schedule schedule) {
                openEditScheduleActivity(schedule);
            }

            @Override
            public void onItemLongClicked(Schedule schedule) throws ExecutionException, InterruptedException {

            }
        });
    }

    private List<Schedule> search(String s) {
        List<Schedule> matchedList = new ArrayList<>();
        for (Schedule schedule : scheduleArrayList) {
            if (containsIgnoreCase(schedule.getTitle(),s)) matchedList.add(schedule);
        }

        return matchedList;
    }
    private boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }

    private void openEditScheduleActivity(Schedule schedule) {
        Intent intent = new Intent(this,AddScheduleActivity.class);
        intent.putExtra(AddScheduleActivity.EXTRA_SCHEDULE,schedule);
        Bundle typeBundle = new Bundle();
        typeBundle.putParcelable(AddScheduleActivity.EXTRA_SCHEDULE_TYPE, schedule.getScheduleType());
        intent.putExtra("TYPE_BUNDLE", typeBundle); /* intent is not passing ScheduleType; I don't know why!!!!? */
        startActivityForResult(intent,MainActivity.EDIT_SCHEDULE_REQUEST);
    }
}