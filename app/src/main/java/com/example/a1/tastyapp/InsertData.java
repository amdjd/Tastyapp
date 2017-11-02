package com.example.a1.tastyapp;

import android.app.Activity;

import java.net.MalformedURLException;
import java.net.URL;


public class InsertData extends PostRequest {
    String path="";
    final static String serverURLStr="http://13.114.103.74:3000";

    public InsertData(Activity activity) {
        super(activity);
    }
    public InsertData(Activity activity, String path) {
        super(activity);
        this.path=path;
    }
    @Override
    protected void onPreExecute() {
        try {
            url = new URL( serverURLStr+ "/"+path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


}
