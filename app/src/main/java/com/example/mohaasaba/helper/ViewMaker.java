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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mohaasaba.R;
import com.example.mohaasaba.ibadah.tasbih.Tasbih;
import com.example.mohaasaba.models.Note;
import com.example.mohaasaba.models.Progress;
import com.example.mohaasaba.models.ProgressHistory;
import com.example.mohaasaba.models.Reminder;
import com.example.mohaasaba.models.ScheduleType;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public static class ProgressHistoryView {
        public final View view;
        private TextView dailyTarget_tv, completed_tv, totalComplete_tv;
        private EditText progressInput_et;
        private Button progress_bt;
        private RelativeLayout dailyTarget_rl, input_RL;
        private ImageButton inputDoneButton, undoButton, allDoneButton;
        private BarChart barChart;

        private ProgressHistory history;

        public ProgressHistoryView(Context context) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.view_progress_history, null, false);

            dailyTarget_tv = view.findViewById(R.id.dailyTarget_TextView_ViewProgressHistory);
            completed_tv = view.findViewById(R.id.targetComplete_TextView_ViewProgressHistory);
            totalComplete_tv = view.findViewById(R.id.totalCompleted_TextView_ViewProgressHistory);
            progress_bt = view.findViewById(R.id.progress_Button_ViewProgressHistory);
            dailyTarget_rl = view.findViewById(R.id.dailyTarget_RelativeLayout_ViewProgressHistory);
            input_RL = view.findViewById(R.id.progressInput_RelativeLayout_ViewProgressHistory);
            progressInput_et = view.findViewById(R.id.progressInput_EditText_ViewProgressHistory);
            inputDoneButton = view.findViewById(R.id.checkButton_ImageButton_ViewProgressHistory);
            barChart = view.findViewById(R.id.progress_chart_ViewProgressHistory);
            undoButton = view.findViewById(R.id.undoButton_ImageButton_ViewProgressHistory);
            allDoneButton = view.findViewById(R.id.allDone_ImageButton_ViewProgressHistory);


            // set Bar Chart
            barChart.setBackgroundColor(Color.TRANSPARENT);
            barChart.setDrawGridBackground(false);
            barChart.setGridBackgroundColor(Color.GREEN);
            barChart.setDrawBorders(false);
            barChart.getDescription().setEnabled(false);
            barChart.setPinchZoom(false);
            barChart.getLegend().setEnabled(false);
            barChart.getAxisLeft().setEnabled(true);
            barChart.getAxisRight().setEnabled(false);
            barChart.getXAxis().setDrawGridLines(false);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

            // Attach Listeners
            progress_bt.setOnClickListener(this::doProgress);
            undoButton.setOnClickListener(this::undoProgress);
            allDoneButton.setOnClickListener(this::allDone);
            dailyTarget_rl.setOnClickListener(this::showTargetInput);
            inputDoneButton.setOnClickListener(this::onTargetInputDone);
        }
        public View getView() {
            return view;
        }

        public ProgressHistoryView setProgressHistory(ProgressHistory progressHistory) {
            this.history = progressHistory;
            setProgressView();
            return this;
        }

        private void setProgressView() {
            Progress progress = history.getProgress(Calendar.getInstance());
            String target = progress.target + " " + progress.unit;
            String completed = progress.progress + " " + progress.unit;
            String total = history.getTotalProgress() + " " + progress.unit;
            dailyTarget_tv.setText(target);
            completed_tv.setText(completed);
            totalComplete_tv.setText(total);
            setChartData();
        }
        private void setChartData() {
            // month starting date and ending date
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),1);
            int lastDayOfMonth = selectedDate.getMaximum(Calendar.DAY_OF_MONTH);

            List<Integer> progressList = new ArrayList<>();
            for (int i = 1; i <= lastDayOfMonth; i++) {
                progressList.add( history.getProgress(selectedDate).progress);
                selectedDate.add(Calendar.DAY_OF_MONTH, 1);
            }

            // This is a long step. think minimizing the process
            List<BarEntry> yAxis = new ArrayList<>();
            for (int i = 1; i <= progressList.size(); i++) {
                yAxis.add(new BarEntry(i, progressList.get(i - 1)));
            }

            BarDataSet dataSet = new BarDataSet(yAxis, "Progress");
            BarData barData = new BarData(dataSet);
            barData.setDrawValues(false);
            barChart.setData(barData);
            barChart.invalidate();
            barChart.animateX(600);

        }
        private void doProgress(View view) {
            history.commitProgress(
                    history.getProgress(Calendar.getInstance()).doProgress(),
                    Calendar.getInstance());
            setProgressView();
        }
        private void undoProgress(View view) {
            history.commitProgress(
                    history.getProgress(Calendar.getInstance()).undoProgress(),
                    Calendar.getInstance());
            setProgressView();
        }
        private void allDone(View view) {
            history.commitProgress(
                    history.getProgress(Calendar.getInstance()).allDone(),
                    Calendar.getInstance());
            setProgressView();
        }
        private void showTargetInput(View view) {
            if (input_RL.getVisibility() == View.VISIBLE) {
                input_RL.setVisibility(View.GONE);
            } else {
                input_RL.setVisibility(View.VISIBLE);
                progressInput_et.setText(String.valueOf(history.getDailyTarget()));
            }
        }
        private void onTargetInputDone(View view) {
            Progress progress = history.getProgress(Calendar.getInstance());
            progress.target = progressInput_et.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(progressInput_et.getText().toString().trim());
            history.commitProgress(progress, Calendar.getInstance());
            input_RL.setVisibility(View.GONE);
            setProgressView();
        }
    }

    public static class TasbihTypeSelector {
        private View view;
        private TextView after_fazr, after_juhr, after_asr, after_magrib, after_esha;
        private TextView before_sleep, morning, evening, mustahab;

        public Tasbih.TasbihType tasbihType;

        public TasbihTypeSelector(Context context) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.view_tasbih_type, null, false);

            after_fazr = view.findViewById(R.id.fazr_ViewTasbihType);
            after_juhr = view.findViewById(R.id.juhr_ViewTasbihType);
            after_asr = view.findViewById(R.id.asr_ViewTasbihType);
            after_magrib = view.findViewById(R.id.magrib_ViewTasbihType);
            after_esha = view.findViewById(R.id.esha_ViewTasbihType);
            before_sleep = view.findViewById(R.id.sleep_ViewTasbihType);
            morning = view.findViewById(R.id.morning_ViewTasbihType);
            evening = view.findViewById(R.id.evening_ViewTasbihType);
            mustahab = view.findViewById(R.id.mustahab_ViewTasbihType);

            after_fazr.setOnClickListener(v -> {
                tasbihType = Tasbih.TasbihType.AFTER_FAZR;
                setView();
            });
            after_juhr.setOnClickListener(v -> {
                tasbihType = Tasbih.TasbihType.AFTER_JUHR;
                setView();
            });
            after_asr.setOnClickListener(v -> {
                tasbihType = Tasbih.TasbihType.AFTER_ASR;
                setView();
            });
            after_magrib.setOnClickListener(v -> {
                tasbihType = Tasbih.TasbihType.AFTER_MAGRIB;
                setView();
            });
            after_esha.setOnClickListener(v -> {
                tasbihType = Tasbih.TasbihType.AFTER_ESHA;
                setView();
            });
            before_sleep.setOnClickListener(v -> {
                tasbihType = Tasbih.TasbihType.BEFORE_SLEEP;
                setView();
            });
            morning.setOnClickListener(v -> {
                tasbihType = Tasbih.TasbihType.MORNING_TASBIH;
                setView();
            });
            evening.setOnClickListener(v -> {
                tasbihType = Tasbih.TasbihType.EVENING_TASBIH;
                setView();
            });
            mustahab.setOnClickListener(v -> {
                tasbihType = Tasbih.TasbihType.MUSTAHAB;
                setView();
            });

        }

        public TasbihTypeSelector setTasbihType(Tasbih.TasbihType tasbihType) {
            this.tasbihType = tasbihType;
            setView();
            return this;
        }
        public View getView() {
            return view;
        }
        public Tasbih.TasbihType getTasbihType() {
            return tasbihType;
        }
        private void setView() {
            clearViewColor(after_fazr);
            clearViewColor(after_juhr);
            clearViewColor(after_asr);
            clearViewColor(after_magrib);
            clearViewColor(after_esha);
            clearViewColor(before_sleep);
            clearViewColor(morning);
            clearViewColor(evening);
            clearViewColor(mustahab);


            switch (tasbihType) {
                case AFTER_FAZR:
                    setActiveColor(after_fazr);
                    break;
                case AFTER_JUHR:
                    setActiveColor(after_juhr);
                    break;
                case AFTER_ASR:
                    setActiveColor(after_asr);
                    break;
                case AFTER_MAGRIB:
                    setActiveColor(after_magrib);
                    break;
                case AFTER_ESHA:
                    setActiveColor(after_esha);
                    break;
                case BEFORE_SLEEP:
                    setActiveColor(before_sleep);
                    break;
                case MORNING_TASBIH:
                    setActiveColor(morning);
                    break;
                case EVENING_TASBIH:
                    setActiveColor(evening);
                    break;
                default:
                    setActiveColor(mustahab);
            }
        }

        private void clearViewColor(TextView view) {
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setTextColor(Color.BLACK);
        }
        private void setActiveColor(TextView view) {
            view.setBackgroundColor(Color.LTGRAY);
            view.setTextColor(Color.WHITE);
        }
    }
}