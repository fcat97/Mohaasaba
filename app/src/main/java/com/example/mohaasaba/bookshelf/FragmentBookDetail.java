package com.example.mohaasaba.bookshelf;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.models.Progress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentBookDetail extends Fragment {
    private static final String ARG_BOOK = FragmentBookDetail.class.getCanonicalName() + "_BOOK";

    private SaveButtonListener saveButtonListener;
    private Book book;
    private BookRepo repo;

    private Toolbar toolbar;
    private EditText bookTitle_et, author_et, publication_et, pages_et, owner_et;
    private TextView entryDate_tv, dailyTarget_tv, completed_tv, totalComplete_tv;
    private Button progress_bt;
    private ImageButton saveButton;
    private RelativeLayout rrl_5, input_RL, dailyTarget_rl;
    private EditText progressInput_et;
    private ImageButton inputDoneButton;
    private BarChart barChart;

    public FragmentBookDetail() {
        // Required empty public constructor
    }

    public static FragmentBookDetail newInstance(Book book) {
        FragmentBookDetail fragment = new FragmentBookDetail();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable(ARG_BOOK);
        }
        if (repo == null) repo = new BookRepo(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_book_detail, null, false);
        toolbar = view.findViewById(R.id.toolbar_FragmentBookDetail);

        bookTitle_et = view.findViewById(R.id.bookTitle_EditText_FragmentBookDetail);
        author_et = view.findViewById(R.id.author_EditText_FragmentBookDetail);
        publication_et = view.findViewById(R.id.publication_EditText_FragmentBookDetail);
        pages_et = view.findViewById(R.id.pages_EditText_FragmentBookDetail);
        entryDate_tv = view.findViewById(R.id.entryDate_TextView_FragmentBookDetail);
        owner_et = view.findViewById(R.id.ownerName_EditText_FragmentBookDetail);
        dailyTarget_tv = view.findViewById(R.id.dailyTarget_TextView_FragmentBookDetail);
        completed_tv = view.findViewById(R.id.targetComplete_TextView_FragmentBookDetail);
        totalComplete_tv = view.findViewById(R.id.totalCompleted_TextView_FragmentBookDetail);
        progress_bt = view.findViewById(R.id.progress_Button_FragmentBookDetail);
        saveButton = view.findViewById(R.id.saveButton_FragmentBookDetail);
        rrl_5 = view.findViewById(R.id.rrl_5_FragmentBookDetail);
        dailyTarget_rl = view.findViewById(R.id.dailyTarget_RelativeLayout_FragmentBookDetail);
        input_RL = view.findViewById(R.id.progressInput_RelativeLayout_FragmentBookDetail);
        progressInput_et = view.findViewById(R.id.progressInput_EditText_FragmentBookDetail);
        inputDoneButton = view.findViewById(R.id.checkButton_ImageButton_FragmentBookDetail);
        barChart = view.findViewById(R.id.progress_chart_FragmentBookDetail);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle("Book Detail");

        if (! book.title.trim().isEmpty()) bookTitle_et.setText(book.title);
        author_et.setText(book.author);
        publication_et.setText(book.publication);
        pages_et.setText(String.valueOf(book.totalPages));

        owner_et.setText(book.owner);
        dailyTarget_tv.setText(String.valueOf(book.readingHistory.getDailyTarget()));
        completed_tv.setText(String.valueOf(book.readingHistory.getProgress(Calendar.getInstance()).progress));
        totalComplete_tv.setText(String.valueOf(book.readingHistory.getTotalProgress()));

        // set Bar Chart
        barChart.setBackgroundColor(Color.TRANSPARENT);
        barChart.setDrawGridBackground(false);
        barChart.setGridBackgroundColor(Color.GREEN);
        barChart.setDrawBorders(false);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        if (book.purchaseTime != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTimeInMillis(book.purchaseTime);

            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            entryDate_tv.setText(date);
        }
        setProgressView();

        saveButton.setOnClickListener(v -> {
            book.title = bookTitle_et.getText().toString().trim().isEmpty() ? "" : bookTitle_et.getText().toString().trim();
            book.author = author_et.getText().toString().trim().isEmpty() ? "" : author_et.getText().toString().trim();
            book.publication = publication_et.getText().toString().trim().isEmpty() ? "" : publication_et.getText().toString().trim();
            book.totalPages = pages_et.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(pages_et.getText().toString().trim());
            book.owner = owner_et.getText().toString().trim().isEmpty() ? "" : owner_et.getText().toString().trim();


            if (saveButtonListener != null && ! book.title.isEmpty()) saveButtonListener.onClick(book);
        });

        rrl_5.setOnClickListener(this::openDatePickerDialog);
        progress_bt.setOnClickListener(this::doProgress);
        dailyTarget_rl.setOnClickListener(this::showTargetInput);
        inputDoneButton.setOnClickListener(this::onTargetInputDone);
    }

    private void setProgressView() {
        dailyTarget_tv.setText(String.valueOf(book.readingHistory.getProgress(Calendar.getInstance()).target));
        completed_tv.setText(String.valueOf(book.readingHistory.getProgress(Calendar.getInstance()).progress));
        totalComplete_tv.setText(String.valueOf(book.readingHistory.getTotalProgress()));
        setChartData();
    }
    private void setChartData() {
        // month starting date and ending date
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),1);
        int lastDayOfMonth = selectedDate.getMaximum(Calendar.DAY_OF_MONTH);

        List<Integer> progressList = new ArrayList<>();
        for (int i = 1; i <= lastDayOfMonth; i++) {
            progressList.add( book.readingHistory.getProgress(selectedDate).progress);
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
        book.readingHistory.getProgress(Calendar.getInstance()).doProgress();
        setProgressView();
    }
    private void showTargetInput(View view) {
        if (input_RL.getVisibility() == View.VISIBLE) {
            input_RL.setVisibility(View.GONE);
        } else {
            input_RL.setVisibility(View.VISIBLE);
            progressInput_et.setText(String.valueOf(book.readingHistory.getDailyTarget()));
        }
    }
    private void onTargetInputDone(View view) {
        Progress progress = book.readingHistory.getProgress(Calendar.getInstance());
        progress.target = progressInput_et.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(progressInput_et.getText().toString().trim());
        book.readingHistory.commitProgress(progress, Calendar.getInstance());
        input_RL.setVisibility(View.GONE);
        setProgressView();
    }

    private void openDatePickerDialog (View v) {
        Calendar calendar = Calendar.getInstance();
        if (book.purchaseTime != 0) {
            calendar.clear();
            calendar.setTimeInMillis(book.purchaseTime);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this::setEntryDate, year, month, day);
        datePickerDialog.show();
    }
    private void setEntryDate (View view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        entryDate_tv.setText(date);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        book.purchaseTime = calendar.getTimeInMillis();
    }
    public FragmentBookDetail setSaveButtonListener(SaveButtonListener saveButtonListener) {
        this.saveButtonListener = saveButtonListener;
        return this;
    }

    public interface SaveButtonListener {
        void onClick(Book book);
    }
}