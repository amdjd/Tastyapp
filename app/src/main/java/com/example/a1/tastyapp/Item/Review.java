package com.example.a1.tastyapp.Item;

import java.net.URL;

/**
 * Created by 1 on 2017-11-29.
 */

public class Review {
    String id;
    String user_id;
    String resname;
    double point;
    String memo;
    URL picture;
    String date;

    public Review(
            String id,
            String user_id,
            String resname,
            double point,
            String memo,
            URL picture,
            String date){
        this.user_id= user_id;
        this.resname= resname;
        this.point= point;
        this.memo= memo;
        this.picture= picture;
        this.date= date;
    }
    public String getId(){
        return id;
    }

    public String getUser_id(){
        return user_id;
    }
    public String getResname(){
        return resname;
    }
    public double getPoint(){
        return  point;
    }
    public String getMemo(){
        return memo;
    }
    public URL getPicture(){
        return picture;
    }
    public String getDate(){
        return date;
    }
}
