package com.example.t.roadsandhighway;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Random;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.SubscribeListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;


/**
 * Created by t on 5/15/17.
 */

public class NewsfeedOnMap extends AppCompatActivity implements MeteorCallback, OnMapReadyCallback {

    private Meteor mMeteor;
    private static String TAG = "NewsFeed";
    private SupportMapFragment mapFragment;
    private Button btnPress;



    ArrayList<StatusObject> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed_on_map);


        // create a new instance
        mMeteor = new Meteor(this, "ws://52.175.255.59/websocket", new InMemoryDatabase());

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the connection
        mMeteor.connect();

        btnPress = (Button) findViewById(R.id.btnNewsPress);


        btnPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

//
//        final CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(23.727358, 90.389717))      // Sets the center of the map to Mountain View
//                .zoom(7)                   // Sets the zoom
//                .bearing(0)                // Sets the orientation of the camera to east
//                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
//                .build();
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.727358, 90.389717), 7 ));
//                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//
//            }
//        });






    }

    private void fetchData() {
        Log.d(TAG, "connection " + String.valueOf(mMeteor.isConnected()));
        if (mMeteor.isConnected()) {
            final String[] params = {};

            String subscriptionId = mMeteor.subscribe("statuses", params, new SubscribeListener() {

                @Override
                public void onSuccess() {
                    Log.d(TAG, params.toString());


                }

                @Override
                public void onError(String error, String reason, String details) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                }
            });
        }


    }

    @Override
    public void onConnect(boolean signedInAutomatically) {
        Log.d("connection",Boolean.toString(signedInAutomatically));

        fetchData();

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {





        if (collectionName.equals("Statuses")) {
            try {
                JSONObject jsonObject = new JSONObject(newValuesJson);
                String address = jsonObject.getString("address");
                double lat = jsonObject.getDouble("latitude");
                double lng = jsonObject.getDouble("longitude");
                String level = jsonObject.getString("level");
                String averageSpeed = jsonObject.getString("averageSpeed");
                String trafficVolume = jsonObject.getString("trafficVolume");
                String note = jsonObject.getString("note");
                String createdAt = jsonObject.getString("createdAt");
                String contactNo = jsonObject.getString("contactNo");
                if (!String.valueOf(lat).equals(null) && !String.valueOf(lng).equals(null)) {
                    list.add(new StatusObject(address, lat, lng, level, averageSpeed, trafficVolume
                            , note, createdAt, contactNo));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        //Log.d(TAG,updatedValuesJson+removedValuesJson);
    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


            for (StatusObject statusObject : list) {
                LatLng latLng = new LatLng(statusObject.lat, statusObject.lng);
                googleMap.addMarker(new MarkerOptions().position(latLng)
                        .title(statusObject.level + " " + statusObject.trafficVolume + " "
                                + statusObject.averageSpeed));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        }


        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.727358, 90.389717), 14), 2000, null);


    }
}
