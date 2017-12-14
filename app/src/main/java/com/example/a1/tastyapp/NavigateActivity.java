package com.example.a1.tastyapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.a1.tastyapp.Adapter.SpacesItemDecoration;
import com.example.a1.tastyapp.Item.Restaurant;
import com.example.a1.tastyapp.Request.QueryResData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NavigateActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    final private String TAG = "NavigateActivity";
    final private int MY_PERMISSION_REQUEST_LOCATION = 100;
    // UI Widgets.
    private Button mStartUpdatesButton;
    private Button distanceButton;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap = null;
    private Location mCurrentLocation;
    private LocationListener mLocationListener;
    private MarkerOptions makerOptions;

    private SpacesItemDecoration decoration;

    private String user_id = "";
    private double distance = 3;


    LatLng currentPosition;
    List<Marker> previous_marker = null;

    LocationRequest locRequest = new LocationRequest().setInterval(10000)
            .setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_navigate);




        Intent intent = getIntent();
        user_id = intent.getExtras().getString("user_id");


        distanceButton = (Button) findViewById(R.id.navi_distancebtn);
        mStartUpdatesButton = (Button) findViewById(R.id.start_updates_button);

        distanceButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSelectOption();
            }
        });

        previous_marker = new ArrayList<Marker>();


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(new MyConnectionCallBack())
                    .addOnConnectionFailedListener(new MyOnConnectionFailedListener())
                    .addApi(LocationServices.API)
                    .build();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //new GetRestaurantData(this,"LinearLayoutManager").execute();
    }

    public void startUpdatesButtonHandler(View view) {
        if (mGoogleApiClient.isConnected() && isPermissionGranted()) {
            startLocationUpdates();
            QueryData(distance);
        }
    }

    public void QueryData(double distance) {
        startLocationUpdates();
        JSONObject postDataParam = new JSONObject();
        double latitude = 0.0;
        double longitude = 0.0;
/*        if (mCurrentLocation != null) {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
        }*/
        if (mCurrentLocation != null) {
            latitude = mGoogleMap.getCameraPosition().target.latitude;
            longitude = mGoogleMap.getCameraPosition().target.longitude;
        }
        try {
            postDataParam.put("distance", distance);
            postDataParam.put("longitude", longitude);
            postDataParam.put("latitude", latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //new GetRestaurantData(MainActivity.this).execute();
        //new QueryResData(NavigateActivity.this, "query-res").execute(postDataParam);
        new QueryResData(NavigateActivity.this, "LinearLayoutManager").execute(postDataParam);
    }

    public void setRestaurantMarker(ArrayList<Restaurant> restaurant) {
        mGoogleMap.clear();
        for (int i = 0; restaurant.size() > i; i++) {
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions
                    .position(new LatLng(restaurant.get(i).getLongitude(), restaurant.get(i).getLatiude()))
                    .snippet(String.valueOf(i + 1))
                    .title(restaurant.get(i).getName());

            mGoogleMap.addMarker(makerOptions);
        }
    }

    private void startLocationUpdates() {
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());
                mCurrentLocation = location;
                //updateUI();
            }
        };
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                locRequest,
                mLocationListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

    }

    //
    public void onMapReady(GoogleMap gMap) {

        mGoogleMap = gMap;

        LatLng seoul;
        // Add a marker in Sydney and move the camera
        if (mCurrentLocation != null) {
            seoul = currentPosition;
        } else
            seoul = new LatLng(37.5642135, 127.0016985);
        // move the camera
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10));

        mGoogleMap.setOnMarkerClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick()
            {
                QueryData(3);
                return false;
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.masonry);
        mRecyclerView.scrollToPosition(Integer.parseInt(marker.getSnippet())-1);
        return false;
    }

    private class MyConnectionCallBack implements GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle bundle) {
            Log.i(TAG,"onConnected");
            if (isPermissionGranted())
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //updateUI();

        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.i(TAG,"onConnectionSuspended");
        }
    }


    private class MyOnConnectionFailedListener implements GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.i(TAG,"onConnectionFailed");
        }
    }

/*
    private void updateUI() {
        double latitude= 0.0;
        double longitude = 0.0;
        if (mCurrentLocation != null) {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
        }
    }
*/

    private void DialogSelectOption() {
        final String items[] = { "100m","300m", "500m", "1km", "3km" };
        AlertDialog.Builder ab = new AlertDialog.Builder(NavigateActivity.this);
        ab.setTitle("반경");
        ab.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(whichButton==0) {
                            distance = 0.1;
                            QueryData(distance);
                        }
                        if(whichButton==1) {
                            distance = 0.3;
                            QueryData(distance);
                        }
                        if(whichButton==2) {
                            distance = 0.5;
                            QueryData(distance);
                        }
                        if(whichButton==3) {
                            distance = 1;
                            QueryData(distance);
                        }
                        if(whichButton==4) {
                            distance = 3;
                            QueryData(distance);
                        }
                    }
                }).setPositiveButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        ab.show();
    }


    @Override
    protected void onStart() {
        Log.i(TAG,"onStart, connect request");
        super.onStart();
        mGoogleApiClient.connect();
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
    //퍼미션 관련
    private boolean isPermissionGranted() {
        String[] PERMISSIONS_STORAGE = {    // 요청할 권한 목록을 설정
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (ActivityCompat.checkSelfPermission(NavigateActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(NavigateActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    NavigateActivity.this,			// MainActivity 액티비티의 객체 인스턴스를 나타냄
                    PERMISSIONS_STORAGE,        // 요청할 권한 목록을 설정한 String 배열
                    MY_PERMISSION_REQUEST_LOCATION    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
            );
            return false;
        } else
            return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    //updateUI();
                } else {
                    Toast.makeText(this,"Permission required",Toast.LENGTH_SHORT);
                }
            }
        }
    }

}