package com.ayranisthenewraki.heredot.herdot;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.ayranisthenewraki.heredot.herdot.model.TimeLocationCouple;
import com.ayranisthenewraki.heredot.herdot.model.TimeLocationListWrapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsViewAllActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_view_all);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        Bundle bundle = getIntent().getExtras();
        TimeLocationListWrapper tlcWrapper = (TimeLocationListWrapper)bundle.getSerializable("timeLocationList");
        List<TimeLocationCouple> tlcList = tlcWrapper.getTlcList();

        for(int i=0; i<tlcList.size(); i++){
            try{
                TimeLocationCouple tlc = tlcList.get(i);
                boolean couldAddMarker = false;
                if(tlc.getShape().getLat()!=null && tlc.getShape().getLng()!=null){
                    LatLng current =  new LatLng(tlc.getShape().getLat(), tlc.getShape().getLng());
                    mMap.addMarker(new MarkerOptions().position(current).title(tlc.getName() + ", " + tlc.getTime() + " " + tlc.getTimeType()));
                    couldAddMarker = true;

                    if(i==0){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
                    }
                }


                if(tlc.getShape().getType() == 0){
                    // Adding the circle to the GoogleMap
                    LatLng current =  new LatLng(tlc.getShape().getShape().getCenter().getLat(), tlc.getShape().getShape().getCenter().getLng());
                    if(!couldAddMarker){
                        mMap.addMarker(new MarkerOptions().position(current).title(tlc.getName() + ", " + tlc.getTime() + " " + tlc.getTimeType()));
                        couldAddMarker = true;

                        if(i==0){
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
                        }
                    }

                    mMap.addCircle(new CircleOptions().center(current)
                            .radius(tlc.getShape().getShape().getRadius())
                            .strokeColor(Color.BLACK)
                            .fillColor(0x30ff0000)
                            .strokeWidth(2));

                }
            }catch(Exception e){
                // won't add a marker but won't crash
            }

        }
    }
}
