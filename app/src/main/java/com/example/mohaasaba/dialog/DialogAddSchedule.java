package com.example.mohaasaba.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.AppRepository;
import com.example.mohaasaba.helper.ThemeUtils;
import com.example.mohaasaba.models.Schedule;

public class DialogAddSchedule extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = View.inflate(getContext(), R.layout.view_dialog_add_schedule, null);

        EditText editText = rootView.findViewById(R.id.title_editText_DialogAddSchedule);
        AppRepository repository = new AppRepository(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setTitle(R.string.enter_plan_name)
                .setPositiveButton(R.string.confirm_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = editText.getText().toString();
                        Schedule schedule = new Schedule(s);
                        repository.insertSchedule(schedule);
                    }
                });

        return builder.create();
    }
}
