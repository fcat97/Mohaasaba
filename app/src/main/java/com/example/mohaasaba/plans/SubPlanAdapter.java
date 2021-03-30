package com.example.mohaasaba.plans;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;

import at.grabner.circleprogress.CircleProgressView;

public class SubPlanAdapter extends ListAdapter<SubPlan, SubPlanAdapter.SubPlanHolder> {
    private static final String TAG = SubPlanAdapter.class.getCanonicalName();
    private DeleteButtonListener deleteButtonListener;
    private ItemClickListener itemClickListener;
    private ItemLongPressListener longPressListener;

    public SubPlanAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<SubPlan> DIFF_CALLBACK = new DiffUtil.ItemCallback<SubPlan>() {
        @Override
        public boolean areItemsTheSame(@NonNull SubPlan oldItem, @NonNull SubPlan newItem) {
            return oldItem.label.equals(newItem.label);
        }

        @Override
        public boolean areContentsTheSame(@NonNull SubPlan oldItem, @NonNull SubPlan newItem) {
            return oldItem.equals(newItem);
        }
    };


    @NonNull
    @Override
    public SubPlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subplan, parent, false);

        return new SubPlanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubPlanHolder holder, int position) {
        SubPlan subPlan = getItem(position);

        // set circular progress view --------------------------------------------------------------
        float progress = 0f;
        if (subPlan.track_time) progress = (float) 100 * subPlan.time_progress / subPlan.time_goal;
        else if (subPlan.track_count) progress = (float) 100 * subPlan.count_progress / subPlan.count_goal;

        holder.circleProgressView.setMaxValue(100f);
        holder.circleProgressView.setValue(progress);
        if (progress == 100f) {
            holder.circleProgressView.setVisibility(View.INVISIBLE);
            holder.completeImageView.setVisibility(View.VISIBLE);
        } else {
            holder.circleProgressView.setVisibility(View.VISIBLE);
            holder.completeImageView.setVisibility(View.INVISIBLE);
        }

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteButtonListener != null) deleteButtonListener.onClick(position);
        });

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) itemClickListener.onClick(subPlan);
        });

        // set label  ------------------------------------------------------------------------------
        holder.titleView.setText(subPlan.label);

        // set goal Text ---------------------------------------------------------------------------
        String goalText = "Goal - ";
        if (subPlan.track_time) {
            long hr = (subPlan.time_goal / 1000 ) / 3600;
            long min = ((subPlan.time_goal / 1000) % 3600) / 60;
            long hr_done = (subPlan.time_progress / 1000 ) / 3600;
            long min_done = ((subPlan.time_progress / 1000) % 3600) / 60;

            goalText += hr_done + "h" + min_done + "m" + "/" + hr + "h" + min + "m";
        } else {
            if (subPlan.count_unit.isEmpty()) {
                goalText += subPlan.count_progress + "/" + subPlan.count_goal;
                goalText += subPlan.count_goal == 1 ? " time" : " times";
            }
            else goalText += subPlan.count_progress + "/" + subPlan.count_goal + " " + subPlan.count_unit;
        }
        goalText += " Done";
        holder.goalTextView.setText(goalText);

        // set onLongPress Listener ----------------------------------------------------------------
        holder.itemView.setOnLongClickListener(v -> {
            if (longPressListener != null) {
                longPressListener.onPress(subPlan);
                return true;
            } else return false;
        });
    }

    static class SubPlanHolder extends RecyclerView.ViewHolder {
        private CircleProgressView circleProgressView;
        private ImageView completeImageView;
        private TextView titleView, goalTextView;
        private ImageButton deleteButton;

        public SubPlanHolder(@NonNull View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.title_TextView_item_subplan);
            goalTextView = itemView.findViewById(R.id.goal_TextView_item_subplan);
            circleProgressView = itemView.findViewById(R.id.circularProgressView_item_subplan);
            completeImageView = itemView.findViewById(R.id.completed_ImageView_item_subplan);
            deleteButton = itemView.findViewById(R.id.deleteButton_ImageButton_item_subplan);
        }
    }

    public SubPlanAdapter setDeleteButtonListener(DeleteButtonListener deleteButtonListener) {
        this.deleteButtonListener = deleteButtonListener;
        return this;
    }

    public SubPlanAdapter setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public SubPlanAdapter setLongPressListener(ItemLongPressListener longPressListener) {
        this.longPressListener = longPressListener;
        return this;
    }

    public interface DeleteButtonListener {
        void onClick(int index);
    }
    public interface ItemClickListener {
        void onClick(SubPlan subPlan);
    }
    public interface ItemLongPressListener {
        void onPress(SubPlan subPlan);
    }
}
