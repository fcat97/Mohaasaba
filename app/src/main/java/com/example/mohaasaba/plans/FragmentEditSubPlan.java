package com.example.mohaasaba.plans;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.mohaasaba.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class FragmentEditSubPlan extends BottomSheetDialogFragment {
    public static final String EXTRA_SUBPLAN = "com.mohaasaba.EXTRA_SUBPLAN";
    private static final String TAG = FragmentEditSubPlan.class.getCanonicalName();

    private SubPlan mSubPlan;

    private EditText labelEditText;
    private TextView trackTimeTextView, trackCountTextView;
    private LinearLayout trackTime_ll, trackCount_ll;
    private Button confirmButton;

    private OnConfirmListener confirmListener;

    private EditText hour_et, min_et, sec_et;
    private EditText countAmount_et;

    public FragmentEditSubPlan () {

    }

    public static FragmentEditSubPlan getInstance(SubPlan subPlan) {
        FragmentEditSubPlan fragmentEditSubPlan = new FragmentEditSubPlan();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_SUBPLAN, subPlan);
        fragmentEditSubPlan.setArguments(args);
        return fragmentEditSubPlan;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubPlan = getArguments().getParcelable(EXTRA_SUBPLAN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_edit_subplan, container, false);
        labelEditText = rootView.findViewById(R.id.label_EditText_FragmentEditSubPlan);
        trackTimeTextView = rootView.findViewById(R.id.timeTrack_TextView_FragmentEdiSubPlan);
        trackCountTextView = rootView.findViewById(R.id.trackCount_TextView_FragmentEdiSubPlan);

        trackTime_ll = rootView.findViewById(R.id.timeInput_LinearLayout_FragmentEditSubPlan);
        trackCount_ll = rootView.findViewById(R.id.countInput_LinearLayout_FragmentEditSubPlan);

        hour_et = rootView.findViewById(R.id.hour_EditText_FragmentEditSubPlan);
        min_et = rootView.findViewById(R.id.minute_EditText_FragmentEditSubPlan);
        sec_et = rootView.findViewById(R.id.second_EditText_FragmentEditSubPlan);

        countAmount_et = rootView.findViewById(R.id.countAmount_EditText_FragmentEditSubPlan);

        confirmButton = rootView.findViewById(R.id.confirm_button_FragmentEditSubPlan);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        labelEditText.setText(mSubPlan.label);
        setTypeButtons();

        trackCountTextView.setOnClickListener(v -> {
            mSubPlan.track_time = false;
            mSubPlan.track_count = true;
            setTypeButtons();
        });
        trackTimeTextView.setOnClickListener(v -> {
            mSubPlan.track_time = true;
            mSubPlan.track_count = false;
            setTypeButtons();
        });

        confirmButton.setOnClickListener(v -> {
            String label = labelEditText.getText().toString().trim();
            int hr = hour_et.getText().toString().isEmpty() ? 0 : Integer.parseInt(hour_et.getText().toString());
            int min = min_et.getText().toString().isEmpty() ? 0 : Integer.parseInt(min_et.getText().toString());
            int sec = sec_et.getText().toString().isEmpty() ? 0 : Integer.parseInt(sec_et.getText().toString());

            int countGoal = countAmount_et.getText().toString().isEmpty() ? 0 : Integer.parseInt(countAmount_et.getText().toString());
            Log.d(TAG, "eee onViewCreated: count " + countGoal);

            if (label.isEmpty()) {
                Toast.makeText(getContext(), "Label can't be Empty", Toast.LENGTH_SHORT).show();
            }
            else if (mSubPlan.track_time && hr == 0 && min == 0 && sec == 0)
                Toast.makeText(getContext(), "Tracking Time can't be 0", Toast.LENGTH_SHORT).show();
            else if (mSubPlan.track_count && countGoal == 0)
                Toast.makeText(getContext(), "Count Goal can't be 0", Toast.LENGTH_SHORT).show();
            else {
                mSubPlan.label = label;
                if (mSubPlan.track_count) {
                    mSubPlan.count_goal = countGoal;
                    mSubPlan.time_goal = 0;
                    Log.d(TAG, "eee onViewCreated: count " + mSubPlan.count_goal);
                }
                else {
                    mSubPlan.count_goal = 0;
                    mSubPlan.time_goal = (hr * 3600) + (min * 60) + sec;
                }
                if (confirmListener != null) {
                    confirmListener.onConfirm();
                    dismiss();
                }
            }
        });
    }

    private void setTypeButtons() {
        TypedValue typedValue = new TypedValue();
        Objects.requireNonNull(getContext()).getTheme()
                .resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color = typedValue.data;

        if (mSubPlan.track_time) {
            trackTimeTextView.setTextColor(Color.WHITE);
            trackCountTextView.setTextColor(Color.BLACK);
            trackTimeTextView.setBackgroundColor(color);
            trackCountTextView.setBackgroundColor(getContext().getTheme().getResources().getColor(R.color.colorGray));

            trackCount_ll.setVisibility(View.GONE);
            trackTime_ll.setVisibility(View.VISIBLE);

            long hr = mSubPlan.time_goal / 3600;
            long min = (mSubPlan.time_goal % 3600) / 60;
            long sec = (mSubPlan.time_goal % 3600) % 60;

            hour_et.setText(String.valueOf(hr));
            min_et.setText(String.valueOf(min));
            sec_et.setText(String.valueOf(sec));
        } else {
            trackTimeTextView.setTextColor(Color.BLACK);
            trackCountTextView.setTextColor(Color.WHITE);
            trackTimeTextView.setBackgroundColor(getContext().getTheme().getResources().getColor(R.color.colorGray));
            trackCountTextView.setBackgroundColor(color);

            trackTime_ll.setVisibility(View.GONE);
            trackCount_ll.setVisibility(View.VISIBLE);

            countAmount_et.setText(String.valueOf(mSubPlan.count_goal));
        }
    }

    public FragmentEditSubPlan setConfirmListener(OnConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
        return this;
    }

    public interface OnConfirmListener {
        void onConfirm();
    }
}
