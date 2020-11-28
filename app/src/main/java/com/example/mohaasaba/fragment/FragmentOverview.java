package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.NotifyAdapter;
import com.example.mohaasaba.database.Note;
import com.example.mohaasaba.database.Notify;
import com.example.mohaasaba.helper.ViewMaker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class FragmentOverview extends Fragment {
    private static final String TAG = "FragmentOverview";
    private FrameLayout mDescriptionFrameLayout;
    private ViewMaker viewMaker;
    private FragmentOverviewListeners listeners;

    // Notification fields
    private RecyclerView notifyRecyclerView;
    private FloatingActionButton notifyAddButton;
    private List<Notify> notifyList;
    private NotifyAdapter notifyAdapter;
    private TextView notifyNoItemTextView;

    public FragmentOverview(List<Notify> notifyList) {
        this.notifyList = notifyList;
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

        notifyRecyclerView = rootView.findViewById(R.id.recyclerView_FragmentReminder);
        notifyAddButton = rootView.findViewById(R.id.addReminder_FAB_FragmentReminder);
        notifyNoItemTextView = rootView.findViewById(R.id.noItem_FragmentReminder);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (listeners != null) listeners.onReach();
        else throw new ClassCastException("Must implement Listeners");



        // Notify Related --------------------------------------------------------------------------
        if (notifyList.size() == 0) notifyNoItemTextView.setVisibility(View.VISIBLE);
        notifyAdapter = new NotifyAdapter();
        notifyRecyclerView.setAdapter(notifyAdapter);
        notifyAdapter.submitList(notifyList);

        notifyAddButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            if (listeners != null) listeners.onEditNotify(new Notify(
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
            else throw new ClassCastException("Must Implement Listeners");
        });


        notifyAdapter.setListener(new NotifyAdapter.onItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                if (listeners != null) listeners.onEditNotify(notifyList.get(position));
                else throw new ClassCastException("Must Implement Listeners");
            }

            @Override
            public void onItemDeleted(int position) {
                notifyList.remove(position);
                notifyAdapter.notifyItemRemoved(position);
                notifyAdapter.notifyItemRangeChanged(position, notifyAdapter.getItemCount());
            }
        });
        //------------------------------------------------------------------------------------------
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

    public void notifyEditConfirmed(Notify notify) {
        Log.d(TAG, "notifyEditConfirmed: called");
        Log.d(TAG, "notifyEditConfirmed: notify name " + notify.message);
        if (! notifyList.contains(notify)) notifyList.add(notify);
        notifyAdapter.submitList(notifyList);
        notifyAdapter.notifyDataSetChanged();
        if (notifyList.size() > 0) notifyNoItemTextView.setVisibility(View.INVISIBLE);
    }
    public interface FragmentOverviewListeners {
        void onReach();
        void onEditNotify(Notify notify);
    }
    public void setListeners(FragmentOverviewListeners listeners) {
        this.listeners = listeners;
    }
}