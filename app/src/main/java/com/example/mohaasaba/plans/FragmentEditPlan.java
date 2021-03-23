package com.example.mohaasaba.plans;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.PlanRepository;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.Calendar;
import java.util.List;


public class FragmentEditPlan extends Fragment {

    private static final String EXTRA_PLAN = "com.example.Mohaasaba.EXTRA_PLAN";
    private static final String TAG = FragmentEditPlan.class.getCanonicalName();

    private Plan mPlan;

    private EditText labelEditText;
    private EditText intervalEditText;
    private DatePickerTimeline datePickerTimeline;
    private RecyclerView recyclerView;
    private ImageButton addSubPlanButton, backButton;

    private Calendar selectedDate;
    private SubPlanAdapter subPlanAdapter;
    private Button saveButton;
    private OnSaveButtonClickListener saveButtonClickListener;

    private List<SubPlan> subPlanList;

    private CheckBox cb_daily, cb_weekly, cb_monthly, cb_yearly, cb_interval, cb_weekDates;

    public FragmentEditPlan() {
        // Required empty public constructor
    }

    public static FragmentEditPlan newInstance(Plan plan) {
        FragmentEditPlan fragment = new FragmentEditPlan();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PLAN, plan);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlan = getArguments().getParcelable(EXTRA_PLAN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = View.inflate(getContext(), R.layout.fragment_edit_plan, null);
        labelEditText = rootView.findViewById(R.id.label_EditText_FragmentEditPlan);
        datePickerTimeline = rootView.findViewById(R.id.datePickerTimeLine_FragmentEditPlan);
        intervalEditText = rootView.findViewById(R.id.interval_et_FragmentEditPlan);

        recyclerView = rootView.findViewById(R.id.recyclerView_FragmentEditPlan);
        addSubPlanButton = rootView.findViewById(R.id.add_subPlan_ImageButton_FragmentEditPlan);

        saveButton = rootView.findViewById(R.id.saveButton_FragmentEditPlan);
        backButton = rootView.findViewById(R.id.backButton_FragmentEditPlan);

        cb_daily = rootView.findViewById(R.id.everyDay_cb_FragmentEditPlan);
        cb_weekly = rootView.findViewById(R.id.everyWeek_cb_FragmentEditPlan);
        cb_monthly = rootView.findViewById(R.id.everyMonth_cb_FragmentEditPlan);
        cb_yearly = rootView.findViewById(R.id.everyYear_cb_FragmentEditPlan);
        cb_interval = rootView.findViewById(R.id.interval_cb_FragmentEditPlan);
        cb_weekDates = rootView.findViewById(R.id.weekDays_cb_FragmentEditPlan);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        labelEditText.setText(mPlan.label);

        // Initialize DateSelector
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(mPlan.planCreationTime);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        datePickerTimeline.setInitialDate(year, month, date);

        selectedDate = Calendar.getInstance();
        datePickerTimeline.setActiveDate(selectedDate);
        datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                selectedDate.set(year, month, day);
                updateSubPlansList();
            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {
                // Do Something
            }
        });

        subPlanAdapter = new SubPlanAdapter()
                .setDeleteButtonListener(this::deleteSubPlan)
                .setItemClickListener(this::openFragmentEditSubPlan);
        recyclerView.setAdapter(subPlanAdapter);
        updateSubPlansList();

        addSubPlanButton.setOnClickListener(v -> {
            openFragmentEditSubPlan(new SubPlan());
        });


        saveButton.setOnClickListener(v -> {
            String label = labelEditText.getText().toString().trim();
            if (label.isEmpty()) {
                Toast.makeText(getContext(), "Label can't be empty!", Toast.LENGTH_SHORT).show();
                return;
            } else mPlan.label = label;

            if (mPlan.period == PlanPeriod.INTERVALS) {
                int interval = 0;
                if (intervalEditText.getText().toString().isEmpty())
                    Toast.makeText(getContext(), "interval can't be zero", Toast.LENGTH_SHORT).show();
                else interval = Integer.parseInt(intervalEditText.getText().toString());
                mPlan.intervalDate = interval;
            }

            PlanRepository repository = new PlanRepository(getContext());
            repository.update(mPlan);
            getParentFragmentManager().popBackStack();
        });

        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        setPeriodSelectors();
        cb_daily.setOnClickListener(v -> {
            mPlan.period = PlanPeriod.DAILY;
            setPeriodSelectors();
        });
        cb_weekly.setOnClickListener(v -> {
            mPlan.period = PlanPeriod.WEEKLY;
            setPeriodSelectors();
        });
        cb_monthly.setOnClickListener(v -> {
            mPlan.period = PlanPeriod.MONTHLY;
            setPeriodSelectors();
        });
        cb_yearly.setOnClickListener(v -> {
            mPlan.period = PlanPeriod.YEARLY;
            setPeriodSelectors();
        });
        cb_interval.setOnClickListener(v -> {
            mPlan.period = PlanPeriod.INTERVALS;
            setPeriodSelectors();
        });
        cb_weekDates.setOnClickListener(v -> {
            mPlan.period = PlanPeriod.WEEK_DAYS;
            setPeriodSelectors();
        });

    }

    private void openFragmentEditSubPlan(SubPlan subPlan) {
        FragmentEditSubPlan fragmentEditSubPlan = FragmentEditSubPlan.getInstance(subPlan)
                .setConfirmListener(() -> {
                    if (! subPlanList.contains(subPlan)) subPlanList.add(subPlan);
                    int index = subPlanList.indexOf(subPlan);
                    mPlan.commitSubPlans(selectedDate, subPlanList);
                    subPlanAdapter.submitList(subPlanList);
                    subPlanAdapter.notifyItemRangeChanged(index, subPlanAdapter.getItemCount());
                });
        fragmentEditSubPlan.show(getChildFragmentManager(), "FragmentEditSubPlan");
    }

    private void deleteSubPlan(int index) {
        subPlanList.remove(index);
        mPlan.commitSubPlans(selectedDate, subPlanList);
        subPlanAdapter.notifyItemRemoved(index);
        subPlanAdapter.notifyItemRangeChanged(index, subPlanAdapter.getItemCount());
    }

    private void setPeriodSelectors() {
        cb_daily.setChecked(mPlan.period == PlanPeriod.DAILY);
        cb_weekly.setChecked(mPlan.period == PlanPeriod.WEEKLY);
        cb_monthly.setChecked(mPlan.period == PlanPeriod.MONTHLY);
        cb_yearly.setChecked(mPlan.period == PlanPeriod.YEARLY);
        cb_interval.setChecked(mPlan.period == PlanPeriod.INTERVALS);
        cb_weekDates.setChecked(mPlan.period == PlanPeriod.WEEK_DAYS);

        if (mPlan.period == PlanPeriod.INTERVALS) {
            intervalEditText.setText(String.valueOf(mPlan.intervalDate));
        }
    }

    private void updateSubPlansList() {
        subPlanList = mPlan.getSubPlans(selectedDate);
        subPlanAdapter.submitList(subPlanList);
        subPlanAdapter.notifyDataSetChanged();
    }

    public FragmentEditPlan setSaveButtonClickListener(OnSaveButtonClickListener saveButtonClickListener) {
        this.saveButtonClickListener = saveButtonClickListener;
        return this;
    }

    public interface OnSaveButtonClickListener {
        void onClicked(Plan plan);
    }
}