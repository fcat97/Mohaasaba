package com.example.mohaasaba.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mohaasaba.R;
import com.example.mohaasaba.models.Notify;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentEditReminder extends BottomSheetDialogFragment {
    private static final String TAG = FragmentEditReminder.class.getCanonicalName();
    private EditText reminderTitleEditText;
    private EditText remindBeforeEditText;
    private EditText repeatIntervalEditText;
    private FloatingActionButton confirmButton;
    private Notify notify;
    private EditFragmentListeners listeners;
    private TextView notifyTime;

    public FragmentEditReminder(Notify notify) {
        this.notify = notify;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = View.inflate(getContext(), R.layout.fragment_edit_reminder, null);

        reminderTitleEditText = rootView.findViewById(R.id.reminderTitle_EditText_FragmentEditReminder);
        remindBeforeEditText = rootView.findViewById(R.id.remindBefore_EditText_FragmentEditReminder);
        repeatIntervalEditText = rootView.findViewById(R.id.reminderInterval_EditText_FragmentEditReminder);
        notifyTime = rootView.findViewById(R.id.notifyTime_textView_FragmentEditReminder);

        confirmButton = rootView.findViewById(R.id.confirm_button_FragmentEditReminder);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (! notify.message.trim().isEmpty()) reminderTitleEditText.setText(notify.message);

        remindBeforeEditText.setText(String.valueOf(notify.beforeMinute));
        repeatIntervalEditText.setText(String.valueOf(notify.repeatMinute));

        confirmButton.setOnClickListener(v -> {
            if (listeners != null) {
                notify.message = reminderTitleEditText.getText().toString().trim();
                notify.beforeMinute = Integer.parseInt(remindBeforeEditText.getText().toString());
                notify.repeatMinute = Integer.parseInt(repeatIntervalEditText.getText().toString());

                /* Check if user entered unwanted values or not */
                if (notify.notificationHour >= 24) notify.notificationHour = 23;
                if (notify.notificationHour < 0) notify.notificationHour = 0;
                if (notify.notificationMinute >= 60) notify.notificationMinute = 59;
                if (notify.notificationMinute < 0) notify.notificationMinute = 0;
                if (notify.repeatMinute > 0 && notify.repeatMinute < 15) notify.repeatMinute = 15;

                listeners.onConfirm();
                dismiss();
            } else throw new ClassCastException("Must Implement Listeners");
        });

        String time  = getTimeString(notify.notificationHour, notify.notificationMinute);
        notifyTime.setText(time);
        notifyTime.setOnClickListener(this::openTimePicker);


    }

    private void openTimePicker(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
            notify.notificationHour = hourOfDay;
            notify.notificationMinute = minute;
            notifyTime.setText(getTimeString(hourOfDay, minute));
            Log.d(TAG, "openTimePicker: h " + hourOfDay + " m " + minute);
        }, notify.notificationHour, notify.notificationMinute, false);
        timePickerDialog.show();
    }
    private String getTimeString(int hour, int minute) {
        if (hour >= 12) {
            return (hour - 12) + " : " + minute + " PM";
        } else return hour + " : " + minute + " AM";
    }

    public void setListeners(EditFragmentListeners listeners) {
        this.listeners = listeners;
    }
    public interface EditFragmentListeners {
        void onConfirm();
    }
}
