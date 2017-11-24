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
    Button buttonWant;
    Button buttonReview;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_detail);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");

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


        imageView_res = (ImageView)findViewById(R.id.imageView_res);

        textresName = (TextView)findViewById(R.id.textresName);
        textresPoint = (TextView)findViewById(R.id.textresPoint);
        textresAdress = (TextView)findViewById(R.id.textresAdress);
        textresTEL = (TextView)findViewById(R.id.textresTEL);
        textBusinesshours = (TextView)findViewById(R.id.textBusinesshours);

        buttonWant = (Button)findViewById(R.id.buttonWant);
        buttonReview = (Button)findViewById(R.id.buttonReview);
        buttonReview.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(ResDetailActivity.this, ReviewActivity.class);
                        i.putExtra("name", name);
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

    public ArrayList<TextView> getTextView(){
        ArrayList<TextView> output = new ArrayList();
        output.add(textresName);
        output.add(textresPoint);
        output.add(textresAdress);
        output.add(textresTEL);
        output.add(textBusinesshours);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
