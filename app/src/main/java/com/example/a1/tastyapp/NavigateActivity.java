package com.example.a1.tastyapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView mPrecisionTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap = null;
    private Location mCurrentLocation;
    private LocationListener mLocationListener;
    private MarkerOptions makerOptions;

    LatLng currentPosition;
    List<Marker> previous_marker = null;

    LocationRequest locRequest = new LocationRequest().setInterval(10000)
            .setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        mStartUpdatesButton = (Button) findViewById(R.id.start_updates_button);

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
            QueryData(0.5);
        }
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //new GetRestaurantData(MainActivity.this).execute();
        //new QueryResData(NavigateActivity.this, "query-res").execute(postDataParam);
        new QueryResData(NavigateActivity.this, "LinearLayoutManager").execute(postDataParam);
    }

    public void setRestaurantMarker(ArrayList<Restaurant> restaurant) {
        for (int i = 0; restaurant.size() > i; i++) {
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions
                    .position(new LatLng(restaurant.get(i).getLongitude(), restaurant.get(i).getLatiude()))
                    .snippet(String.valueOf(i+1))
                    .title(restaurant.get(i).getName());

            mGoogleMap.addMarker(makerOptions);
        }
    }
    private void startLocationUpdates() {
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentPosition
                        = new LatLng( location.getLatitude(), location.getLongitude());
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
        mGoogleMap.setMyLocationEnabled(true);
    }

    //
    public void onMapReady(GoogleMap gMap) {

        mGoogleMap = gMap;

        LatLng sydney;
        // Add a marker in Sydney and move the camera
        if (mCurrentLocation != null) {
            sydney = currentPosition;
        }
        else
            sydney = new LatLng(37.5391507, 127.0856099);
        // move the camera
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

        mGoogleMap.setOnMarkerClickListener(this);
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

    @Override
    protected void onStart() {
        Log.i(TAG,"onStart, connect request");
        super.onStart();
        mGoogleApiClient.connect();
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