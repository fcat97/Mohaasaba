package com.example.mohaasaba.bookshelf;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.mohaasaba.R;
import com.example.mohaasaba.fragment.FragmentEditReminder;
import com.example.mohaasaba.models.Notify;
import com.example.mohaasaba.receivers.NotificationScheduler;

import java.util.List;

public class TalimActivity extends AppCompatActivity {
    public static final String TAG = TalimActivity.class.getCanonicalName();

    private FragmentBooks fragmentBooksReading;
    private FragmentBookDetail fragmentBookDetail;
    private BookRepo bookRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talim);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.colorWhite));
        }

        bookRepo = new BookRepo(this);
        LiveData<List<Book>> allBooks = bookRepo.getAllBooks();

        fragmentBooksReading = new FragmentBooks(allBooks)
                .setItemClickedListener(this::openBookEditor)
                .setAddButtonListener(this::openBookEditor);

        openFragmentBooks();
    }

    private void openBookEditor(Book book) {
        fragmentBookDetail = FragmentBookDetail.newInstance(book)
                .setSaveButtonListener(book1 -> {
                    bookRepo.updateBook(book1);
                    getSupportFragmentManager().popBackStack();
                })
                .setNotifyListeners(this::showEditReminderFragment);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_TalimActivity, fragmentBookDetail)
                .addToBackStack("Fragment Book Detail")
                .commit();
    }


    private void openFragmentBooks() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout_TalimActivity, fragmentBooksReading)
                .commit();
    }
    private void showEditReminderFragment(Notify notify) {
        FragmentEditReminder fragmentEditReminder = new FragmentEditReminder(notify);
        fragmentEditReminder.show(getSupportFragmentManager(), "Fragment Edit Reminder");
        fragmentEditReminder.setListeners(() -> fragmentBookDetail.notifyEditConfirmed(notify));
    }
}