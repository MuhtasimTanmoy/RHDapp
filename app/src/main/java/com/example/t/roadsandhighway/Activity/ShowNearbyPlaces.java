package com.example.t.roadsandhighway.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.t.roadsandhighway.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import java.util.ArrayList;

public class    ShowNearbyPlaces extends AppCompatActivity implements OnMapReadyCallback {


    ArrayList<LatLng> latLngs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nearby_places);

        Intent i = getIntent();
        latLngs = (ArrayList<LatLng>) i.getSerializableExtra("locList");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.d("ShowMarker", latLngs.size()+"");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        boolean b=true;
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        for (LatLng latLng : latLngs) {
            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .title("found"));
           // googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            if(b==true){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15), 2000, null);
            b=false;}


        }

//        Polyline line = googleMap.addPolyline(new PolylineOptions()
//                .addAll(latLngs)
//                .width(12)
//                .color(Color.parseColor("#05b1fb"))//Google maps blue color
//                .geodesic(true)
//        );
//
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.727358, 90.389717),15), 2000, null);


        /*
           for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(2)
                .color(Color.BLUE).geodesic(true));
            }
           */
    }


}
