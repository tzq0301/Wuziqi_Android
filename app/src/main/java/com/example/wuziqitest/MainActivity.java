package com.example.wuziqitest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button mBtnStart;
    private Button mBtnIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnStart = findViewById(R.id.btn_start);
        mBtnStart.getBackground().setAlpha(220);
        mBtnStart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PanelActivity.class);
            startActivity(intent);
        });

        mBtnIntro = findViewById(R.id.btn_intro);
        mBtnIntro.getBackground().setAlpha(220);
        mBtnIntro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

}

