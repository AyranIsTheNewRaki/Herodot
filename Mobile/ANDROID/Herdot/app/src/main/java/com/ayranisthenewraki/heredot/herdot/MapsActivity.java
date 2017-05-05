package com.ayranisthenewraki.heredot.herdot;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    boolean putMarkerEnabled = false;
    boolean cirlceSelected = false;
    boolean cirlceAdded = false;
    LatLng selectedPoint;
    Integer radius = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        Spinner timeResDropdown = (Spinner)findViewById(R.id.timeResSpinner);
        String[] timeItems = new String[]{"Century", "Decade", "Year", "Date"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timeItems);
        timeResDropdown.setAdapter(timeAdapter);

        final Button markerToggle = (Button) findViewById(R.id.pinDropper);

        markerToggle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(putMarkerEnabled){
                    putMarkerEnabled = false;
                    int image_resid = getApplicationContext().getResources().getIdentifier("pin", "drawable", getApplicationContext().getPackageName());
                    markerToggle.setBackgroundResource(image_resid);
                }
                else{
                    putMarkerEnabled = true;
                    int image_resid = getApplicationContext().getResources().getIdentifier("pin_selected", "drawable", getApplicationContext().getPackageName());
                    markerToggle.setBackgroundResource(image_resid);
                }

            }
        });

    }

    private void drawCircle(){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(selectedPoint);

        // Radius of the circle
        circleOptions.radius(radius);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        // Add a marker in Sydney and move the camera
        selectedPoint = new LatLng(41.085, 29.046666666666667);
        mMap.addMarker(new MarkerOptions().position(selectedPoint).title("Selection"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedPoint));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedPoint, 15));

        final Button circleTool = (Button) findViewById(R.id.circleTool);
        final Button increaseRadius = (Button) findViewById(R.id.increaseRadius);
        final Button decreaseRadius = (Button) findViewById(R.id.decreaseRadius);

        circleTool.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(cirlceSelected){
                    cirlceSelected = false;
                    int image_resid = getApplicationContext().getResources().getIdentifier("circle", "drawable", getApplicationContext().getPackageName());
                    circleTool.setBackgroundResource(image_resid);
                    increaseRadius.setVisibility(View.INVISIBLE);
                    decreaseRadius.setVisibility(View.INVISIBLE);
                }
                else{
                    cirlceSelected = true;
                    int image_resid = getApplicationContext().getResources().getIdentifier("circle_selected", "drawable", getApplicationContext().getPackageName());
                    circleTool.setBackgroundResource(image_resid);
                    if(!cirlceAdded){
                        radius = 50;
                        drawCircle();
                        cirlceAdded = true;
                    }
                    increaseRadius.setVisibility(View.VISIBLE);
                    decreaseRadius.setVisibility(View.VISIBLE);
                }
            }
        });


        increaseRadius.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(selectedPoint));
                radius += 10;
                drawCircle();
            }
        });

        decreaseRadius.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(selectedPoint));
                if(radius>10){
                    radius -= 10;
                }
                drawCircle();
            }
        });



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if(putMarkerEnabled){
                    mMap.clear();
                    selectedPoint = point;
                    cirlceAdded = false;
                    mMap.addMarker(new MarkerOptions().position(point));
                }
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
