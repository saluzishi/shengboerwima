package com.example.shengboerwima;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shengboerwima.dao.UserDao;

public class dengluActivity extends AppCompatActivity implements View.OnClickListener {
    //声明控件
    private Button mBtnLogin;
    private EditText mEtUser;
    private EditText mEtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denglu);
        //找到控件
        mEtUser = findViewById(R.id.et_dl_1);
        mEtPassword = findViewById(R.id.et_dl_2);
        mBtnLogin = findViewById(R.id.btn_dl_login);
        //匹配正确的密码
        mBtnLogin.setOnClickListener(this);
    }

    public void onClick(View v) {
        // 获取输入的数据
        String username = mEtUser.getText().toString();
        String password = mEtPassword.getText().toString();

        // 验证用户名和密码
        new Thread() {
            @Override
            public void run() {
                UserDao userDao = new UserDao();
                int msg = userDao.login(username, password);
                hand1.sendEmptyMessage(msg);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler() {
        // 弹出内容设置（定义交互信息）
        String fail = "登录失败！";
        String ok = "登录成功！";
        String password_err = "密码错误！";
        String account_err = "账号不存在";

        @Override
        public void handleMessage(Message msg) {
            Intent intent = null;
            if (msg.what == 0) {
                Toast.makeText(getApplicationContext(), fail, Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), ok, Toast.LENGTH_LONG).show();
                intent = new Intent(dengluActivity.this, acceptActivity.class);
                startActivity(intent);
            } else if (msg.what == 2) {
                Toast.makeText(getApplicationContext(), password_err, Toast.LENGTH_LONG).show();
            } else if (msg.what == 3) {
                Toast.makeText(getApplicationContext(), account_err, Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}