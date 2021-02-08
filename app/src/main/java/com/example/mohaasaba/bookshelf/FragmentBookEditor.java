package com.example.mohaasaba.bookshelf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.mohaasaba.R;
import com.example.mohaasaba.helper.ViewMaker;
import com.example.mohaasaba.models.Progress;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;


public class FragmentBookEditor extends Fragment {
    private Book book;
    private BookRepo bookRepo;

    private EditText titleEditText;
    private EditText authorEditText;
    private EditText publicationEditText;
    private EditText pagesEditText;
    private EditText targetEditText;
    private TextView totalProgressTextView;
    private EditText todayProgressEditText;

    private FrameLayout frameLayout;
    private TextView confirmButton;
    private TextView deleteButton;
    private TextView moveButton1;
    private TextView moveButton2;
    private ImageView progressButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_book_details, container, false);
        titleEditText = view.findViewById(R.id.title_EditText_ViewBookDetails);
        authorEditText = view.findViewById(R.id.author_EditText_ViewBookDetails);
        publicationEditText = view.findViewById(R.id.publication_EditText_ViewBookDetails);
        pagesEditText = view.findViewById(R.id.pages_EditText_ViewBookDetails);
        targetEditText = view.findViewById(R.id.target_EditText_ViewBookDetails);
        totalProgressTextView = view.findViewById(R.id.totalProgress_EditText_ViewBookDetails);
        todayProgressEditText = view.findViewById(R.id.todayProgress_EditText_ViewBookDetails);

        frameLayout = view.findViewById(R.id.frameLayout_ViewBookDetails);
        confirmButton = view.findViewById(R.id.confirm_TextView_ViewBookDetails);
        deleteButton = view.findViewById(R.id.deleteButton_ViewBookDetails);
        moveButton1 = view.findViewById(R.id.moveButton1_ViewBookDetails);
        moveButton2 = view.findViewById(R.id.moveButton2_ViewBookDetails);
        progressButton = view.findViewById(R.id.progressButton_ImageView_ViewBookDetails);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bookRepo = new BookRepo(getContext());

        titleEditText.setText(book.title);
        authorEditText.setText(book.author);
        publicationEditText.setText(book.publication);
        pagesEditText.setText(String.valueOf(book.totalPages));
        targetEditText.setText(String.valueOf(book.readingHistory.getDailyTarget()));
        totalProgressTextView.setText(String.valueOf(book.readingHistory.getTotalProgress()));
        todayProgressEditText.setText(String.valueOf(book.readingHistory
                .getProgress(Calendar.getInstance()).currentProgress));


        // Setting DateSelectorView Logic ------------------------------------------------------------
        ViewMaker viewMaker = new ViewMaker(getContext());
        ViewMaker.DateSelectorView dateSelectorView = viewMaker.getDateSelectorView();
        dateSelectorView.setScheduleType(book.scheduleType);
        frameLayout.addView(dateSelectorView.getView());
        // Set parse schedule time and set to textView
        /*
        int startTime = type.startingMinute;
        int endingTime = type.endingMinute;
        String start;
        String end;
        if (startTime < 720) start = String.valueOf(startTime / 60) + " : " + String.valueOf(startTime % 60) + " AM";
        else start = String.valueOf((startTime / 60) - 12) + " : " + String.valueOf(startTime % 60) + " PM";
        if (endingTime < 720) end = String.valueOf(endingTime / 60) + " : " + String.valueOf(endingTime % 60) + " AM";
        else end = String.valueOf((endingTime / 60) - 12) + " : " + String.valueOf(endingTime % 60) + " PM";
        String time = start + " - " + end;
        tv_scheduleTime.setText(time);
         */

        // Setting Confirm Button Logic ------------------------------------------------------------
        confirmButton.setOnClickListener(v -> {
            book.title = titleEditText.getText().toString();
            book.author = authorEditText.getText().toString();
            book.publication = publicationEditText.getText().toString();
            book.totalPages = Integer.parseInt(pagesEditText.getText().toString());
            Progress progress = book.readingHistory.getProgress(Calendar.getInstance());
            progress.maxProgress = Integer.parseInt(
                    targetEditText.getText().toString().isEmpty() ? "0" : targetEditText.getText().toString());
            progress.currentProgress = Integer.parseInt(
                    todayProgressEditText.getText().toString().isEmpty() ? "0" : todayProgressEditText.getText().toString());
            book.readingHistory.commitProgress(progress, Calendar.getInstance());
            bookRepo.updateBook(book);

        });

        // Setting Delete Button Logic ------------------------------------------------------------
        deleteButton.setOnClickListener(v -> {
            bookRepo.deleteBook(book);

        });

        // Setting progress Button Logic -----------------------------------------------------------
        progressButton.setOnClickListener(v -> {
            book.readingHistory.commitProgress(book.readingHistory.getProgress(Calendar.getInstance())
                    .doProgress(), Calendar.getInstance());
            totalProgressTextView.setText(String.valueOf(book.readingHistory.getTotalProgress()));
            todayProgressEditText.setText(String.valueOf(book.readingHistory
                    .getProgress(Calendar.getInstance()).currentProgress));
        });

        // Setting Move to other reading status buttons logic --------------------------------------
        if (book.readingStatus == Book.ReadingStatus.READING ) moveButton1.setText(R.string.read);
        else if (book.readingStatus == Book.ReadingStatus.READ ||
                book.readingStatus == Book.ReadingStatus.WISH_LISTED) moveButton1.setText(R.string.reading);

        if (book.readingStatus == Book.ReadingStatus.READING ||
                book.readingStatus == Book.ReadingStatus.READ) moveButton2.setText(R.string.wishlist);
        else if (book.readingStatus == Book.ReadingStatus.WISH_LISTED) moveButton2.setText(R.string.read);

        moveButton1.setOnClickListener(v -> {
            if (book.readingStatus == Book.ReadingStatus.READING) book.readingStatus = Book.ReadingStatus.READ;
            else if (book.readingStatus == Book.ReadingStatus.READ) book.readingStatus = Book.ReadingStatus.READING;
            else book.readingStatus = Book.ReadingStatus.READING;
            bookRepo.updateBook(book);

        });

        moveButton2.setOnClickListener(v -> {
            if (book.readingStatus == Book.ReadingStatus.READING) book.readingStatus = Book.ReadingStatus.WISH_LISTED;
            else if (book.readingStatus == Book.ReadingStatus.READ) book.readingStatus = Book.ReadingStatus.WISH_LISTED;
            else book.readingStatus = Book.ReadingStatus.READ;
            bookRepo.updateBook(book);

        });
        //------------------------------------------------------------------------------------------

    }

    public FragmentBookEditor setBook(Book book) {
        this.book = book;
        return this;
    }
}
