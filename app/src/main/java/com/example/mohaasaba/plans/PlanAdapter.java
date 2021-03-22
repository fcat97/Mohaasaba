package com.example.mohaasaba.plans;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;

import java.util.Calendar;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;

public class PlanAdapter extends ListAdapter<Plan, PlanAdapter.PlanHolder> {
    private static final String TAG = PlanAdapter.class.getCanonicalName();
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener longClickListener;

    public PlanAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Plan> DIFF_CALLBACK = new DiffUtil.ItemCallback<Plan>() {
        @Override
        public boolean areItemsTheSame(@NonNull Plan oldItem, @NonNull Plan newItem) {
            return oldItem.planID.equals(newItem.planID);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Plan oldItem, @NonNull Plan newItem) {
            float oldProgress = oldItem.getProgress(Calendar.getInstance());
            float newProgress = newItem.getProgress(Calendar.getInstance());

            if (! oldItem.label.equals(newItem.label)) return false;
            else if (! oldItem.tags.equals(newItem.tags)) return false;
            else return oldProgress == newProgress;
        }
    };

    @NonNull
    @Override
    public PlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan, parent, false);

        return new PlanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanHolder holder, int position) {
        Plan plan = getItem(position);

        holder.titleView.setText(plan.label);
        holder.tagsTextView.setText(plan.tags);
        Log.d(TAG + "Plan", "onBindViewHolder: called");

        Calendar today = Calendar.getInstance();
        List<SubPlan> subPlans = plan.getSubPlans(today);
        int maxProgress = subPlans.size();
        float old = holder.circleProgressView.getCurrentValue();
        float currentProgress = plan.getProgress(today);

        if (maxProgress != 0) { // if Schedule contains Task > 0
            holder.circleProgressView.setValueAnimated(old,currentProgress,1000);
            if (currentProgress == 100f) {
                holder.completeImageView.setVisibility(View.VISIBLE);
                holder.circleProgressView.setVisibility(View.INVISIBLE);
            } else {
                holder.completeImageView.setVisibility(View.INVISIBLE);
                holder.circleProgressView.setVisibility(View.VISIBLE);
            }
        } else holder.circleProgressView.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) onItemClickListener.onClick(plan);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onLongClick(plan);
                return true;
            } else return false;
        });

    }

    static class PlanHolder extends RecyclerView.ViewHolder {
        private TextView titleView, tagsTextView;
        private CircleProgressView circleProgressView;
        private ImageView tagIcon;
        private ImageView completeImageView;

        public PlanHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.titleTextView_ItemPlan);
            tagsTextView = itemView.findViewById(R.id.tagsTextView_ItemPlan);
            circleProgressView = itemView.findViewById(R.id.progress_circular_ItemPlan);
            tagIcon = itemView.findViewById(R.id.tagIcon_ItemPlan);
            completeImageView = itemView.findViewById(R.id.completed_ImageView_ItemRecyclerView_ItemPlan);

        }
    }

    public PlanAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public PlanAdapter setLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
        return this;
    }

    public interface OnItemClickListener {
        void onClick(Plan plan);
    }

    public interface OnItemLongClickListener {
        void onLongClick(Plan plan);
    }
}
