package com.example.a1.tastyapp.Request;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.a1.tastyapp.MainActivity;

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


    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Succes")){
            Intent intent = new Intent(activity,
                    MainActivity.class);
            activity.startActivity(intent);
        }
        if(result.equals("Server Error : 401")){
            Toast.makeText(activity,"아이디와 비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
        }
        if(result.equals("exist id")){
            Toast.makeText(activity,"이미 있는 아이디 혹은 이메일 입니다.",Toast.LENGTH_SHORT).show();
        }


    }
    }

