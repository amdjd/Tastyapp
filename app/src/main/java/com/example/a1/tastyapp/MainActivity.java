package com.example.a1.tastyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button siginBtn = (Button) findViewById(R.id.sig_in);
        Button singupBtn = (Button) findViewById(R.id.sig_up);
        siginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_user_id = (EditText) findViewById(R.id.get_id);
                EditText edit_password = (EditText) findViewById(R.id.get_pass);
                JSONObject postDataParam = new JSONObject();
                try {
                    postDataParam.put("user_id", edit_user_id.getText().toString());
                    postDataParam.put("password", edit_password.getText().toString());
                } catch (JSONException e) {
                    Log.e(TAG, "JSONEXception");
                }
               new InsertData(MainActivity.this, "login").execute(postDataParam);
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
}
