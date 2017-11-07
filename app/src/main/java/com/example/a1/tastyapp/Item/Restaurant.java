package com.example.a1.tastyapp.Item;

import android.graphics.Bitmap;

/**
 * Created by 1 on 2017-10-26.
 */

public class Restaurant {
    String id;
    String name;
    String point;
    String tel;
    Bitmap picture;

    public Restaurant(String id, String name,String tel, String point, Bitmap picture) {
        this.id=id;
        this.name=name;
        this.tel=tel;
        this.point=point;
        this.picture=picture;
    }
    public String getId(){ return id;}
    public String getName(){ return name;}
    public String getPoint(){ return point;}
    public Bitmap getPicture(){ return picture;}

}