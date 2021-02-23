package com.example.mohaasaba.ibadah.bookshelf;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.mohaasaba.R;
import com.example.mohaasaba.fragment.FragmentEditReminder;
import com.example.mohaasaba.models.Notify;

import java.util.List;

public class TalimActivity extends AppCompatActivity {
    public static final String TAG = TalimActivity.class.getCanonicalName();

    private FragmentBooks fragmentBooksReading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talim);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.colorWhite));
        }

        BookRepo bookRepo = new BookRepo(this);
        LiveData<List<Book>> allBooks = bookRepo.getAllBooks();

        fragmentBooksReading = new FragmentBooks(allBooks)
                .setItemClickedListener(this::openBookEditor)
                .setAddButtonListener(this::openBookEditor);

        openFragmentBooks();
    }

    private void openBookEditor(Book book) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_TalimActivity, FragmentBookDetail.newInstance(book))
                .addToBackStack("Fragment Book Detail")
                .commit();
    }


    private void openFragmentBooks() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout_TalimActivity, fragmentBooksReading)
                .commit();
    }
}