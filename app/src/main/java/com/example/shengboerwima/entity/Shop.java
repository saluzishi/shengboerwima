package com.example.shengboerwima.entity;

import java.io.Serializable;
import java.net.URL;

public class Shop implements Serializable {
    private int shopId;
    private String shopName;
    private String shopFloor;
    private String shopIntro;
    private URL shopLogo;
    private URL shopPic;


    public Shop(){

    }

    public Shop(String shopName, String shopFloor, String shopIntro, URL shopLogo, URL shopPic){
        this.shopName = shopName;
        this.shopFloor = shopFloor;
        this.shopIntro = shopIntro;
        this.shopLogo = shopLogo;
        this.shopPic = shopPic;
    }

    public int getShopId() {
        return shopId;
    }

    public String getShopFloor() {
        return shopFloor;
    }

    public String getShopIntro() {
        return shopIntro;
    }

    public URL getShopLogo() {
        return shopLogo;
    }

    public String getShopName() {
        return shopName;
    }

    public URL getShopPic() {
        return shopPic;
    }

    public void setShopFloor(String shopFloor) {
        this.shopFloor = shopFloor;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void setShopIntro(String shopIntro) {
        this.shopIntro = shopIntro;
    }

    public void setShopLogo(URL shopLogo) {
        this.shopLogo = shopLogo;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopPic(URL shopPic) {
        this.shopPic = shopPic;
    }
}
