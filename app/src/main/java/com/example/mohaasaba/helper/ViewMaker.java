package com.example.mohaasaba.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mohaasaba.R;
import com.example.mohaasaba.models.Note;
import com.example.mohaasaba.models.Reminder;
import com.example.mohaasaba.models.ScheduleType;

import java.text.DateFormat;
import java.util.Calendar;

public class ViewMaker {
    private static final String TAG = "ViewMaker";
    private Context mContext;

    public ViewMaker(Context context) {
        this.mContext = context;
    }

    public View getNoteView(Note note) {
        return new NoteView().getNoteView(note);
    }
    public View getReminderView(Reminder reminder) {
        return new ReminderView().getReminderView(reminder);
    }

    public DateSelectorView getDateSelectorView() {
        return new DateSelectorView(mContext);
    }

    private class NoteView {
        private TextView noteTitle, noteDetail;
        private TextView lastModConstStr, lastModVar;
        private View noteView;
        private ImageButton overflowButton;
        private ImageButton expandButton;
        private ImageView noteIcon;
        private boolean expanded = false;

        public NoteView() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.noteView = inflater.inflate(R.layout.view_description_add_schedule,null);

            this.noteTitle = noteView.findViewById(R.id.title_EditText_DescriptionView);
            this.noteDetail = noteView.findViewById(R.id.detail_EditText_DescriptionView);
            this.lastModConstStr = noteView.findViewById(R.id.lastModifiedConstant_TextView_DescriptionView);
            this.lastModVar = noteView.findViewById(R.id.lastModifiedVariable_TextView_DescriptionView);
            this.overflowButton = noteView.findViewById(R.id.overflowButton_DescriptionView);
            this.expandButton = noteView.findViewById(R.id.expand_ImageButton_DescriptionView);
            this.noteIcon = noteView.findViewById(R.id.icon_descriptionView);


            lastModConstStr.setVisibility(View.VISIBLE);
            lastModVar.setVisibility(View.VISIBLE);
            overflowButton.setVisibility(View.VISIBLE);
        }

        public View getNoteView(Note note) {
            noteTitle.setText(note.getTitle());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                noteDetail.setText(Html.fromHtml(note.getDetail(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                noteDetail.setText(Html.fromHtml(note.getDetail()));
            }
            Calendar currentTime = Calendar.getInstance();
            if (currentTime.get(Calendar.DAY_OF_MONTH) > note.getDateModified().get(Calendar.DAY_OF_MONTH)){
                lastModVar.setText(DateFormat.getDateInstance(DateFormat.LONG).format(note.getDateModified().getTime()));
            } else if (currentTime.get(Calendar.DAY_OF_MONTH) == note.getDateModified().get(Calendar.DAY_OF_MONTH) &&
                    currentTime.get(Calendar.MINUTE) > note.getDateModified().get(Calendar.MINUTE) ) {
                lastModVar.setText(R.string.today_text);
            } else {
                lastModVar.setText(R.string.now_text);
            }


            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expanded) {
                        float deg = expandButton.getRotation() - 180F;
                        expandButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                        expanded = false;
                        noteDetail.setMaxLines(2);
                    } else {
                        float deg = expandButton.getRotation() + 180F;
                        expandButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                        expanded = true;
                        noteDetail.setMaxLines(10);
                    }
                }
            });
            return noteView;
        }
    }

    private class ReminderView {
        private TextView reminderTime;
        private TextView reminderDate;
        private TextView repeatInterval;
        private LinearLayout repeatIntervalLL;
        private ConstraintLayout constraintLayout;
        private View mReminderView;
        private ImageView bellIcon;

        public ReminderView () {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mReminderView = inflater.inflate(R.layout.view_reminder_add_schedule,null);

            reminderTime = mReminderView.findViewById(R.id.reminderTime_TextView_ReminderView);
            reminderDate = mReminderView.findViewById(R.id.reminderDate_TextView_ReminderView);
            repeatInterval = mReminderView.findViewById(R.id.repeat_interval_TextView_Reminder_View);
            repeatIntervalLL = mReminderView.findViewById(R.id.repeat_interval_linearL_ReminderView);
            constraintLayout = mReminderView.findViewById(R.id.constrainLayout_ReminderView);
            bellIcon = mReminderView.findViewById(R.id.bell_icon_ReminderView);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public View getReminderView(Reminder reminder) {
            reminderTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(reminder.getReminderTime().getTime()));
            reminderDate.setText(DateFormat.getDateInstance(DateFormat.LONG).format(reminder.getReminderTime().getTime()));
            if (reminder.getRepeatingInterval() != Reminder._NULL_) {
                String text = reminder.getRepeatingInterval() + " " + reminder.getRepeatIntervalUnit();
                repeatInterval.setText(text);
            } else repeatIntervalLL.setVisibility(View.GONE);
            if (reminder.getReminderTime().getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) {
                bellIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bell_off,mContext.getTheme()));
                constraintLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorRed));
            }

            return mReminderView;
        }
    }

    public static class DateSelectorView implements View.OnClickListener {
        private View rootView;
        private ScheduleType type;

        private TextView sat;
        private TextView sun;
        private TextView mon;
        private TextView tue;
        private TextView wed;
        private TextView thu;
        private TextView fri;

        public boolean saturday = false;
        public boolean sunday = false;
        public boolean monday = false;
        public boolean tuesday = false;
        public boolean wednesday = false;
        public boolean thursday = false;
        public boolean friday = false;

        public DateSelectorView(Context context) {
            rootView = LayoutInflater.from(context).inflate(R.layout.view_days_selector, null);

            sat = rootView.findViewById(R.id.saturdayLabel_DaysSelectorView);
            sun = rootView.findViewById(R.id.sundayLabel_DaysSelectorView);
            mon = rootView.findViewById(R.id.mondayLabel_DaysSelectorView);
            tue = rootView.findViewById(R.id.tuesdayLabel_DaysSelectorView);
            wed = rootView.findViewById(R.id.wednesdayLabel_DaysSelectorView);
            thu = rootView.findViewById(R.id.thursdayLabel_DaysSelectorView);
            fri = rootView.findViewById(R.id.fridayLabel_DaysSelectorView);

            sat.setOnClickListener(this);
            sun.setOnClickListener(this);
            mon.setOnClickListener(this);
            tue.setOnClickListener(this);
            wed.setOnClickListener(this);
            thu.setOnClickListener(this);
            fri.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //TODO: get this colors from Theme colors
            if (v.getId() == R.id.saturdayLabel_DaysSelectorView) {
                saturday = !saturday;
                type.everySaturday = saturday;
                if (saturday) v.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                else v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));
            }
            else if (v.getId() == R.id.sundayLabel_DaysSelectorView) {
                sunday = !sunday;
                type.everySunday = sunday;
                if (sunday) v.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                else v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));
            }
            else if (v.getId() == R.id.mondayLabel_DaysSelectorView) {
                monday = !monday;
                type.everyMonday = monday;
                if (monday) v.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                else v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));
            }
            else if (v.getId() == R.id.tuesdayLabel_DaysSelectorView) {
                tuesday = !tuesday;
                type.everyTuesday = tuesday;
                if (tuesday) v.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                else v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));
            }
            else if (v.getId() == R.id.wednesdayLabel_DaysSelectorView) {
                wednesday = !wednesday;
                type.everyWednesday = wednesday;
                if (wednesday) v.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                else v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));
            }
            else if (v.getId() == R.id.thursdayLabel_DaysSelectorView) {
                thursday = !thursday;
                type.everyThursday = thursday;
                if (thursday) v.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                else v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));
            }
            else if (v.getId() == R.id.fridayLabel_DaysSelectorView) {
                friday = !friday;
                type.everyFriday = friday;
                if (friday) v.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                else v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));
            }
        }

        public View getView() {
            return rootView;
        }

        public void setScheduleType(ScheduleType type) {
            this.type = type;

            saturday = type.everySaturday;
            sunday = type.everySunday;
            monday = type.everyMonday;
            tuesday = type.everyTuesday;
            wednesday = type.everyWednesday;
            thursday = type.everyThursday;
            friday = type.everyFriday;

            if (saturday) sat.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            else sat.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));

            if (sunday) sun.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            else sun.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));

            if (monday) mon.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            else mon.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));

            if (tuesday) tue.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            else tue.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));

            if (wednesday) wed.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            else wed.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));

            if (thursday) thu.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            else thu.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));

            if (friday) fri.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            else fri.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(230, 230, 230)));
        }

    }


}