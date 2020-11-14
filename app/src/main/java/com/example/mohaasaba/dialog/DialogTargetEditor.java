package com.example.mohaasaba.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SwitchCompat;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.History;

public class DialogTargetEditor extends AppCompatDialogFragment {
    private static final String TAG = "DialogTargetEditor";
    private EditText maxProgressEditText;
    private TargetEditorListeners listeners;
    private EditText unitEditText;
    private EditText stepEditText;
    private History.Progress progress;
    private SwitchCompat todoProgress;
    private KeyListener unitKey;
    private KeyListener stepKey;
    private KeyListener maxProgressKey;

    public DialogTargetEditor(History.Progress progress, TargetEditorListeners listeners) {
        this.listeners = listeners;
        this.progress = progress;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.view_target_editor, null);
        maxProgressEditText = view.findViewById(R.id.maxProgress_EditText_ViewTargetEditor);
        unitEditText = view.findViewById(R.id.progressUnit_TextView_ViewTargetEditor);
        stepEditText = view.findViewById(R.id.step_TextView_ViewTargetEditor);
        todoProgress = view.findViewById(R.id.todoProgress_Switch_ViewTargetEditor);

        maxProgressKey = maxProgressEditText.getKeyListener();
        stepKey = stepEditText.getKeyListener();
        unitKey = unitEditText.getKeyListener();

        /* On todoSwitch state change */
        todoProgress.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                maxProgressEditText.setKeyListener(null);
                unitEditText.setKeyListener(null);
                stepEditText.setKeyListener(null);
            } else {
                maxProgressEditText.setKeyListener(maxProgressKey);
                unitEditText.setKeyListener(unitKey);
                stepEditText.setKeyListener(stepKey);
            }
        });

        if (progress != null && !progress.onTodo) {
            maxProgressEditText.setText(String.valueOf(progress.maxProgress));
            unitEditText.setText(progress.unit);
            stepEditText.setText(String.valueOf(progress.progressStep));
        } else if (progress != null) {
            todoProgress.setChecked(true);
        }


        builder.setView(view)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (todoProgress.isChecked()) {
                            listeners.onConfirm(0, "Todo", 1, todoProgress.isChecked());
                        } else {
                            if (maxProgressEditText.getText().toString().trim().isEmpty() || unitEditText.getText().toString().trim().isEmpty()) return;
                            int maxProgress = Integer.parseInt(maxProgressEditText.getText().toString());
                            String unit = unitEditText.getText().toString().trim();

                            int step = 1;
                            if (! stepEditText.getText().toString().isEmpty()) step = Integer.parseInt(stepEditText.getText().toString());
                            if (maxProgressEditText.getText().toString().isEmpty() || unitEditText.getText().toString().isEmpty()) return;
                            listeners.onConfirm(maxProgress, unit, step, todoProgress.isChecked());
                        }
                    }
                })
                .setCancelable(true)
                .setTitle("Edit Target");

        AlertDialog dialog = builder.create();
        return dialog;

    }


    public interface TargetEditorListeners{
        void onConfirm(int maxProgress, String unit, int step, boolean onTodo);
    }

}
