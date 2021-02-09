package com.example.mohaasaba.bookshelf;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.helper.ViewMaker;
import com.example.mohaasaba.models.Schedule;
import com.example.mohaasaba.models.ScheduleType;

import at.grabner.circleprogress.CircleProgressView;

public class BookAdapter extends ListAdapter<Book, BookAdapter.ViewHolder> {
    private ItemClickListener itemClickListener;

    public BookAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<Book> DIFF_CALLBACK = new DiffUtil.ItemCallback<Book>() {
        @Override
        public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return oldItem.bookID.equals(newItem.bookID);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_taalim_reading, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = getItem(position);

        holder.tv_bookTitle.setText(book.title);
        holder.tv_bookAuthor.setText(book.author == null ? " " : book.author);

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) itemClickListener.onClick(book);
        });

        // set schedule time
        /*
        int startTime = book.scheduleType.startingMinute;
        int endingTime = book.scheduleType.endingMinute;
        String start;
        String end;
        if (startTime < 720) start = String.valueOf(startTime / 60) + " : " + String.valueOf(startTime % 60) + " AM";
        else start = String.valueOf((startTime / 60) - 12) + " : " + String.valueOf(startTime % 60) + " PM";
        if (endingTime < 720) end = String.valueOf(endingTime / 60) + " : " + String.valueOf(endingTime % 60) + " AM";
        else end = String.valueOf((endingTime / 60) - 12) + " : " + String.valueOf(endingTime % 60) + " PM";
        String time = start + " - " + end;
        */

    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_bookTitle;
        private TextView tv_bookAuthor;
        private ImageView bookIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_bookTitle = itemView.findViewById(R.id.title_TextView_ItemTalim);
            tv_bookAuthor = itemView.findViewById(R.id.author_TextView_ItemTalim);
            bookIcon = itemView.findViewById(R.id.bookIcon_ImageView_ItemTaalim);
        }
    }

    public BookAdapter setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }
    public interface ItemClickListener {
        void onClick(Book book);
    }
}
