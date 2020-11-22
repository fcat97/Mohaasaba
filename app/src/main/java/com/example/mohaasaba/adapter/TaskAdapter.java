package com.example.mohaasaba.adapter;

import android.media.Image;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.Task;

import at.grabner.circleprogress.CircleProgressView;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskHolder> {
    private static final String TAG = "Task Adapter";
    private ItemClickedListener listener;

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.commitDate == newItem.commitDate;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = View.inflate(parent.getContext(), R.layout.item_fragment_todo, null);

        return new TaskHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = getItem(position);
        holder.textView.setText(task.text);
        holder.circleProgressView.setMaxValue(task.maxProgress);
        holder.circleProgressView.setValue(task.currentProgress);
        holder.circleProgressView.setBlockCount(task.maxProgress / task.step);
        
        holder.itemView.setOnClickListener(v -> {
            if (listener == null) try {
                throw new Exception("Must Implement method");
            } catch (Exception e) {
                e.printStackTrace();
            }
            listener.itemClicked(position);
        });

        holder.itemView.setOnLongClickListener(v -> {
            listener.itemLongClicked(position);
            return true;
        });

        if (task.getProgress() == 100f) {
            holder.circleProgressView.setVisibility(View.INVISIBLE);
            holder.completedImageView.setVisibility(View.VISIBLE);
        } else {
            holder.circleProgressView.setVisibility(View.VISIBLE);
            holder.completedImageView.setVisibility(View.INVISIBLE);
        }

    }

    static class TaskHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private CircleProgressView circleProgressView;
        private ImageView completedImageView;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView_itemView_FragmentTodo);
            circleProgressView = itemView.findViewById(R.id.circularProgressView_itemView_FragmentTodo);
            completedImageView = itemView.findViewById(R.id.completed_ImageView_itemView_FragmentTodo);
        }
    }

    public void setListener(ItemClickedListener listener) {
        this.listener = listener;
    }
    public interface ItemClickedListener {
        void itemClicked(int position);
        void itemLongClicked(int position);
    }
}
