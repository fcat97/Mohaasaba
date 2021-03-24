package com.example.mohaasaba.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.models.Notify;

import java.text.DateFormat;
import java.util.Calendar;

public class NotifyAdapter extends ListAdapter<Notify, NotifyAdapter.NotifyHolder> {
    private OnItemClickCallBack onItemClickCallBack;
    private OnDeleteListener onDeleteListener;

    public NotifyAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Notify> DIFF_CALLBACK = new DiffUtil.ItemCallback<Notify>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notify oldItem, @NonNull Notify newItem) {
            return oldItem.notifyID.equals(newItem.notifyID);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notify oldItem, @NonNull Notify newItem) {
            if (oldItem.notificationHour != newItem.notificationHour) return false;
            else if (oldItem.notificationMinute != newItem.notificationMinute) return false;
            else return oldItem.message.equals(newItem.message);
        }
    };


    @NonNull
    @Override
    public NotifyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_reminder, parent, false);
        return new NotifyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyHolder holder, int position) {
        holder.message.setText(getItem(position).message);
        holder.sn.setText(String.valueOf(position + 1));
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.HOUR_OF_DAY, getItem(position).notificationHour);
        calendar.set(Calendar.MINUTE, getItem(position).notificationMinute);

        holder.time.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime()));

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickCallBack != null) onItemClickCallBack.onItemClick(getItem(position));
            else throw new ClassCastException("Must implement Listener");
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteListener != null) onDeleteListener.onDelete(getItem(position));
            else throw new ClassCastException("Must implement Listener");
        });
    }

    static class NotifyHolder extends RecyclerView.ViewHolder {
        private TextView sn;
        private TextView message;
        private TextView time;
        private ImageButton deleteButton;

        public NotifyHolder(@NonNull View itemView) {
            super(itemView);
            sn = itemView.findViewById(R.id.itemSL_TextView_itemFragmentReminder);
            message = itemView.findViewById(R.id.message_TextView_itemFragmentReminder);
            time = itemView.findViewById(R.id.time_TextView_itemFragmentReminder);
            deleteButton = itemView.findViewById(R.id.deleteButton_ImageButton__itemFragmentReminder);
        }
    }

    public NotifyAdapter setOnItemClickCallBack(OnItemClickCallBack onItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack;
        return this;
    }

    public NotifyAdapter setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
        return this;
    }

    public interface OnItemClickCallBack {
        void onItemClick(Notify notify);
    }
    public interface OnDeleteListener {
        void onDelete(Notify notify);
    }
}
