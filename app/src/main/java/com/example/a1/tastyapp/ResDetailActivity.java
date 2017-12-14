package com.example.a1.tastyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1.tastyapp.Request.PostReviewRes;
import com.example.a1.tastyapp.Request.QueryOneResData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView imageView_res;
    TextView textresName ;
    TextView textresPoint ;
    TextView textresAdress ;
    TextView textresTEL ;
    TextView textBusinesshours;
    TextView textresType;
    TextView idText;
    Button buttonWant;
    Button buttonReview;

    String name;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_detail);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        user_id = intent.getExtras().getString("user_id");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        idText = (TextView)header.findViewById(R.id.navUserId);
        idText.setText(user_id+" 님");
        imageView_res = (ImageView)findViewById(R.id.imageView_res);

        textresName = (TextView)findViewById(R.id.textresName);
        textresPoint = (TextView)findViewById(R.id.textresPoint);
        textresAdress = (TextView)findViewById(R.id.textresAdress);
        textresTEL = (TextView)findViewById(R.id.textresTEL);
        textBusinesshours = (TextView)findViewById(R.id.textBusinesshours);
        textresType = (TextView)findViewById(R.id.res_type);
        buttonWant = (Button)findViewById(R.id.buttonWant);
        buttonReview = (Button)findViewById(R.id.buttonReview);
        buttonReview.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(ResDetailActivity.this, ReviewActivity.class);
                        i.putExtra("name", name);
                        i.putExtra("user_id", user_id);
                        ResDetailActivity.this.startActivity(i);
                    }
                }
        );
        JSONObject postDataParam = new JSONObject();
        try {
            postDataParam.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new QueryOneResData(ResDetailActivity.this).execute(postDataParam);
        new PostReviewRes(ResDetailActivity.this).execute(postDataParam);
    }

    public void textresTELonClick(View v)
    {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+textresTEL.getText()));
        startActivity(i);
    }

    public ImageView getImageView(){
        ImageView output = null;
        output = imageView_res;
        return output;
    }

    public String getUser_id(){
        return user_id;
    }

    public ArrayList<TextView> getTextView(){
        ArrayList<TextView> output = new ArrayList();
        output.add(textresName);
        output.add(textresPoint);
        output.add(textresAdress);
        output.add(textresTEL);
        output.add(textBusinesshours);
        output.add(textresType);
        return output;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.res_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            /*Intent intent = new Intent(getApplicationContext(),
                    NavigateActivity.class);
            startActivity(intent);*/
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
