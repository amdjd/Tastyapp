package com.example.a1.tastyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

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
                EditText edit_name = (EditText) findViewById(R.id.put_name);
                JSONObject postDataParam = new JSONObject();
                try {
                    postDataParam.put("user_id", edit_user_id.getText().toString());
                    postDataParam.put("email", edit_email.getText().toString());
                    postDataParam.put("password", edit_password.getText().toString());
                    postDataParam.put("name", edit_name.getText().toString());
                } catch (JSONException e) {
                    Log.e(TAG, "JSONEXception");
                }
                new InsertData(RegisterActivity.this,"insert-user").execute(postDataParam);
            }
        });
    }
}
