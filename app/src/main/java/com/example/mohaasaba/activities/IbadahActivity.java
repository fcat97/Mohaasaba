package com.example.mohaasaba.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.mohaasaba.R;
import com.example.mohaasaba.bookshelf.TalimActivity;

import soup.neumorphism.NeumorphCardView;

public class IbadahActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibadah);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.colorGray));
        }

        NeumorphCardView talimCard = findViewById(R.id.talim_CardView_IbadahActivity);
        talimCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, TalimActivity.class);
            startActivity(intent);
        });


    }

}