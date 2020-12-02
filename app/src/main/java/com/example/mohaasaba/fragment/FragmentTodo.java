package com.example.mohaasaba.fragment;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.TaskAdapter;
import com.example.mohaasaba.models.History;
import com.example.mohaasaba.models.Task;
import com.example.mohaasaba.dialog.DialogDatePicker;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;


public class FragmentTodo extends Fragment {
    private static final String TAG = "FragmentTodo";
    private History history;
    private EditText addTodoEditText;
    private ImageButton addTodoButton;
    private TaskAdapter mAdapter;
    private CircleProgressView mCircularProgressView;
    private TextView selectedTextView;
    private RecyclerView mRecyclerView;
    private LineChart lineChart;
    private RelativeLayout datePickerRecyclerView;

    private List<Task> taskList;
    private Calendar selectedDate;

    private FragmentTodoListeners listeners;

    public FragmentTodo(History history) {
        this.history = history;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerView_FragmentTodo);
        addTodoButton = rootView.findViewById(R.id.addButton_FragmentTodo);
        mCircularProgressView = rootView.findViewById(R.id.circularProgressView_header_FragmentTodo);
        selectedTextView = rootView.findViewById(R.id.textView_header_FragmentTodo);
        addTodoEditText = rootView.findViewById(R.id.addTodo_EditText_FragmentTodo);
        lineChart = rootView.findViewById(R.id.lineChart_header_FragmentTodo);
        datePickerRecyclerView = rootView.findViewById(R.id.recyclerView_Header_FragmentTodo);


        /* Set the properties of LineChart */
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

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new TaskAdapter();
        mRecyclerView.setAdapter(mAdapter);

        addTodoButton.setOnClickListener(v -> {
            String text = addTodoEditText.getText().toString().trim();
            if (! text.isEmpty()) {
                addTask(text);
                addTodoEditText.getText().clear();
            }
        });

        addTodoEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE  || event.getAction() == KeyEvent.ACTION_DOWN) {
                String text = addTodoEditText.getText().toString().trim();
                if (! text.isEmpty()) {
                    addTask(text);
                    addTodoEditText.getText().clear();
                    return true;
                }
            }
            return false;
        });

        // Initialize all Views
        selectedDate = Calendar.getInstance();
        taskList = history.getTasks(selectedDate);
        mAdapter.submitList(taskList);
        mAdapter.notifyDataSetChanged();
        setHeaderView();

        // Set Listener for opening Date Picker Dialog
        datePickerRecyclerView.setOnClickListener(v -> {
            if (listeners != null) listeners.onClicked();
        });

        /** Adapter Listeners */
        mAdapter.setListener(new TaskAdapter.ItemClickedListener() {
            @Override
            public void itemClicked(int position) {
                editTask(taskList.get(position));
            }

            @Override
            public void itemLongClicked(int position) {
                taskList.get(position).progress();
                history.commitTodo(selectedDate, taskList);
                setHeaderView();
                mAdapter.notifyItemChanged(position);
            }

            @Override
            public void deleteButtonClicked(int position) {
                Log.d(TAG, "deleteButtonClicked: position " + position);
                taskList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                history.commitTodo(selectedDate, taskList);
                setHeaderView();
            }
        });
    }
    /** Add new task and commit to history class*/
    private void addTask(String text) {
        Task task = new Task(text);
        taskList.add(task);
        history.commitTodo(selectedDate, taskList);
        mAdapter.submitList(taskList);
        mAdapter.notifyItemInserted(taskList.size() - 1);
        mRecyclerView.scrollToPosition(taskList.size() - 1);
        setHeaderView();
    }

    private void editTask(Task task) {
        FragmentTaskEditor fragmentTaskEditor = new FragmentTaskEditor(task);
        fragmentTaskEditor.show(getParentFragmentManager(), "Target Editor");

        fragmentTaskEditor.setListener(() -> {
            history.commitTodo(selectedDate, taskList);
            mAdapter.notifyItemChanged(taskList.indexOf(task));
            setHeaderView();
        });
    }

    /** Set data to circular progress view */
    private void setHeaderView() {
        float progress = history.getProgress(selectedDate);
        mCircularProgressView.setValueAnimated(progress, 500);
        selectedTextView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(selectedDate.getTime()));
        setChartData(); // Set the LineChart Data
    }

    public void setSelectedDate(Calendar calendar) {
        selectedDate.clear();
        selectedDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        taskList = history.getTasks(selectedDate);
        mAdapter.submitList(taskList);
        mAdapter.notifyDataSetChanged();
    }

    private void setChartData() {

        // check if selected dates are changed on not
        // this will minimize the time required
        Calendar monthStarting = Calendar.getInstance();
        monthStarting.clear();
        Calendar monthEnding = Calendar.getInstance();
        monthEnding.clear();

        // month starting date and ending date
        monthStarting.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),1);
        monthEnding.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
                selectedDate.getActualMaximum(Calendar.DAY_OF_MONTH));

        // This is a long step. think minimizing the process
        List<Float> progressOfMonth = history.getProgress(monthStarting, monthEnding);
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

        // Create data
        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);

        // Set data to chart
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.animateX(600);
    }
    private void openDatePickerDialog() {
        // TODO: Fix this issue
        DialogDatePicker datePickerDialog = new DialogDatePicker();
        datePickerDialog.show(getParentFragmentManager(),"date picker");
    }

    //TODO: Implement DatePicker Dialog From FragmentTodo;
    //TODO: Implement OnItemSwipe methods

    public void setFragmentListeners(FragmentTodoListeners listeners) {
        this.listeners = listeners;
    }

    public interface FragmentTodoListeners {
        void onClicked();
    }
}