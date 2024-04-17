package com.example.shengboerwima.shop_list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shengboerwima.R;
import com.example.shengboerwima.entity.Shop;

import java.io.IOException;
import java.net.URL;
import java.util.List;
public class InfoListAdapter extends BaseAdapter{
    Context context;
    List<Shop> mDatas;
    URL url;
    String name;
    String floor;
    ViewHolder holder;
    Bitmap bitmap;
    // 定义一个LruCache来缓存从网络上获取的图片，需要把它设置成全局的变量
    private LruCache<String, BitmapDrawable> mImageCache;

    public InfoListAdapter(Context context, List<Shop> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

        // 获取app最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        // 实例化我们的LRU缓存
        mImageCache = new LruCache<String, BitmapDrawable>(cacheSize){
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
    }

    // 决定了ListView列表展示的行数
    @Override
    public int getCount() {
        return mDatas.size();
    }

    // 返回指定位置对应的数据
    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    // 返回指定位置所对应的id
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Object location_url = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_infolist_lv, null); // 将布局转换成view对象的方法
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 加载控件显示的内容
        // 获取集合指定位置的数据
        Shop shop = mDatas.get(position);
        name = shop.getShopName();
        floor = "楼层:" + shop.getShopFloor();
        url = shop.getShopLogo();
        holder.titleTv.setText(name);
        holder.floorTv.setText(floor);
        holder.iv.setTag(location_url);

        // 从缓存中加载图片
        BitmapDrawable drawable = getBitmapFromMemoryCache(location_url.toString());
        if(drawable != null){
            holder.iv.setImageDrawable(drawable);
        }else{
            // Android 4.0 之后不能在主线程中请求HTTP(url)请求
            new Thread(() -> {
                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                    if(bitmap != null){
                        addBitmapToMemoryCache(location_url.toString(),new BitmapDrawable(context.getResources(),bitmap));
                        holder.iv.post(()->{
                            if(holder.iv.getTag() != null && holder.iv.getTag().equals(location_url)){
                                holder.iv.setImageDrawable(getBitmapFromMemoryCache(location_url.toString()));
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return convertView;
    }
    /* 从内存缓存中获取图片 */
    private BitmapDrawable getBitmapFromMemoryCache(String imageUrl) {
        return mImageCache.get(imageUrl);
    }
    /* 把图片加入到内存缓存中 */
    private void addBitmapToMemoryCache(String imageUrl, BitmapDrawable drawable) {
        if (getBitmapFromMemoryCache(imageUrl) == null) {
            mImageCache.put(imageUrl, drawable);
        }
    }

    final static class ViewHolder{
        ImageView iv;
        TextView titleTv,floorTv;
        public ViewHolder(View view){
            iv = view.findViewById(R.id.item_info_iv);
            titleTv = view.findViewById(R.id.item_info_tv_title);
            floorTv = view.findViewById(R.id.item_info_tv_floor);
        }
    }
}