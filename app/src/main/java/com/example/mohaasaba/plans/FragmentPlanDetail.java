package com.example.mohaasaba.plans;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.PlanRepository;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentPlanDetail extends Fragment {
    public static final String EXTRA_PLAN = "com.mohaasaba.EXTRA_PLAN";
    private Plan mPlan;
    private TextView label_tv, period_tv, tags_tv;
    private RecyclerView recyclerView;
    private ImageButton backButton, editButton;
    private LineChart lineChart;
    private RelativeLayout viewProtector;
    private Toolbar toolbar;

    private EditButtonListener editButtonListener;

    private SubPlanActionAdapter actionAdapter;
    private PlanRepository repository;
    private ImageView notificationIcon;

    public FragmentPlanDetail () {

    }

    public static FragmentPlanDetail getInstance(Plan plan) {
        FragmentPlanDetail fragmentPlanDetail = new FragmentPlanDetail();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PLAN, plan);
        fragmentPlanDetail.setArguments(args);

        return fragmentPlanDetail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlan = getArguments().getParcelable(EXTRA_PLAN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_plan_detail, null, false);

        toolbar = rootView.findViewById(R.id.toolbar_FragmentPlanDetail);
        label_tv = rootView.findViewById(R.id.planLabel_FragmentPlanDetail);
        period_tv = rootView.findViewById(R.id.period_TextView_FragmentPlanDetail);
        tags_tv = rootView.findViewById(R.id.tags_TextView_FragmentPlanDetail);
        lineChart = rootView.findViewById(R.id.lineChart_FragmentPlanDetail);
        viewProtector = rootView.findViewById(R.id.viewProtector_FragmentPlanDetail);

        recyclerView = rootView.findViewById(R.id.recyclerView_FragmentPlanDetail);

        backButton = rootView.findViewById(R.id.backButton_FragmentPlanDetail);
        editButton = rootView.findViewById(R.id.editButton_FragmentPlanDetail);

        notificationIcon = rootView.findViewById(R.id.notificationIcon_FragmentPlanDetail);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = new PlanRepository(getContext());

        toolbar.setTitle("Plan Detail");
        label_tv.setText(mPlan.label);
        period_tv.setText(mPlan.period.name());
        tags_tv.setText(mPlan.tags);

        actionAdapter = new SubPlanActionAdapter()
                .setChangeListener(this::updatePlan);
        recyclerView.setAdapter(actionAdapter);
        actionAdapter.submitList(mPlan.getSubPlans(Calendar.getInstance()));

        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        editButton.setOnClickListener(v -> {
            if (editButtonListener != null) editButtonListener.onClick(mPlan);
        });

        // LineChart ------------------------------------------------------------------------
        lineChart.setBackgroundColor(Color.TRANSPARENT);
        lineChart.setDrawGridBackground(false);
        lineChart.setGridBackgroundColor(Color.GREEN);
        lineChart.setDrawBorders(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        setChartData();

        // ViewProtector ---------------------------------------------------------------------------
        Calendar today = Calendar.getInstance();
        if (mPlan.period == PlanPeriod.WEEK_DAYS
                && ! mPlan.selectedWeekDays.contains(today.get(Calendar.DAY_OF_WEEK))) {
            viewProtector.setVisibility(View.VISIBLE);
        } else viewProtector.setVisibility(View.INVISIBLE);

        viewProtector.setOnClickListener(v -> {
            // do nothing
        });

        // Notification Icon -----------------------------------------------------------------------
        if (mPlan.notifyList.size() > 0) notificationIcon.setVisibility(View.VISIBLE);
        else notificationIcon.setVisibility(View.INVISIBLE);
    }

    private void setChartData() {

        // check if selected dates are changed on not
        // this will minimize the time required
        Calendar monthStarting = Calendar.getInstance();
        monthStarting.clear();
        Calendar monthEnding = Calendar.getInstance();
        monthEnding.clear();

        Calendar selectedDate = Calendar.getInstance();

        // month starting date and ending date
        monthStarting.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),1);
        monthEnding.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
                selectedDate.getActualMaximum(Calendar.DAY_OF_MONTH));

        // This is a long step. think minimizing the process
        List<Float> progressOfMonth = mPlan.getProgressBetween(monthStarting, monthEnding);
        List<Entry> yAxis = new ArrayList<>();

        // Get progress history of current month
        for (int i = 0; i < progressOfMonth.size(); i++) {
            yAxis.add(new Entry(i + 1, progressOfMonth.get(i)));
        }

        // Prepare Data set
        LineDataSet dataSet = new LineDataSet(yAxis, "Progress");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(1f);
        dataSet.setFillAlpha(255);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        // Get color from theme
        int[] attrs = {R.attr.colorAccent};
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs);
        int colorPrimary = typedArray.getColor(0, Color.BLACK);
        typedArray.recycle();
        // set the accent color
        dataSet.setFillColor(colorPrimary);
        dataSet.setColor(colorPrimary);
        dataSet.setFillAlpha(35);

        // Create data
        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);

        // Set data to chart
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.animateX(600);
    }

    private void updatePlan() {
        mPlan.commitSubPlans(Calendar.getInstance(), actionAdapter.getCurrentList());
        repository.update(mPlan);
        setChartData();
    }

    public FragmentPlanDetail setEditButtonListener(EditButtonListener editButtonListener) {
        this.editButtonListener = editButtonListener;
        return this;
    }

    public interface EditButtonListener {
        void onClick(Plan plan);
    }
}
