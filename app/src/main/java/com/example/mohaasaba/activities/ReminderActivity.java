package com.example.mohaasaba.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.Reminder;
import com.example.mohaasaba.dialog.DialogDatePicker;
import com.example.mohaasaba.dialog.DialogTimePicker;

import java.text.DateFormat;
import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String EXTRA_REMINDER = "com.example.mohasabap.REMINDER";
    private static final String TAG = "ReminderActivity";

    private EditText mLabelEditText;
    private Button mAlarmDateButton;
    private Button mAlarmTimeButton;
    private EditText notifyBeforeEditText;
    private EditText repeatIntervalEditText;
    private SwitchCompat notifySwitch;
    private SwitchCompat repeatSwitch;
    private Calendar mCalender;
    private Reminder mReminder;
    private Button confirmButton;

    private RadioButton notify_minute, notify_hour, notify_day, notify_month;
    private RadioButton repeatInterval_minute, repeatInterval_hour,repeatInterval_day,repeatInterval_month;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);


        //Instantiating all views of activity
        mLabelEditText = findViewById(R.id.label_EditText_ReminderActivity);
        mAlarmDateButton = findViewById(R.id.reminderDate_Button_ReminderActivity);
        mAlarmTimeButton = findViewById(R.id.reminderTime_Button_ReminderActivity);
        notifyBeforeEditText = findViewById(R.id.notifyBefore_editText_ReminderActivity);
        repeatIntervalEditText = findViewById(R.id.repeat_editText_ReminderActivity);
        notifySwitch = findViewById(R.id.notify_switch_ReminderActivity);
        repeatSwitch = findViewById(R.id.repeat_switch_ReminderActivity);

        notify_minute = findViewById(R.id.notify_minute);
        notify_hour = findViewById(R.id.notify_hour);
        notify_day = findViewById(R.id.notify_day);
        notify_month = findViewById(R.id.notify_month);
        notify_minute.setChecked(true);

        repeatInterval_minute = findViewById(R.id.repeat_minute);
        repeatInterval_hour = findViewById(R.id.repeat_hour);
        repeatInterval_day = findViewById(R.id.repeat_day);
        repeatInterval_month = findViewById(R.id.repeat_month);
        repeatInterval_minute.setChecked(true);

        confirmButton = findViewById(R.id.confirm_button_ReminderActivity);
        confirmButton.setOnClickListener(v -> saveReminder());


        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_REMINDER)) {
            /* For Edit Reminder Request */
            mReminder = intent.getParcelableExtra(EXTRA_REMINDER);
            assert mReminder != null;
            mCalender = mReminder.getReminderTime();

            if (mReminder.getReminderLabel() != null) mLabelEditText.setText(mReminder.getReminderLabel());
            mAlarmTimeButton.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(mCalender.getTime()));
            mAlarmDateButton.setText(DateFormat.getDateInstance(DateFormat.LONG).format(mCalender.getTime()));
            if (mCalender.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                mAlarmDateButton.setTextColor(getResources().getColor(R.color.colorRed));
            }
            if (mCalender.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) {
                mAlarmTimeButton.setTextColor(getResources().getColor(R.color.colorRed));
            }

            if (mReminder.getPreRemindTime() != Reminder._NULL_) {
                notifyBeforeEditText.setText(mReminder.getPreRemindTime());
                notifySwitch.setChecked(true);
                switch (mReminder.getPreReminderUnit()) {
                    case Reminder.MINUTE:
                        notify_minute.setChecked(true);
                        break;
                    case Reminder.HOUR:
                        notify_hour.setChecked(true);
                        break;
                    case Reminder.DAY:
                        notify_day.setChecked(true);
                        break;
                    case Reminder.MONTH:
                        notify_month.setChecked(true);
                        break;
                    default:
                        notify_minute.setChecked(false);
                        notify_hour.setChecked(false);
                        notify_day.setChecked(false);
                        notify_month.setChecked(false);
                        break;
                }
            }

            if (mReminder.getRepeatingInterval() != Reminder._NULL_) {
                repeatIntervalEditText.setText(mReminder.getRepeatingInterval());
                repeatSwitch.setChecked(true);
                switch (mReminder.getPreReminderUnit()) {
                    case Reminder.MINUTE:
                        repeatInterval_minute.setChecked(true);
                        break;
                    case Reminder.HOUR:
                        repeatInterval_hour.setChecked(true);
                        break;
                    case Reminder.DAY:
                        repeatInterval_day.setChecked(true);
                        break;
                    case Reminder.MONTH:
                        repeatInterval_month.setChecked(true);
                        break;
                    default:
                        repeatInterval_minute.setChecked(false);
                        repeatInterval_hour.setChecked(false);
                        repeatInterval_day.setChecked(false);
                        repeatInterval_month.setChecked(false);
                        break;
                }
            }
        } else {
            /* For add Reminder Request */
            //Setting the current date and time
            mCalender = Calendar.getInstance();
            mCalender.set(Calendar.SECOND,0);
            mCalender.set(Calendar.MINUTE,mCalender.get(Calendar.MINUTE) + 1);
            mAlarmTimeButton.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(mCalender.getTime()));
            mAlarmDateButton.setText(DateFormat.getDateInstance(DateFormat.LONG).format(mCalender.getTime()));
        }
    }


    //On click save menu Item
    private void saveReminder() {
        /* reminder must be in future time */
        if (mCalender.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) {
            Toast.makeText(this, "Time Has past already", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mReminder == null) {
            mReminder = new Reminder(mCalender);
        }
        if (!mLabelEditText.getText().toString().trim().isEmpty()) {
            mReminder.setReminderLabel(mLabelEditText.getText().toString().trim());
        }
        if (notifySwitch.isChecked()) {
            //TODO: Show a warning that this must not empty.
            if (notifyBeforeEditText.getText().toString().trim().isEmpty()) return;
            int time = Integer.parseInt(notifyBeforeEditText.getText().toString());
            mReminder.setPreRemindTime(time);
            if (notify_minute.isChecked()) mReminder.setPreReminderUnit(Reminder.MINUTE);
            else if (notify_hour.isChecked()) mReminder.setPreReminderUnit(Reminder.HOUR);
            else if (notify_day.isChecked()) mReminder.setPreReminderUnit(Reminder.DAY);
            else if (notify_month.isChecked()) mReminder.setPreReminderUnit(Reminder.MONTH);
        }
        if (repeatSwitch.isChecked()) {
            //TODO: Show a warning that this must not empty.
            if (repeatIntervalEditText.getText().toString().trim().isEmpty()) return;
            int interval = Integer.parseInt(repeatIntervalEditText.getText().toString());
            mReminder.setRepeatingInterval(interval);
            if (repeatInterval_minute.isChecked()) mReminder.setRepeatIntervalUnit(Reminder.MINUTE);
            else if (repeatInterval_hour.isChecked()) mReminder.setRepeatIntervalUnit(Reminder.HOUR);
            else if (repeatInterval_day.isChecked()) mReminder.setRepeatIntervalUnit(Reminder.DAY);
            else if (repeatInterval_month.isChecked()) mReminder.setRepeatIntervalUnit(Reminder.MONTH);
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_REMINDER, mReminder);
        setResult(RESULT_OK, data);
        finish();
    }

    public void openDatePickerDialog(View view) {
        DialogDatePicker datePickerDialog = new DialogDatePicker();
        datePickerDialog.show(getSupportFragmentManager(),"date picker");
    }

    public void openTimePickerDialog(View view) {
        DialogTimePicker dialogTimePicker = new DialogTimePicker();
        dialogTimePicker.show(getSupportFragmentManager(),"time picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalender.set(year,month,dayOfMonth);
        mAlarmDateButton.setText(DateFormat.getDateInstance(DateFormat.LONG).format(mCalender.getTime()));
        mAlarmDateButton.setTextColor(getTheme().getResources().getColor(R.color.colorGreen));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalender.set(Calendar.MINUTE,minute);
        mAlarmTimeButton.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(mCalender.getTime()));
        mAlarmTimeButton.setTextColor(getTheme().getResources().getColor(R.color.colorGreen));
    }
}