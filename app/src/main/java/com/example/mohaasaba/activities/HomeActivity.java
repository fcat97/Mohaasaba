package com.example.mohaasaba.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mohaasaba.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar_HomeActivity);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        Button button_1 = findViewById(R.id.btn1_HomeActivity); button_1.setOnClickListener(this);
        Button button_2 = findViewById(R.id.btn2_HomeActivity); button_2.setOnClickListener(this);
        Button button_3 = findViewById(R.id.btn3_HomeActivity); button_3.setOnClickListener(this);
        Button button_4 = findViewById(R.id.btn4_HomeActivity); button_4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn1_HomeActivity) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}