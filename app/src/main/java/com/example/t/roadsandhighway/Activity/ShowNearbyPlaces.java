package com.example.t.roadsandhighway.Activity;

import android.content.Intent;
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
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import java.util.ArrayList;

public class ShowNearbyPlaces extends AppCompatActivity implements OnMapReadyCallback {


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
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        for (LatLng latLng : latLngs) {
            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        }
    }


}
