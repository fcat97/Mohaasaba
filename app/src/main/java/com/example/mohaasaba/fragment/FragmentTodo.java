package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.TodoArrayAdapter;
import com.example.mohaasaba.database.Todo;

import java.text.DateFormat;
import java.util.Calendar;


public class FragmentTodo extends Fragment {
    private static final String TAG = "FragmentTodo";
    private Todo mTodo;
    private EditText addTodoEditText;
    private ImageButton addTodoButton;
    private Button datePickerButton;
    TodoArrayAdapter mAdapter;
    FragmentTodoListener todoListeners;

    public FragmentTodo(Todo todo, FragmentTodoListener todoListeners) {
        this.mTodo = todo;
        this.todoListeners = todoListeners;
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
        ListView listView = rootView.findViewById(R.id.listView_FragmentTodo);
        addTodoButton = rootView.findViewById(R.id.addButton_FragmentTodo);
        datePickerButton = rootView.findViewById(R.id.datePickerButton_FragmentTodo);
        addTodoEditText = rootView.findViewById(R.id.addTodo_EditText_FragmentTodo);
        mAdapter = new TodoArrayAdapter(getContext(), R.layout.item_fragment_todo, mTodo,
                new TodoArrayAdapter.TodoArrayAdapterListeners() {
                    @Override
                    public void onItemClicked(int position) {
                        todoListeners.onItemClicked(position);
                    }

                    @Override
                    public void onItemLongClicked(int position) {
                        todoListeners.onItemLongClicked(position);
                    }
                });
        listView.setAdapter(mAdapter);
        datePickerButton.setOnClickListener(v -> todoListeners.onDatePickerButtonClicked());
        addTodoButton.setOnClickListener(v -> {
            if (! addTodoEditText.getText().toString().trim().isEmpty()) {
                todoListeners.onAddTodoButtonClicked(addTodoEditText.getText().toString().trim());
                addTodoEditText.getText().clear();
            }
        });

        addTodoEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE  || event.getAction() == KeyEvent.ACTION_DOWN) {
                if (! addTodoEditText.getText().toString().trim().isEmpty()) {
                    todoListeners.onAddTodoButtonClicked(addTodoEditText.getText().toString().trim());
                    addTodoEditText.getText().clear();
                    return true;
                }
            }
            return false;
        });
        Calendar c = Calendar.getInstance();
        setDatePickerButtonText(c);
        return rootView;
    }

    public void setTodo(Todo todo) {
        mTodo = todo;
        mAdapter.setTodo(todo);
    }

    public void setDatePickerButtonText(Calendar calendar) {
        datePickerButton.setText(DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime()));
    }

    public interface FragmentTodoListener {
        void onItemClicked(int position);
        void onAddTodoButtonClicked(String todoText);
        void onDatePickerButtonClicked();
        void onItemLongClicked(int position);
    }
}