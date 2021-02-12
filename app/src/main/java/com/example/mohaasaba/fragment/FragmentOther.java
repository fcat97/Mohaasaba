package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.NotifyAdapter;
import com.example.mohaasaba.database.notify.Notify;
import com.example.mohaasaba.database.notify.NotifyRepository;
import com.example.mohaasaba.models.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentOther extends Fragment {
    private static final String TAG = "FragmentOverview";
    private FragmentOtherListeners listeners;
    private Schedule schedule;

    // Notification fields
    private RecyclerView notifyRecyclerView;
    private FloatingActionButton notifyAddButton;
    private NotifyAdapter notifyAdapter;
    private TextView notifyNoItemTextView;
    private EditText noteEditText;
    private NotifyRepository repository;

    public FragmentOther(Schedule schedule) {
        this.schedule = schedule;
        this.repository = new NotifyRepository(getContext());
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other, container, false);

        notifyRecyclerView = rootView.findViewById(R.id.recyclerView_FragmentReminder);
        notifyAddButton = rootView.findViewById(R.id.addReminder_FAB_FragmentReminder);
        notifyNoItemTextView = rootView.findViewById(R.id.noItem_FragmentReminder);
        noteEditText = rootView.findViewById(R.id.note_EditText_viewNote_FragmentOverView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noteEditText.setText(schedule.getNote());
        noteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: " + s.toString());
                schedule.setNote(s.toString());
            }
        });

        // Notify Related --------------------------------------------------------------------------
        repository = new NotifyRepository(getContext());
        notifyAdapter = new NotifyAdapter();
        notifyRecyclerView.setAdapter(notifyAdapter);
        LiveData<List<Notify>> notifyListLiveData = repository.getNotifyOf(schedule.getScheduleID());
        notifyListLiveData.observe(getViewLifecycleOwner(), notifies -> {
            if (notifies.size() == 0) notifyNoItemTextView.setVisibility(View.VISIBLE);
            else notifyNoItemTextView.setVisibility(View.INVISIBLE);
            notifyAdapter.submitList(notifies);
        });


        notifyAddButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            if (listeners != null) listeners.onEditNotify(new Notify(
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), schedule.getScheduleID()));
            else throw new ClassCastException("Must Implement Listeners");
        });


        notifyAdapter.setListener(new NotifyAdapter.onItemClickedListener() {
            @Override
            public void onItemClicked(Notify notify) {
                if (listeners != null) listeners.onEditNotify(notify);
                else throw new ClassCastException("Must Implement Listeners");
            }

            @Override
            public void onItemDeleted(Notify notify) {
                repository.deleteNotify(notify);
//                deletable.add(notify);
//                int position = notifyAdapter.getCurrentList().indexOf(notify);
//                notifyAdapter.notifyItemRemoved(position);
//                notifyAdapter.notifyItemRangeChanged(position, notifyAdapter.getItemCount());
            }
        });
    }


    public void notifyEditConfirmed(Notify notify) {
//        Log.d(TAG, "notifyEditConfirmed: called");
//        Log.d(TAG, "notifyEditConfirmed: notify name " + notify.message);
//        if (! notifyList.contains(notify)) notifyList.add(notify);
//        notifyAdapter.submitList(notifyList);
//        notifyAdapter.notifyDataSetChanged();
//        if (notifyList.size() > 0) notifyNoItemTextView.setVisibility(View.INVISIBLE);
        notify.notifyOwnerID = schedule.getScheduleID();
        repository.insertNotify(notify);
    }
    public interface FragmentOtherListeners {
        void onEditNotify(Notify notify);
    }
    public void setListeners(FragmentOtherListeners listeners) {
        this.listeners = listeners;
    }
}