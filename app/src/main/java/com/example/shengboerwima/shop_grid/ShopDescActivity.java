package com.example.shengboerwima.shop_grid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shengboerwima.R;
import com.example.shengboerwima.entity.Shop;
import com.example.shengboerwima.functionActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShopDescActivity extends AppCompatActivity {
    TextView titleTv1,titleTv2,descTv,floorTv;
    ImageView backIv,shopPicIv;
    Button mBtnAudio;
    URL url;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_desc);
        initView();

//        接受上一级页面传来的数据
        Intent intent = getIntent();
        Shop shop = (Shop) intent.getSerializableExtra("shop");
//        设置显示控件
        titleTv1.setText(shop.getShopName());
        titleTv2.setText(shop.getShopName());
        descTv.setText(shop.getShopIntro());
        floorTv.setText(shop.getShopFloor());
        url = shop.getShopPic();
        assert url != null;
        try {
            bitmap = BitmapFactory.decodeStream(url.openStream());
            assert bitmap != null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        shopPicIv.setImageBitmap(bitmap);
//        List<Shop> list = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            shopPicIv.setImageResource(shopPic[i]);
//        }
    }

    private void initView() {
        titleTv1 = findViewById(R.id.shopdesc_tv_title1);
        titleTv2 = findViewById(R.id.shopdesc_tv_title2);
        shopPicIv= findViewById(R.id.shopdesc_iv_shopPic);
        descTv = findViewById(R.id.shopdesc_tv_desc);
        floorTv = findViewById(R.id.shopdesc_tv_not);
        backIv = findViewById(R.id.shopdesc_iv_back);
        mBtnAudio = findViewById(R.id.btn_audio);
        backIv.setOnClickListener(v -> {
            finish();   //销毁当前的activity
        });
        mBtnAudio.setOnClickListener(v -> {
            Intent intent1;
            intent1 = new Intent(ShopDescActivity.this, functionActivity.class);
            startActivity(intent1);
        });
    }
}