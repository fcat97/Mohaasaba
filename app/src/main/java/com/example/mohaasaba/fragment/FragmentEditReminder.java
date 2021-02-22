package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mohaasaba.R;
import com.example.mohaasaba.models.Notify;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentEditReminder extends BottomSheetDialogFragment {
    private EditText reminderTitleEditText;
    private EditText remindBeforeEditText;
    private EditText repeatIntervalEditText;
    private TextView reminderHourEditText;
    private TextView reminderMinuteEditText;
    private FloatingActionButton confirmButton;
    private Notify notify;
    private EditFragmentListeners listeners;

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
        reminderHourEditText = rootView.findViewById(R.id.reminderHour_EditText_FragmentEditReminder);
        reminderMinuteEditText = rootView.findViewById(R.id.reminderMinute_EditText_FragmentEditReminder);

        confirmButton = rootView.findViewById(R.id.confirm_button_FragmentEditReminder);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (! notify.message.trim().isEmpty()) reminderTitleEditText.setText(notify.message);
        reminderHourEditText.setText(String.valueOf(notify.notificationHour));
        reminderMinuteEditText.setText(String.valueOf(notify.notificationMinute));

        remindBeforeEditText.setText(String.valueOf(notify.beforeMinute));
        repeatIntervalEditText.setText(String.valueOf(notify.repeatMinute));

        confirmButton.setOnClickListener(v -> {
            if (listeners != null) {
                notify.message = reminderTitleEditText.getText().toString().trim();
                notify.beforeMinute = Integer.parseInt(remindBeforeEditText.getText().toString());
                notify.repeatMinute = Integer.parseInt(repeatIntervalEditText.getText().toString());
                notify.notificationHour = Integer.parseInt(reminderHourEditText.getText().toString());
                notify.notificationMinute = Integer.parseInt(reminderMinuteEditText.getText().toString());

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


    }

    public void setListeners(EditFragmentListeners listeners) {
        this.listeners = listeners;
    }
    public interface EditFragmentListeners {
        void onConfirm();
    }
}
