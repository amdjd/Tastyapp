package com.example.a1.tastyapp.Item;

import java.net.URL;

/**
 * Created by 1 on 2017-10-26.
 */

public class Restaurant {
    String id;
    double point;
    String name;
    URL picture;
    String tel;
    String adress;
    double latiude;
    double longitude;
    String businesshours;
    String menu;

    public Restaurant(String id,
                      double point,
                      String name,
                      URL picture,
                      String tel,
                      String adress,
                      double latiude,
                      double longitude,
                      String businesshours,
                      String menu)

    {
        this.id = id;
        this.point = point;
        this.name = name;
        this.picture = picture;
        this.tel = tel;
        this.adress = adress;
        this.latiude = latiude;
        this.longitude = longitude;
        this. businesshours = businesshours;
        this. menu = menu;
    }



    public String getId(){ return id;}
    public String getName(){ return name;}
    public double getPoint(){ return point;}
    public URL getPicture(){ return picture;}
    public String getTel(){ return tel;}
    public String getAdress(){ return adress;}
    public double getLatiude(){ return latiude;}
    public double getLongitude(){ return longitude;}
    public String getBusinesshours(){ return businesshours;}
    public String getMenu(){ return menu;}
}