package com.example.a1.tastyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1.tastyapp.Adapter.SpacesItemDecoration;
import com.example.a1.tastyapp.Request.GetQueryResData;
import com.example.a1.tastyapp.Request.GetRestaurantData;
import com.example.a1.tastyapp.Request.QueryResData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final private String TAG = "MainActivity";
    final private int MY_PERMISSION_REQUEST_LOCATION = 100;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationListener mLocationListener;
    private boolean mRequestingLocationUpdates=true;
    private SpacesItemDecoration decoration;
    String title="#Place";
    String sort = "point";
    TextView bottomSheet_Text;
    SeekBar sb;
    TextView idText;

    LocationRequest locRequest = new LocationRequest().setInterval(10000)
            .setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    Toolbar toolbar;
    String user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        Intent intent = getIntent();
        user_id = intent.getExtras().getString("user_id");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.search);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(getApplicationContext(),
                        NavigateActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        idText = (TextView)header.findViewById(R.id.navUserId);
        idText.setText(user_id+" 님");

        new GetQueryResData(MainActivity.this).execute();

        bottomSheet_Text = (TextView)findViewById(R.id.bottomSheet_Text);
        Button bottomSheet_distanceSort = (Button)findViewById(R.id.bottomSheet_distanceSort);
        Button bottomSheet_pointSort = (Button)findViewById(R.id.bottomSheet_pointSort);

        bottomSheet_distanceSort.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort="distance";
                bottomSheet_Text.setText("거리순");
                QueryData(sb.getProgress());
            }
        });
        bottomSheet_pointSort.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort="point";
                bottomSheet_Text.setText("평점순");
                QueryData(sb.getProgress());
            }
        });


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(new MyConnectionCallBack())
                    .addOnConnectionFailedListener(new MyOnConnectionFailedListener())
                    .addApi(LocationServices.API)
                    .build();
        }

        final TextView tv = (TextView)findViewById(R.id.destence);
        sb  = (SeekBar) findViewById(R.id.distancSeekBar);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if(progress==0) {
                    tv.setText("100m");
                    QueryData(0.1);
                }
                if(progress==1){
                    tv.setText("300m");
                    QueryData(0.3);
                }
                if(progress==2){
                    tv.setText("500m");
                    QueryData(0.5);
                }
                if(progress==3){
                    tv.setText("1km");
                    QueryData(1);
                }
                if(progress==4){
                    tv.setText("3km");
                    QueryData(3);
                }
            }
        });

        new GetRestaurantData(MainActivity.this).execute();
    }

    public void QueryData(double distance) {
        startLocationUpdates();
        JSONObject postDataParam = new JSONObject();
        double latitude = 0.0;
        double longitude = 0.0;
        if (mCurrentLocation != null) {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
        }
        try {
            postDataParam.put("distance", distance);
            postDataParam.put("longitude", longitude);
            postDataParam.put("latitude", latitude);
            postDataParam.put("sort", sort);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //new GetRestaurantData(MainActivity.this).execute();
        new QueryResData(MainActivity.this).execute(postDataParam);
    }

    public void geoTitle() {
        startLocationUpdates();
        double latitude = 0.0;
        double longitude = 0.0;
        latitude = mCurrentLocation.getLatitude();
        longitude = mCurrentLocation.getLongitude();


        Geocoder gCoder = new Geocoder(this, Locale.getDefault());
        List<Address> addr = null;
        try {
            addr = gCoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address a = addr.get(0);
        //Toast.makeText(this,"lat"+latitude+ "lon"+longitude+":"+a.getAddressLine(0).toString(), Toast.LENGTH_SHORT).show();
        title = a.getAddressLine(0).toString();

    }
    @Override
    public void onResume() {
        super.onResume();
        setTitle(title);
    }
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            MainActivity.this.moveTaskToBack(true);
            MainActivity.this .finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.view, menu);
        getMenuInflater().inflate(R.menu.search, menu);
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


    //

    private class MyConnectionCallBack implements GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle bundle) {
            Log.i(TAG, "onConnected");
            if (isPermissionGranted())
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.i(TAG, "onConnectionSuspended");
        }
    }

    private class MyOnConnectionFailedListener implements GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.i(TAG, "onConnectionFailed");
        }
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart, connect request");
        super.onStart();
        mGoogleApiClient.connect();
    }

    // It is a good practice to remove location requests when the activity is in a paused or
    // stopped state. Doing so helps battery performance and is especially
    // recommended in applications that request frequent location updates.
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
            stopLocationUpdates();
    }

    // It is a good practice to remove location requests when the activity is in a paused or
    // stopped state. Doing so helps battery performance and is especially
    // recommended in applications that request frequent location updates.
    @Override
    protected void onStop() {
        Log.i(TAG, "onStop, disconnect request");
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLocation = location;

            }
        };
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                locRequest,
                mLocationListener);


    }


    private  void stopLocationUpdates() {
        if (mLocationListener != null && mGoogleApiClient != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
    }

    public Toolbar getToolbar(){
        return toolbar;
    }

    public String getUser_id(){
        return user_id;
    }

    public SpacesItemDecoration getDecoration(){
        return decoration;
    }
    public void setDecoration(SpacesItemDecoration decoration){
        this.decoration=decoration;
    }
    //퍼미션
    void requestPermission() {
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }
    private boolean isPermissionGranted() {
        String[] PERMISSIONS_STORAGE = {    // 요청할 권한 목록을 설정
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                    PERMISSIONS_STORAGE,        // 요청할 권한 목록을 설정한 String 배열
                    MY_PERMISSION_REQUEST_LOCATION    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
            );
            return false;
        } else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
                }
            }
        }
    }




}
