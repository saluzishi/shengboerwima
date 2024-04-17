package com.example.shengboerwima;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //声明控件
    private Button mBtnlogin1;
    private Button mBtnlogin2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //找到控件
        mBtnlogin1 = findViewById(R.id.btn_login1);
        mBtnlogin2 = findViewById(R.id.btn_login2);

        //实现直接跳转
        mBtnlogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(MainActivity.this,dengluActivity.class);
                startActivity(intent);
            }
        });
        mBtnlogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个意图对象，准备跳到指定的活动页面
                Intent intent = null;
                intent = new Intent(MainActivity.this,zhuceActivity.class);
                startActivity(intent);
            }
        });
    }
}