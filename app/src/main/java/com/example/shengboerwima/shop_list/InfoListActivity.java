package com.example.shengboerwima.shop_list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shengboerwima.R;
import com.example.shengboerwima.dao.ShopDao;
import com.example.shengboerwima.entity.Shop;
import com.example.shengboerwima.shop_grid.ShopDescActivity;

import java.util.ArrayList;
import java.util.List;

public class InfoListActivity extends AppCompatActivity implements View.OnClickListener {
    EditText searchEt;
    ImageView searchIv, flushIv;
    ListView showLv;
    // 数据源
    List<Shop> mDatas = new ArrayList<>();      // 所有商铺列表
    List<Shop> allShopList = new ArrayList<>(); // 所有商铺列表
    List<Shop> searchResult = new ArrayList<>(); // 搜索结果
    private InfoListAdapter adapter;
    boolean isSearching = false; // 是否在搜索模式下

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);
        // 1.查找控件
        initView();
        // 哪些函数需要激活和数据库交互全部放到Thread处理
        new Thread() {
            @Override
            public void run() {
                // 2.找到ListView对应的数据源
                allShopList = ShopDao.listShopItem();
                mDatas.addAll(allShopList);
            }
        }.start();
        // 3.创建适配器  BaseAdapter的子类
        adapter = new InfoListAdapter(this, mDatas);
        // 4.设置适配器
        showLv.setAdapter(adapter);
        // 设置单向点击监听功能
        setListener();
        // 自动刷新界面
        autoRefresh();
    }

    // 刷新图片列表
    private void flushList(){
        if (isSearching){
            mDatas.clear();
            mDatas.addAll(searchResult);
        } else {
            mDatas.clear();
            mDatas.addAll(allShopList);
        }
        adapter.notifyDataSetChanged();
    }

    // 自动刷新界面，每秒刷新一次图片列表
    private void autoRefresh(){
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    flushList();
                    break;
            }
        }
    };

    private void setListener() {
        showLv.setOnItemClickListener((parent, view, position, id) -> {
            Shop shop = mDatas.get(position);
            Intent intent = new Intent(InfoListActivity.this, ShopDescActivity.class);
            intent.putExtra("shop", shop);
            startActivity(intent);
        });
    }

    private void initView() {
        searchEt = findViewById(R.id.info_et_search);
        searchIv = findViewById(R.id.info_iv_search);
        flushIv = findViewById(R.id.info_iv_flush);
        showLv = findViewById(R.id.infolist_lv);
        searchIv.setOnClickListener(this); //添加点击事件的监听器
        flushIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_iv_flush:  // 刷新点击
                isSearching = false; // 结束搜索模式
                searchEt.setText("");
                flushList(); // 刷新图片列表
                break;
            case R.id.info_iv_search:  // 搜索点击
                isSearching = true; // 进入搜索模式
                String msg = searchEt.getText().toString().trim();  //获取输入信息
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this,"输入内容不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                searchResult.clear(); // 清空上次搜索结果
                for (int i = 0; i < allShopList.size(); i++) {
                    String title = allShopList.get(i).getShopName();
                    if (title.contains(msg)) {
                        searchResult.add(allShopList.get(i));
                    }
                }
                flushList(); // 刷新图片列表
                break;
        }
        searchEt.setText(""); // 清空搜索框内容
    }
}
