package com.example.wuziqitest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

public class PanelActivity extends AppCompatActivity {

    private WuziqiPanel wuziqiPanel;

    private Button mBtnRestart;
    private Button mBtnReturn;
    private Button mBtnRetract;
    private Chronometer mCh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        wuziqiPanel = findViewById(R.id.panel);

        mBtnRestart = findViewById(R.id.btn_restart);
        mBtnRestart.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PanelActivity.this);
            builder.setTitle("请确定").setMessage("确定要重新开始")
                    .setPositiveButton("确定", (dialog, which) -> wuziqiPanel.restart())
                    .setNegativeButton("取消", (dialog, which) -> {}).show();
        });

        mBtnReturn = findViewById(R.id.btn_return);
        mBtnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(PanelActivity.this, MainActivity.class);
            startActivity(intent);
        });

        mBtnRetract = findViewById(R.id.btn_retract);
        mBtnRetract.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PanelActivity.this);
            builder.setTitle("请确定").setMessage("确定进行一次悔棋操作\n充值可进行两次悔棋操作")
                    .setPositiveButton("确定", (dialog, which) -> wuziqiPanel.retract())
                    .setNeutralButton("充值", (dialog, which) -> {
                        Toast.makeText(this, "充值功能尚未开发完全\n敬请期待！", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("取消", (dialog, which) -> {}).show();
        });

        mCh = findViewById(R.id.ch);
        mCh.setFormat("本局游戏已进行：%s");
        mCh.start();
        mCh.setOnChronometerTickListener(chronometer -> {
            if (wuziqiPanel.getMIsGameOver()) {
                mCh.stop();
            }
        });

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    //在标题栏引用菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_panel, menu);
        return true;
    }
    //设置菜单栏中选项触发的条件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_panel_restart) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PanelActivity.this);
            builder.setTitle("请确定").setMessage("确定要重新开始")
                    .setPositiveButton("确定", (dialog, which) -> wuziqiPanel.restart())
                    .setNegativeButton("取消", (dialog, which) -> {}).show();
            return  true;
        }
        if (id == R.id.menu_panel_retract) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PanelActivity.this);
            builder.setTitle("请确定").setMessage("确定进行一次悔棋操作\n充值可进行两次悔棋操作")
                    .setPositiveButton("确定", (dialog, which) -> wuziqiPanel.retract())
                    .setNeutralButton("充值", (dialog, which) -> {
                        Toast.makeText(this, "充值功能尚未开发完全\n敬请期待！", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("取消", (dialog, which) -> {}).show();
            return true;
        }
        if (id == R.id.menu_panel_skills) {
            Intent intent = new Intent(PanelActivity.this, WebSkillsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
