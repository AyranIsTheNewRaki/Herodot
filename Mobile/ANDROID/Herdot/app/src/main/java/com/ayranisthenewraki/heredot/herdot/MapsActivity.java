package com.ayranisthenewraki.heredot.herdot;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ayranisthenewraki.heredot.herdot.model.Center;
import com.ayranisthenewraki.heredot.herdot.model.ShapeDetail;
import com.ayranisthenewraki.heredot.herdot.model.ShapeIdentifier;
import com.ayranisthenewraki.heredot.herdot.model.TimeLocationCouple;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
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
    Double radius = 50.0;

    String timeLocationName = "";
    String timeText = "";
    String timeResolution = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

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

        final EditText nameInput = (EditText) findViewById(R.id.dateTimeName);
        final EditText timeInput = (EditText) findViewById(R.id.timeString);
        final Spinner timeResDropdown = (Spinner)findViewById(R.id.timeResSpinner);

        String[] timeItems = new String[]{"Century", "Decade", "Year", "Date"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timeItems);
        timeResDropdown.setAdapter(timeAdapter);

        timeResDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(timeResDropdown.getSelectedItemPosition()==0)
                    timeInput.setHint("19th");
                else if(timeResDropdown.getSelectedItemPosition()==1)
                    timeInput.setHint("1970s");
                else if(timeResDropdown.getSelectedItemPosition()==2)
                    timeInput.setHint("2015");
                else if(timeResDropdown.getSelectedItemPosition()==3)
                    timeInput.setHint("27/03/2016");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        final Button addTimeLocationButton = (Button) findViewById(R.id.addTimeLocationCouple);

        addTimeLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                timeLocationName = nameInput.getText().toString();
                timeText = timeInput.getText().toString();
                timeResolution = timeResDropdown.getSelectedItem().toString();
                if(timeLocationName.length()==0 || timeText.length()==0){
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(MapsActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please fill all fields to continue");
                    alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    TimeLocationCouple tlc = new TimeLocationCouple();
                    tlc.setName(timeLocationName);
                    tlc.setTime(timeText);
                    tlc.setTimeType(timeResolution);
                    ShapeIdentifier shapeId = new ShapeIdentifier();
                    shapeId.setType(0);
                    ShapeDetail shape = new ShapeDetail();
                    shape.setRadius(radius);
                    Center center = new Center();
                    center.setLat(selectedPoint.latitude);
                    center.setLng(selectedPoint.longitude);
                    shape.setCenter(center);
                    shapeId.setShape(shape);
                    tlc.setShape(shapeId);

                    goBackToAddItemView(view, tlc);
                }

            }
        });

    }

    public void goBackToAddItemView(View view, TimeLocationCouple tlc) {
        Intent intent = new Intent();
        intent.putExtra("userToken", intent.getStringExtra("userToken"));
        Bundle bundle = new Bundle();
        bundle.putSerializable("timeLocation", tlc);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
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
                        radius = 50.0;
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

        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.clear();
                selectedPoint = place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedPoint));
                mMap.addMarker(new MarkerOptions().position(selectedPoint));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                System.out.println("An error occurred: " + status);
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
