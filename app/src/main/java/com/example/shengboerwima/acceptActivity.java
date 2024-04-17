package com.example.shengboerwima;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shengboerwima.shop_list.InfoListActivity;

public class acceptActivity extends AppCompatActivity {

    //声明控件
    private Button mBtnNewlogin1;
    private Button mBtnNewlogin2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        //找到控件
        mBtnNewlogin1 = findViewById(R.id.mBtnNewlogin1);
        mBtnNewlogin2 = findViewById(R.id.mBtnNewlogin2);

        //实现直接跳转
        mBtnNewlogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(acceptActivity.this,InfoListActivity.class);
                startActivity(intent);
            }
        });
        mBtnNewlogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个意图对象，准备跳到指定的活动页面
                Intent intent = null;
                intent = new Intent(acceptActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

}