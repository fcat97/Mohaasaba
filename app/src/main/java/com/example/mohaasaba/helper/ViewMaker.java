package com.example.mohaasaba.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.CustomCalenderViewAdapter;
import com.example.mohaasaba.database.History;
import com.example.mohaasaba.database.Note;
import com.example.mohaasaba.database.Reminder;
import com.example.mohaasaba.database.Todo;

import java.text.DateFormat;
import java.util.Calendar;

import at.grabner.circleprogress.CircleProgressView;

public class ViewMaker {
    private static final String TAG = "ViewMaker";
    private Context mContext;

    public ViewMaker(Context context) {
        this.mContext = context;
    }

    public View getNoteView(Note note) {
        return new NoteView().getNoteView(note);
    }
    public View getReminderView(Reminder reminder) {
        return new ReminderView().getReminderView(reminder);
    }
    public View getTargetView(History history, TargetViewListeners targetViewListeners) {
        return new TargetView(targetViewListeners).getTargetView(history);
    }

    private class NoteView {
        private TextView noteTitle, noteDetail;
        private TextView lastModConstStr, lastModVar;
        private View noteView;
        private ImageButton overflowButton;
        private ImageButton expandButton;
        private ImageView noteIcon;
        private boolean expanded = false;

        public NoteView() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.noteView = inflater.inflate(R.layout.view_description_add_schedule,null);

            this.noteTitle = noteView.findViewById(R.id.title_EditText_DescriptionView);
            this.noteDetail = noteView.findViewById(R.id.detail_EditText_DescriptionView);
            this.lastModConstStr = noteView.findViewById(R.id.lastModifiedConstant_TextView_DescriptionView);
            this.lastModVar = noteView.findViewById(R.id.lastModifiedVariable_TextView_DescriptionView);
            this.overflowButton = noteView.findViewById(R.id.overflowButton_DescriptionView);
            this.expandButton = noteView.findViewById(R.id.expand_ImageButton_DescriptionView);
            this.noteIcon = noteView.findViewById(R.id.icon_descriptionView);


            lastModConstStr.setVisibility(View.VISIBLE);
            lastModVar.setVisibility(View.VISIBLE);
            overflowButton.setVisibility(View.VISIBLE);
        }

        public View getNoteView(Note note) {
            noteTitle.setText(note.getTitle());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                noteDetail.setText(Html.fromHtml(note.getDetail(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                noteDetail.setText(Html.fromHtml(note.getDetail()));
            }
            Calendar currentTime = Calendar.getInstance();
            if (currentTime.get(Calendar.DAY_OF_MONTH) > note.getDateModified().get(Calendar.DAY_OF_MONTH)){
                lastModVar.setText(DateFormat.getDateInstance(DateFormat.LONG).format(note.getDateModified().getTime()));
            } else if (currentTime.get(Calendar.DAY_OF_MONTH) == note.getDateModified().get(Calendar.DAY_OF_MONTH) &&
                    currentTime.get(Calendar.MINUTE) > note.getDateModified().get(Calendar.MINUTE) ) {
                lastModVar.setText(R.string.today_text);
            } else {
                lastModVar.setText(R.string.now_text);
            }


            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expanded) {
                        float deg = expandButton.getRotation() - 180F;
                        expandButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                        expanded = false;
                        noteDetail.setMaxLines(2);
                    } else {
                        float deg = expandButton.getRotation() + 180F;
                        expandButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                        expanded = true;
                        noteDetail.setMaxLines(10);
                    }
                }
            });
            return noteView;
        }
    }

    private class ReminderView {
        private TextView reminderTime;
        private TextView reminderDate;
        private TextView repeatInterval;
        private LinearLayout repeatIntervalLL;
        private ConstraintLayout constraintLayout;
        private View mReminderView;
        private ImageView bellIcon;

        public ReminderView () {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mReminderView = inflater.inflate(R.layout.view_reminder_add_schedule,null);

            reminderTime = mReminderView.findViewById(R.id.reminderTime_TextView_ReminderView);
            reminderDate = mReminderView.findViewById(R.id.reminderDate_TextView_ReminderView);
            repeatInterval = mReminderView.findViewById(R.id.repeat_interval_TextView_Reminder_View);
            repeatIntervalLL = mReminderView.findViewById(R.id.repeat_interval_linearL_ReminderView);
            constraintLayout = mReminderView.findViewById(R.id.constrainLayout_ReminderView);
            bellIcon = mReminderView.findViewById(R.id.bell_icon_ReminderView);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public View getReminderView(Reminder reminder) {
            reminderTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(reminder.getReminderTime().getTime()));
            reminderDate.setText(DateFormat.getDateInstance(DateFormat.LONG).format(reminder.getReminderTime().getTime()));
            if (reminder.getRepeatingInterval() != Reminder._NULL_) {
                String text = reminder.getRepeatingInterval() + " " + reminder.getRepeatIntervalUnit();
                repeatInterval.setText(text);
            } else repeatIntervalLL.setVisibility(View.GONE);
            if (reminder.getReminderTime().getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) {
                bellIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bell_off,mContext.getTheme()));
                constraintLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorRed));
            }

            return mReminderView;
        }
    }

    private  class TargetView {
        private CircleProgressView mCircleProgressView;
        private TextView topTextView;
        private TextView bottomTextView;
        private Button incrementButton;
        private View rootView;
        private TargetViewListeners listeners;
        private int maxProgress, currentProgress, progressStep;
        private String unit;
        private ImageButton expandButton;
        private ImageView completedImageView;
        private RecyclerView recyclerView;
        private boolean isExpandable = true;
        CustomCalenderViewAdapter adapter;

        public TargetView(TargetViewListeners listeners) {
            this.listeners = listeners;

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater.inflate(R.layout.view_target_add_schedule, null);


            mCircleProgressView = rootView.findViewById(R.id.progress_circular_viewTarget_AddScheduleActivity);
            topTextView = rootView.findViewById(R.id.textView1_viewTarget_AddScheduleActivity);
            bottomTextView = rootView.findViewById(R.id.textView3_viewTarget_AddScheduleActivity);
            recyclerView = rootView.findViewById(R.id.recyclerView_viewTarget_AddScheduleActivity);
            completedImageView = rootView.findViewById(R.id.completed_ImageView_viewTarget_AddScheduleActivity);

            incrementButton = rootView.findViewById(R.id.incrementButton_viewTarget_AddScheduleActivity);
            expandButton = rootView.findViewById(R.id.expand_ImageButton_viewTarget_AddScheduleActivity);
        }

        public View getTargetView(History history) {
            History.Progress progress = history.getProgressOf(Calendar.getInstance());
            /* make sure progress != null when you call this method
            *  else you will get null rather than View */
            if (progress == null) return null;
            if (progress.onTodo) {
                Todo todo = history.getTodo(Calendar.getInstance());
                currentProgress = 0;
                maxProgress = todo.getStates().size();
                for (boolean state :
                        todo.getStates()) {
                    if (state) currentProgress++;
                }
                unit = mContext.getResources().getString(R.string.todo_text);
                incrementButton.setText(R.string.complete_todo);
                incrementButton.setTextSize(9f);
                if (maxProgress == currentProgress) {

                }
            } else {
                int currentProgress = progress.currentProgress;
                maxProgress = progress.maxProgress;
                this.currentProgress = currentProgress;
                progressStep = progress.progressStep;
                unit = progress.unit;
            }

            topTextView.setText(buildTextViewText(currentProgress, unit));
            bottomTextView.setText(buildTextViewText(maxProgress, unit));
            mCircleProgressView.setValue(currentProgress);
            if (maxProgress == 0) mCircleProgressView.setMaxValue(10);
            else mCircleProgressView.setMaxValue(maxProgress);

            if (maxProgress == currentProgress) {
                completedImageView.setVisibility(View.VISIBLE);
                mCircleProgressView.setVisibility(View.INVISIBLE);
            }

            adapter = new CustomCalenderViewAdapter(history);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 7);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);

            expandButton.setOnClickListener(v -> {
                if (isExpandable) {
                    recyclerView.setVisibility(View.VISIBLE);
                    isExpandable = false;
                    float deg = 180F;
                    expandButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                    isExpandable = true;
                    float deg = 0F;
                    expandButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                }
            });

            incrementButton.setOnClickListener(v -> {
                if (progress.onTodo) return;
                incrementProgress();
            });

            incrementButton.setOnLongClickListener(v -> {
                if (progress.onTodo) return false;
                decrementProgress();
                return true;
            });

            rootView.setOnLongClickListener(v -> {
                listeners.onViewLongClicked();
                return true;
            });
            return rootView;
        }

        private String buildTextViewText(int progress, String unit) {
            return progress + " " + unit;
        }

        private void incrementProgress() {
            int animationStartAt = currentProgress;
            if (currentProgress + progressStep >= maxProgress) currentProgress = maxProgress;
            else currentProgress = currentProgress + progressStep;

            topTextView.setText(buildTextViewText(currentProgress, unit));
            mCircleProgressView.setValueAnimated(animationStartAt, currentProgress, 500);
            if (maxProgress == currentProgress) {
                /* Animate circularProgressView to hide */
                /* https://stackoverflow.com/questions/6796139/fade-in-fade-out-android-animation-in-java?noredirect=1&lq=1 */
                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new DecelerateInterpolator()); //add this
                fadeOut.setDuration(1000);

                AnimationSet invisibleAnimation = new AnimationSet(false); //change to false
                invisibleAnimation.addAnimation(fadeOut);
                /*animation.addAnimation(fadeOut);*/
                mCircleProgressView.setAnimation(invisibleAnimation);
                mCircleProgressView.setVisibility(View.INVISIBLE);

                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new AccelerateInterpolator()); //and this
                fadeIn.setDuration(1000);
                AnimationSet visibleAnimation = new AnimationSet(false);
                visibleAnimation.addAnimation(fadeIn);
                completedImageView.setAlpha(0f);
                completedImageView.setVisibility(View.VISIBLE);
                completedImageView.setAnimation(visibleAnimation);
                completedImageView.setAlpha(1f);
            }
            listeners.onIncrementButtonClicked(currentProgress);
            adapter.refresh();
        }
        private void decrementProgress() {
            if (maxProgress == currentProgress) {
                /* Animate circularProgressView to show */
                /* https://stackoverflow.com/questions/6796139/fade-in-fade-out-android-animation-in-java?noredirect=1&lq=1 */
                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new DecelerateInterpolator()); //add this
                fadeOut.setDuration(1000);

                AnimationSet invisibleAnimation = new AnimationSet(false); //change to false
                invisibleAnimation.addAnimation(fadeOut);
                completedImageView.setAnimation(invisibleAnimation);
                completedImageView.setVisibility(View.INVISIBLE);

                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new AccelerateInterpolator()); //and this
                fadeIn.setDuration(1000);
                AnimationSet visibleAnimation = new AnimationSet(false);
                visibleAnimation.addAnimation(fadeIn);
                mCircleProgressView.setAlpha(0f);
                mCircleProgressView.setVisibility(View.VISIBLE);
                mCircleProgressView.setAnimation(visibleAnimation);
                mCircleProgressView.setAlpha(1f);
            }

            int animationStartAt = currentProgress;
            if (currentProgress - progressStep < 0) currentProgress = 0;
            else currentProgress = currentProgress - progressStep;

            topTextView.setText(buildTextViewText(currentProgress, unit));
            mCircleProgressView.setValueAnimated(animationStartAt, currentProgress, 500);
            listeners.onIncrementButtonLongClicked(currentProgress);
            adapter.refresh();
        }

    }


    public interface TargetViewListeners {
        void onIncrementButtonClicked(int afterClickedProgress);
        void onIncrementButtonLongClicked(int afterClickedProgress);
        void onViewLongClicked();
    }

}