package com.example.mohaasaba.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
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
    public IncomeExpenseSelector getIESelector() {
        return new IncomeExpenseSelector(mContext);
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
        private int selectedColor;

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

            // Set onClickListener to all views
            sat.setOnClickListener(this);
            sun.setOnClickListener(this);
            mon.setOnClickListener(this);
            tue.setOnClickListener(this);
            wed.setOnClickListener(this);
            thu.setOnClickListener(this);
            fri.setOnClickListener(this);

            // Get theme color from Resource
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
            int themeColor = typedValue.data;
            // Set alpha(transparency) to theme color
            selectedColor = Color.argb(165, Color.red(themeColor), Color.green(themeColor), Color.blue(themeColor));
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.saturdayLabel_DaysSelectorView) {
                saturday = !saturday;
                type.everySaturday = saturday;
                if (saturday) {
                    ((TextView)v).setTextColor(Color.WHITE);
                    v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                }
                else {
                    ((TextView)v).setTextColor(Color.BLACK);
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                }
            }
            else if (v.getId() == R.id.sundayLabel_DaysSelectorView) {
                sunday = !sunday;
                type.everySunday = sunday;
                if (sunday) {
                    v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                    ((TextView)v).setTextColor(Color.WHITE);
                }
                else {
                    ((TextView)v).setTextColor(Color.BLACK);
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                }
            }
            else if (v.getId() == R.id.mondayLabel_DaysSelectorView) {
                monday = !monday;
                type.everyMonday = monday;
                if (monday) {
                    v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                    ((TextView)v).setTextColor(Color.WHITE);
                }
                else {
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                    ((TextView)v).setTextColor(Color.BLACK);
                }
            }
            else if (v.getId() == R.id.tuesdayLabel_DaysSelectorView) {
                tuesday = !tuesday;
                type.everyTuesday = tuesday;
                if (tuesday) {
                    v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                    ((TextView)v).setTextColor(Color.WHITE);
                }
                else {
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                    ((TextView)v).setTextColor(Color.BLACK);
                }
            }
            else if (v.getId() == R.id.wednesdayLabel_DaysSelectorView) {
                wednesday = !wednesday;
                type.everyWednesday = wednesday;
                if (wednesday) {
                    v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                    ((TextView)v).setTextColor(Color.WHITE);
                }
                else {
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                    ((TextView)v).setTextColor(Color.BLACK);
                }
            }
            else if (v.getId() == R.id.thursdayLabel_DaysSelectorView) {
                thursday = !thursday;
                type.everyThursday = thursday;
                if (thursday) {
                    v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                    ((TextView)v).setTextColor(Color.WHITE);
                }
                else {
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                    ((TextView)v).setTextColor(Color.BLACK);
                }
            }
            else if (v.getId() == R.id.fridayLabel_DaysSelectorView) {
                friday = !friday;
                type.everyFriday = friday;
                if (friday) {
                    v.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                    ((TextView)v).setTextColor(Color.WHITE);
                }
                else {
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                    ((TextView)v).setTextColor(Color.BLACK);
                }
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

            if (saturday) {
                sat.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                sat.setTextColor(Color.WHITE);
            }
            else {
                sat.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                sat.setTextColor(Color.BLACK);
            }

            if (sunday) {
                sun.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                sun.setTextColor(Color.WHITE);
            }
            else {
                sun.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                sun.setTextColor(Color.BLACK);
            }

            if (monday) {
                mon.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                mon.setTextColor(Color.WHITE);
            }
            else {
                mon.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                mon.setTextColor(Color.BLACK);
            }

            if (tuesday) {
                tue.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                tue.setTextColor(Color.WHITE);
            }
            else {
                tue.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                tue.setTextColor(Color.BLACK);
            }

            if (wednesday) {
                wed.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                wed.setTextColor(Color.WHITE);
            }
            else {
                wed.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                wed.setTextColor(Color.BLACK);
            }

            if (thursday) {
                thu.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                thu.setTextColor(Color.WHITE);
            }
            else {
                thu.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                thu.setTextColor(Color.BLACK);
            }

            if (friday) {
                fri.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                fri.setTextColor(Color.WHITE);
            }
            else {
                fri.setBackgroundTintList(ColorStateList.valueOf(Color.argb(85,230, 230, 230)));
                fri.setTextColor(Color.BLACK);
            }
        }

    }

    public class IncomeExpenseSelector{
        private final View rootView;
        public TextView income;
        public TextView expense;
        private EditText amountEditText;
        private int selectedColor;

        public IncomeExpenseSelector(Context context) {
            rootView = LayoutInflater.from(context)
                    .inflate(R.layout.view_inceome_expense_selector, null, false);
            income = rootView.findViewById(R.id.income_TextView_amountEditor_FragmentTransactionEditor);
            expense = rootView.findViewById(R.id.expense_TextView_amountEditor_FragmentTransactionEditor);
            amountEditText = rootView.findViewById(R.id.amount_EditText_amountEditor_FragmentTransactionEditor);

            // Get the color of selected state of buttons
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                selectedColor = mContext.getColor(R.color.colorGray);
            } else selectedColor = Color.LTGRAY;

            income.setOnClickListener(v -> {
                setAsIncome();
            });
            expense.setOnClickListener(v -> {
                setAsExpense();
            });
        }
        private void setAsIncome() {
            float amount = Float.parseFloat(amountEditText.getText().toString());
            if (amount < 0.0) {
                amountEditText.setText(String.valueOf(-1 * amount));
            }
            income.setBackgroundColor(selectedColor);
            income.setTextColor(Color.BLACK);

            expense.setBackgroundColor(Color.TRANSPARENT);
        }
        private void setAsExpense() {
            float amount = Float.parseFloat(amountEditText.getText().toString());
            if (amount > 0.0) {
                amountEditText.setText(String.valueOf(-1 * amount));
            }
            expense.setBackgroundColor(selectedColor);
            expense.setTextColor(Color.BLACK);

            income.setBackgroundColor(Color.TRANSPARENT);
        }

        public float getAmount() {
            return Float.parseFloat(amountEditText.getText().toString());
        }
        public void setAmount(float amount) {
            amountEditText.setText(String.valueOf(amount));
            if (amount < 0.0) setAsExpense();
            else setAsIncome();
        }
        public View getIESelectorView() { return rootView; }
    }
}