package com.example.a1.tastyapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    final static String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerBtn = (Button) findViewById(R.id.regist_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_user_id = (EditText) findViewById(R.id.put_id);
                EditText edit_email = (EditText) findViewById(R.id.put_email);
                EditText edit_password = (EditText) findViewById(R.id.put_password);
                EditText edit_password2 = (EditText) findViewById(R.id.put_password2);
                EditText edit_name = (EditText) findViewById(R.id.put_name);
                JSONObject postDataParam = new JSONObject();

                if(edit_user_id.getText().toString().equals("") || edit_email.getText().toString().equals("") || edit_password.getText().toString().equals("") || edit_name.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this,"입력사항이 누락되었습니다..",Toast.LENGTH_SHORT).show();
                }

                else if(!Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,12}$", edit_user_id.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"올바른 ID 형식이 아닙니다\n4~12자리로 Id를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(edit_email.getText().toString()).matches())
                {
                    Toast.makeText(RegisterActivity.this,"올바른 이메일 형식이 아닙니다",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!Pattern.matches("^[A-Za-z0-9]*.{6,12}$", edit_password.getText().toString()))
                {
                    Toast.makeText(RegisterActivity.this,"암호를 6~12자리로 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!edit_password.getText().toString().equals(edit_password2.getText().toString()))
                {
                    Toast.makeText(RegisterActivity.this,"입력하신 비밀번호와 비밀번호 확인이 일치하지 않습니다",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!Pattern.matches("^[가-힣-A-Za-z0-9]*.{1,10}$", edit_name.getText().toString()))
                {
                    Toast.makeText(RegisterActivity.this,"이름 형식을 지켜주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    try {
                        postDataParam.put("user_id", edit_user_id.getText().toString());
                        postDataParam.put("email", edit_email.getText().toString());
                        postDataParam.put("password", edit_password.getText().toString());
                        postDataParam.put("name", edit_name.getText().toString());

                    } catch (JSONException e) {
                        Log.e(TAG, "JSONEXception");
                    }
                    if(isNetworkAvailable())
                        new InsertData(RegisterActivity.this, "insert-user").execute(postDataParam);
                    else
                        Toast.makeText(RegisterActivity.this, "인터넷 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
