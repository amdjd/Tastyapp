package com.example.a1.tastyapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a1.tastyapp.Request.InsertData;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    final static String TAG = "Login";

    EditText edit_user_id;
    EditText edit_password;

    Button siginBtn;
    Button singupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        siginBtn = (Button) findViewById(R.id.sig_in);
        singupBtn = (Button) findViewById(R.id.sig_up);
        siginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_user_id = (EditText) findViewById(R.id.get_id);
                edit_password = (EditText) findViewById(R.id.get_pass);
                JSONObject postDataParam = new JSONObject();
                if(edit_user_id.getText().toString().equals("") || edit_password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        postDataParam.put("user_id", edit_user_id.getText().toString());
                        postDataParam.put("password", edit_password.getText().toString());
                    } catch (JSONException e) {
                        Log.e(TAG, "JSONEXception");
                    }
                    if(isNetworkAvailable())
                         new InsertData(LoginActivity.this, "login").execute(postDataParam);
                    else
                        Toast.makeText(LoginActivity.this, "인터넷 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public String getUser_id(){
        String user_id= String.valueOf(edit_user_id.getText());

        return user_id;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
