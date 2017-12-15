package com.example.a1.tastyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.a1.tastyapp.Request.PostReviewRes;

import org.json.JSONException;
import org.json.JSONObject;

public class MyReviewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        Intent intent = getIntent();
        user_id = intent.getExtras().getString("user_id");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        JSONObject postDataParam = new JSONObject();
        try {
            postDataParam.put("user_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new PostReviewRes(MyReviewActivity.this).execute(postDataParam);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(getApplicationContext(),
                    NavigateActivity.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        } else if (id == R.id.nav_res) {
            /*Intent intent = new Intent(getApplicationContext(),
                    NavigateActivity.class);
            startActivity(intent);*/
        } else if (id == R.id.nav_review) {
            Intent intent = new Intent(getApplicationContext(),
                    MyReviewActivity.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        }else if (id == R.id.nav_logout) {
            Intent intent = new Intent(getApplicationContext(),
                    LoginActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
