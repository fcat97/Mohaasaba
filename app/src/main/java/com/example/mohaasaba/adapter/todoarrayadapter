package com.example.mohaasaba.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.Todo;

import java.util.List;

public class TodoArrayAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<Boolean> states;
    private List<String> todoTexts;
    private TodoArrayAdapterListeners listeners;


    public TodoArrayAdapter(@NonNull Context context, int resource, Todo todo, TodoArrayAdapterListeners listeners) {
        super(context, resource);
        this.mContext = context;
        this.states = todo.getStates();
        this.todoTexts = todo.getTodoTexts();
        this.listeners = listeners;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = View.inflate(mContext, R.layout.item_fragment_todo, null);
        RelativeLayout relativeLayout = itemView.findViewById(R.id.RelativeLayout1_itemView_FragmentTodo);
        RelativeLayout optionRelativeLayout = itemView.findViewById(R.id.RelativeLayout2_itemView_FragmentTodo);
        TextView textView = itemView.findViewById(R.id.textView_itemView_FragmentTodo);
        CheckBox checkBox = itemView.findViewById(R.id.checkBox_itemView_FragmentTodo);

        checkBox.setChecked(states.get(position));
        textView.setText(todoTexts.get(position));

        relativeLayout.setOnClickListener(v -> {
            listeners.onItemClicked(position);
        });
        relativeLayout.setOnLongClickListener(v -> {
            listeners.onItemLongClicked(position);
            return true;
        });
        checkBox.setOnClickListener(v -> listeners.onItemClicked(position));
        return itemView;
    }

    @Override
    public int getCount() {
        return todoTexts.size();
    }

    public void setTodo(Todo todo) {
        this.states = todo.getStates();
        this.todoTexts = todo.getTodoTexts();
        notifyDataSetInvalidated();
    }

    public interface TodoArrayAdapterListeners{
        void onItemClicked(int position);
        void onItemLongClicked(int position);
    }

}
