package com.example.shengboerwima.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * function： 数据库工具类，连接数据库使用
 */

public class JDBCUtils {
    private static final String TAG = "mysql-party-JDBCUtils";

    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动

    private static String user = "saluzishi";// 用户名

    private static String password = "baaa959730c60e60";// 密码

    public static Connection getConn(){
        Connection connection = null;
        try{
            Class.forName(driver);// 动态加载类
            String ip = "mysql.sqlpub.com";// 数据库ip地址
//            String ip = "10.0.2.2";// 数据库ip地址
                        // 尝试建立到给定数据库URL的连接
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/saluzishi?useSSL=false" ,
                    user, password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }
}