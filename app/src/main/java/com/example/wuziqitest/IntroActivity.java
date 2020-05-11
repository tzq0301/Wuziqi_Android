package com.example.wuziqitest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class IntroActivity extends AppCompatActivity {

    private TextView tvIntro;
    private Button mBtnStart;
    private Button mBtnReturn;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        tvIntro = findViewById(R.id.tv1);
        tvIntro.getBackground().setAlpha(100);
        tvIntro.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tvIntro.getBackground().setAlpha(180);
                    break;
                case MotionEvent.ACTION_UP:
                    tvIntro.getBackground().setAlpha(100);
                    break;
            }
            return true;
        });

        mBtnStart = findViewById(R.id.btn_intro_start);
        mBtnStart.getBackground().setAlpha(220);
        mBtnStart.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, PanelActivity.class);
            startActivity(intent);
        });

        mBtnReturn = findViewById(R.id.btn_intro_return);
        mBtnReturn.getBackground().setAlpha(220);
        mBtnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
