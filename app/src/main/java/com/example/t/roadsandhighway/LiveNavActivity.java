package com.example.t.roadsandhighway;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.t.roadsandhighway.Activity.GpsService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by t on 5/28/17.
 */

public class LiveNavActivity extends AppCompatActivity implements OnMapReadyCallback {
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_nav);

        button= (Button) findViewById(R.id.stopService);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GpsService.class);
                        stopService(i);
            }
        });
        Intent i = new Intent(getApplicationContext(), GpsService.class);
        startService(i);

//

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.lmap);
        mapFragment.getMapAsync(this);
        }





    @Override
    public void onMapReady(GoogleMap googleMap) {



//        Polyline line = googleMap.addPolyline(new PolylineOptions()
//                .addAll(latLngs)
//                .width(12)
//                .color(Color.parseColor("#05b1fb"))//Google maps blue color
//                .geodesic(true)
//        );


    }
}
