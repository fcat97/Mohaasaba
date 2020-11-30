package com.example.mohaasaba.helper;

import android.annotation.SuppressLint;
import android.content.Context;
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


}