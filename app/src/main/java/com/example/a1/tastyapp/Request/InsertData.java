package com.example.a1.tastyapp.Request;

import android.app.Activity;

import java.net.MalformedURLException;
import java.net.URL;


public class InsertData extends PostRequest {
    final static String serverURLStr="http://13.114.103.74:3000";
    String path="";

    public InsertData(Activity activity, String path) {
        super(activity);
        this.path=path;
    }
    @Override
    protected void onPreExecute() {
            try {
                url = new URL(serverURLStr + "/" + path);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

