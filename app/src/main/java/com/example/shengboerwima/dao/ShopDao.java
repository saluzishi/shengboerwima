package com.example.shengboerwima.dao;

import android.util.Log;

import com.example.shengboerwima.entity.Shop;
import com.example.shengboerwima.utils.JDBCUtils;

import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShopDao implements Serializable {
    private static final String TAG = "mysql-party-ShopDao";

    /**
     * function: 增加店铺信息
     */
    public boolean addShop(Shop shop){
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        try {
            String sql = "insert into shop_basic (shop_name,shop_floor,shop_intro,shop_logo,shop_pic) values (?,?,?,?,?)";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){

                    // 注册：将数据插入数据库
                    ps.setString(1,shop.getShopName()); // 商店名称
                    ps.setString(2,shop.getShopFloor()); // 所在楼层
                    ps.setString(3, shop.getShopIntro()); // 获取商店信息
                    ps.setURL(4,shop.getShopLogo()); // Logo地址
                    ps.setURL(5,shop.getShopPic()); // 全景图地址

                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    if(rs>0)
                        return true;
                    else
                        return false;
                }else {
                    return  false;
                }
            }else {
                return  false;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "异常插入：" + e.getMessage());
            return false;
        }
    }

    /**
     * function: 查找商家列表
     */
    public static List<Shop> listShopItem() {
        List<Shop> list = new ArrayList<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();
        Shop shop;
        try {
            String sql = "select shop_name,shop_floor,shop_intro,shop_logo,shop_pic from shop_basic";
            if (connection != null){ // connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ResultSet rs = ps.executeQuery();
                    if(rs != null)
                    {
                        while (rs.next()) {
                            //注意：下标是从1开始
                            String shopName = rs.getString(1);
                            String shopFloor = rs.getString(2);
                            String shopIntro = rs.getString(3);
                            URL shopLogo = rs.getURL(4);
                            URL shopPic = rs.getURL(5);
                            shop = new Shop(shopName, shopFloor, shopIntro, shopLogo, shopPic);
                            list.add(shop);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "异常findUser：" + e.getMessage());
            return null;
        }
        return list;
    }
}
