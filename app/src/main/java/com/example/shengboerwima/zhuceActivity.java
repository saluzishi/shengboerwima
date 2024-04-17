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
import com.example.shengboerwima.entity.User;

public class zhuceActivity extends AppCompatActivity implements View.OnClickListener {
    //声明控件
    private Button mBtnLogin;
    // 用户id
    private EditText mEtUser;
    // 第一次输入密码
    private EditText mEtPassword1;
    // 第二次输入密码
    private EditText mEtPassword2;
    // 用户昵称
    private EditText mEtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);
        //找到控件中输入的内容
        mEtUser = findViewById(R.id.et_zc_1);
        mEtPassword1 = findViewById(R.id.et_zc_2);
        mEtPassword2 = findViewById(R.id.et_zc_3);
        mEtName = findViewById(R.id.et_zc_4);
        mBtnLogin = findViewById(R.id.btn_zc_login);
        mBtnLogin.setOnClickListener(this);
    }

    public void onClick(View v) {
        // 获取输入的数据
        String userid = mEtUser.getText().toString();
        String password1 = mEtPassword1.getText().toString();
        String password2 = mEtPassword2.getText().toString();
        String username = mEtName.getText().toString();

        // 首先要保证用户两次密码输入一致
        // 界面反应：成功则跳转并保存用户信息入数据库，失败则弹出报错信息
        // 数据库数据交互
        User user = new User();
        user.setUserAccount(userid);
        user.setUserPassword(password1);
        user.setUserName(username);
        user.setUserType(1);
        user.setUserState(0);
        user.setUserDel(0);

        new Thread() {
            @Override
            public void run() {
                int msg = 0;
                UserDao userDao = new UserDao();
                User uu = userDao.findUser(user.getUserAccount());
                if (uu != null) {
                    msg = 1;
                } else if(!(password1.equals(password2))) {
                    msg = 2;
                } else{
                    boolean flag = userDao.register(user);
                    if (flag) {
                        msg = 3;
                    }
                }
                hand.sendEmptyMessage(msg);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        // 定义交互信息
        String fail = "注册失败";
        String duplicate = "该账号已经存在，请换一个账号";
        String notMatch = "两次输入的密码不匹配，请重新输入！";
        String ok = "注册成功！";

        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                Toast.makeText(getApplicationContext(),fail,Toast.LENGTH_LONG).show();
            } else if(msg.what == 1) {
                Toast.makeText(getApplicationContext(),duplicate,Toast.LENGTH_LONG).show();
            } else if(msg.what == 2) {
                Toast.makeText(getApplicationContext(),notMatch,Toast.LENGTH_LONG).show();
            }
            else if(msg.what == 3) {
                Toast.makeText(getApplicationContext(), ok, Toast.LENGTH_LONG).show();
                Intent intent = null;
                intent = new Intent(zhuceActivity.this,dengluActivity.class);
                startActivity(intent);
                //将想要传递的数据用putExtra封装在intent中
                intent.putExtra("a","注册");
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        }
    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}