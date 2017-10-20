package com.example.t.roadsandhighway;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.t.roadsandhighway.Activity.GpsService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by t on 5/28/17.
 */

public class LiveNavActivity extends AppCompatActivity implements OnMapReadyCallback {
    Button button;
    boolean toggle=true;
    LatLng start;
    SupportMapFragment mapFragment;
    LocationListener locationListener;
    LocationManager locationManager;
    List<LatLng> pathList;
    private static String TAG="gps";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_nav);

        button= (Button) findViewById(R.id.stopService);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        pathList=new ArrayList<>();

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("gps",location.toString());
                pathList.add(new LatLng(location.getLatitude(),location.getLongitude()));
                mapFragment.getMapAsync(LiveNavActivity.this);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };







        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2000); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        button.startAnimation(anim);

//        Intent i = new Intent(getApplicationContext(), GpsService.class);
//        startService(i);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"inside");
//                Intent i = new Intent(getApplicationContext(), GpsService.class);
//                        stopService(i);
                LiveNavActivity.super.onBackPressed();
            }
        });



        SingleShotLocationProvider.requestSingleUpdate(getApplicationContext(), new SingleShotLocationProvider.LocationCallback() {
            @Override
            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                start=new LatLng(location.getLat(),location.getLang());
                mapFragment.getMapAsync(LiveNavActivity.this);


            }
        });

//

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.lmap);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        Log.d("gps","ache");

        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 10:
                Log.d("gps","ha ache");

                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


//        for (StatusObject statusObject : statusList) {
//            LatLng latLng = new LatLng(statusObject.lat, statusObject.lng);
//            googleMap.addMarker(new MarkerOptions().position(latLng)
//                    .title(statusObject.level + " "));
////            + statusObject.trafficVolume + " "
////                    + statusObject.averageSpeed
//            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        }




        if(pathList.size()>0){
        Polyline line = googleMap.addPolyline(new PolylineOptions()
                .addAll(pathList)
                .width(12)
                .color(Color.parseColor("#05b1fb"))//Google maps blue color
                .geodesic(true)
        );}
        googleMap.addMarker(new MarkerOptions().position(start).title("Starting from"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 15), 2000, null);


    }
}
