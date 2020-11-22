package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.History;
import com.example.mohaasaba.database.Note;
import com.example.mohaasaba.database.Reminder;
import com.example.mohaasaba.helper.ViewMaker;

import java.util.Calendar;

public class FragmentOverview extends Fragment {
    private static final String TAG = "FragmentOverview";
    private FrameLayout mDescriptionFrameLayout;
    private FrameLayout mReminderFrameLayout;
    private ViewMaker viewMaker;
    private FragmentOverviewCallbacks fragmentOverviewCallbacks;

    public FragmentOverview() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {

        }*/
        viewMaker = new ViewMaker(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        mDescriptionFrameLayout = rootView.findViewById(R.id.description_frameLayout_OverviewFragment);
        mReminderFrameLayout = rootView.findViewById(R.id.reminder_frameLayout_OverviewFragment);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentOverviewCallbacks.onReach();
    }


    public void setNoteView(Note note) {
        mDescriptionFrameLayout.removeAllViews();
        if (note != null) {
            View noteView = viewMaker.getNoteView(note);
            mDescriptionFrameLayout.addView(noteView);
            mDescriptionFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mDescriptionFrameLayout.setVisibility(View.GONE);
        }
    }
    public void setReminderView(Reminder reminder) {
        mReminderFrameLayout.removeAllViews();
        if (reminder != null) {
            View reminderView = viewMaker.getReminderView(reminder);
            mReminderFrameLayout.addView(reminderView);
            mReminderFrameLayout.setVisibility(View.VISIBLE);
        } else mReminderFrameLayout.setVisibility(View.GONE);
    }

    public interface FragmentOverviewCallbacks {
        void onReach();
    }
    public void setFragmentOverviewCallbacks(FragmentOverviewCallbacks fragmentOverviewCallbacks) {
        this.fragmentOverviewCallbacks = fragmentOverviewCallbacks;
    }
}