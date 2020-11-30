package com.example.mohaasaba.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import com.example.mohaasaba.database.DataConverter;
import com.example.mohaasaba.models.Schedule;
import com.example.mohaasaba.models.Task;
import com.example.mohaasaba.helper.ThemeUtils;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import at.grabner.circleprogress.CircleProgressView;

public class RecyclerViewAdapter extends ListAdapter<Schedule, RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private OnClickListener listener;
    public RecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Schedule> DIFF_CALLBACK = new DiffUtil.ItemCallback<Schedule>() {
        @Override
        public boolean areItemsTheSame(@NonNull Schedule oldItem, @NonNull Schedule newItem) {
            return oldItem.getScheduleID().equals(newItem.getScheduleID());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Schedule oldItem, @NonNull Schedule newItem) {

            if (! oldItem.getTitle().equals(newItem.getTitle())) return false;
            else if (oldItem.getHistory() == null && newItem.getHistory() != null) return false;
            else if (! oldItem.getHistory().equals(newItem.getHistory())) return false;
            else if (oldItem.getThemeID() != newItem.getThemeID()) return false;
            else return oldItem.getTags().equals(newItem.getTags());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_recyclerview_main,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule = getItem(position);
        holder.titleView.setText(schedule.getTitle());

        Calendar today = Calendar.getInstance();
        List<Task> todoList = schedule.getHistory().getTasks(today);
        int maxProgress = todoList.size();
        float currentProgress = schedule.getHistory().getProgress(today);

        if (maxProgress != 0) { // if Schedule contains Task > 0
            holder.circleProgressView.setValue(currentProgress);
            if (currentProgress == 100f) {
                holder.completeImageView.setVisibility(View.VISIBLE);
                holder.circleProgressView.setVisibility(View.INVISIBLE);
            } else {
                holder.completeImageView.setVisibility(View.INVISIBLE);
                holder.circleProgressView.setVisibility(View.VISIBLE);
            }
        } else holder.circleProgressView.setVisibility(View.INVISIBLE);

        if (schedule.getTags().size() != 0) {
            String tags = DataConverter.joinListToString(schedule.getTags());
            holder.tagsTextView.setText(tags);
        }

        if (schedule.getThemeID() != -1001) {
            int[] attrs = {R.attr.colorPrimary};
            TypedArray typedArray = mContext.obtainStyledAttributes(ThemeUtils.getResourceID(schedule.getThemeID()), attrs);
            int colorPrimary = typedArray.getColor(0, Color.BLACK);
            typedArray.recycle();
            int[] attrs1 = {R.attr.colorAccent};
            TypedArray typedArray1 = mContext.obtainStyledAttributes(ThemeUtils.getResourceID(schedule.getThemeID()), attrs1);
            int colorAccent = typedArray1.getColor(0,Color.LTGRAY);
            typedArray1.recycle();

            holder.titleView.setTextColor(colorPrimary);
            holder.tagsTextView.setTextColor(colorPrimary);
            holder.tagIcon.setImageTintList(ColorStateList.valueOf(colorPrimary));
            holder.circleProgressView.setBarColor(colorAccent);

        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView, tagsTextView;
        private CircleProgressView circleProgressView;
        private ImageView tagIcon;
        private ImageView completeImageView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.titleTextView_mainActivity);
            tagsTextView = itemView.findViewById(R.id.tagsTextView_mainActivity);
            circleProgressView = itemView.findViewById(R.id.progress_circular_mainActivity);
            tagIcon = itemView.findViewById(R.id.tagIcon_mainActivity);
            completeImageView = itemView.findViewById(R.id.completed_ImageView_ItemRecyclerView_MainActivity);

            itemView.setOnClickListener(v -> {
                //There is a if check; Left for testing
                if (listener != null)
                    listener.onItemClicked(getItem(getAdapterPosition()));
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    try {
                        listener.onItemLongClicked(getItem(getAdapterPosition()));
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            });
        }
    }

    public interface OnClickListener {
        void onItemClicked(Schedule schedule);
        void onItemLongClicked(Schedule schedule) throws ExecutionException, InterruptedException;
    }
    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }
}
