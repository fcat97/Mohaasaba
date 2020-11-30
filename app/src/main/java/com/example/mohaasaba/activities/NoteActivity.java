package com.example.mohaasaba.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_At;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Hr;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Link;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Quote;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Subscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Superscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.example.mohaasaba.R;
import com.example.mohaasaba.models.Note;
import com.example.mohaasaba.helper.ThemeUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Calendar;

public class NoteActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "com.example.mohasabap.EXTRA_NOTE";
    private static final String TAG = "NoteActivity";

    private EditText mTitleEditText;
    private Note mNote;

    private IARE_Toolbar mToolbar;
    private AREditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int themeID = getIntent().getIntExtra(AddScheduleActivity.EXTRA_THEME_ID, -1001);
        if (themeID != -1001) setTheme(ThemeUtils.getResourceID(themeID));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Toolbar toolbar = findViewById(R.id.toolbar_noteActivity);
        setSupportActionBar(toolbar);

        initToolBar();

        mTitleEditText = findViewById(R.id.title_EditText_NoteActivity);
        mEditText.requestFocus();
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NOTE)) {
            this.mNote = intent.getParcelableExtra(EXTRA_NOTE);
            mTitleEditText.setText(mNote.getTitle());
            mEditText.fromHtml(mNote.getDetail());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_menuItem_noteActivity) {
            createNote();
            if (mNote != null) saveNote(mNote);
            else Toast.makeText(this, "Write some notes first", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void initToolBar() {
        mToolbar = this.findViewById(R.id.areToolbar);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem strikethrough = new ARE_ToolItem_Strikethrough();
        IARE_ToolItem quote = new ARE_ToolItem_Quote();
        IARE_ToolItem listNumber = new ARE_ToolItem_ListNumber();
        IARE_ToolItem listBullet = new ARE_ToolItem_ListBullet();
        IARE_ToolItem hr = new ARE_ToolItem_Hr();
        IARE_ToolItem link = new ARE_ToolItem_Link();
        IARE_ToolItem subscript = new ARE_ToolItem_Subscript();
        IARE_ToolItem superscript = new ARE_ToolItem_Superscript();
        IARE_ToolItem left = new ARE_ToolItem_AlignmentLeft();
        IARE_ToolItem center = new ARE_ToolItem_AlignmentCenter();
        IARE_ToolItem right = new ARE_ToolItem_AlignmentRight();
//        IARE_ToolItem image = new ARE_ToolItem_Image();
//        IARE_ToolItem video = new ARE_ToolItem_Video();
        IARE_ToolItem at = new ARE_ToolItem_At();
        mToolbar.addToolbarItem(bold);
        mToolbar.addToolbarItem(italic);
        mToolbar.addToolbarItem(underline);
        mToolbar.addToolbarItem(strikethrough);
        mToolbar.addToolbarItem(quote);
        mToolbar.addToolbarItem(listNumber);
        mToolbar.addToolbarItem(listBullet);
        mToolbar.addToolbarItem(hr);
        mToolbar.addToolbarItem(link);
        mToolbar.addToolbarItem(subscript);
        mToolbar.addToolbarItem(superscript);
        mToolbar.addToolbarItem(left);
        mToolbar.addToolbarItem(center);
        mToolbar.addToolbarItem(right);
//        mToolbar.addToolbarItem(image);
//        mToolbar.addToolbarItem(video);
        mToolbar.addToolbarItem(at);

        mEditText = this.findViewById(R.id.arEditText);
        mEditText.setToolbar(mToolbar);
        mEditText.setTextAppearance(this, R.style.textRegular);
    }

    private void createNote() {
        String detail = mEditText.getHtml();
        String title = mTitleEditText.getText().toString().trim();
        Document document = Jsoup.parse(detail);
        String text = document.body().text();

        if (!text.isEmpty()) {
            if (title.isEmpty()) {
                /* Details at https://stackoverflow.com/questions/9825798/how-to-read-a-text-from-a-web-page-with-java */
                if (text.length() >= 70) title = makeTitle(text.substring(0,30));
                else title = makeTitle(text);
            }
            if (mNote == null) mNote = new Note(detail);
            else mNote.setDetail(detail);
            mNote.setTitle(title);
        }

    }

    private void saveNote(Note note) {
        Intent data = new Intent();
        note.setDateModified(Calendar.getInstance());
        data.putExtra(EXTRA_NOTE,note);
        setResult(RESULT_OK,data);
        finish();
    }

    //TODO: Need to rewrite this method
    private String makeTitle(String detail) {
        String[] words = detail.split(" ");
        StringBuilder title = new StringBuilder();
        for (int i=0; i < words.length && i < 7; i++) {
            title.append(" ").append(words[i]);
        }
        return title.toString();
    }
}