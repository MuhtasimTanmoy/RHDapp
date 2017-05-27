package com.example.t.roadsandhighway.Activity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.t.roadsandhighway.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;
import im.delight.android.ddp.db.Document;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

import static android.R.attr.button;
import static android.R.attr.stateListAnimator;
import static android.R.attr.statusBarColor;

/**
 * Created by t on 5/15/17.
 */

public class NewsfeedOnMap extends AppCompatActivity implements MeteorCallback,OnMapReadyCallback {

    private Meteor mMeteor;
    private static String TAG = "NewsFeed";
    private SupportMapFragment mapFragment;
    private Button btnPress;

    private class statusObject{
        String address;
        double lat;
        double lng;
        String level;
        String averageSpeed;
        String trafficVolume;
        String note;
        String createdAt;
        String contactNo;

        public statusObject(String address, double lat, double lng,
                            String level, String averageSpeed, String trafficVolume,
                            String note, String createdAt, String contactNo) {
            this.address = address;
            this.lat = lat;
            this.lng = lng;
            this.level = level;
            this.averageSpeed = averageSpeed;
            this.trafficVolume = trafficVolume;
            this.note = note;
            this.createdAt = createdAt;
            this.contactNo = contactNo;
        }
    }
    ArrayList<statusObject> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed_on_map);



        // create a new instance
        mMeteor = new Meteor(this, "ws://52.175.255.59/websocket",new InMemoryDatabase());

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the connection
        mMeteor.connect();

        btnPress=(Button) findViewById(R.id.btnNewsPress);

        btnPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();

            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);








    }

    private void fetchData() {
        Log.d(TAG, "connection "+String.valueOf(mMeteor.isConnected()));
        if (mMeteor.isConnected()) {
            mMeteor.loginWithUsername("bishwa", "1234", new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    final String[] params = {};
                    String subscriptionId = mMeteor.subscribe("statuses", params, new SubscribeListener() {

                        @Override
                        public void onSuccess() {
                            Log.d(TAG,params.toString());


                        }

                        @Override
                        public void onError(String error, String reason, String details) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();

                        }
                    });




                    String[] collectionNames = mMeteor.getDatabase().getCollectionNames();
                    for (String doc :collectionNames){

                        Log.d(TAG, "Logged in: " + doc);

                    }
//
//                            Collection collection = mMeteor.getDatabase().getCollection("users");
//                            Log.d(TAG, "Logged in: " + collection);
//                            int numCollections = mMeteor.getDatabase().count();
//                            Log.d(TAG, "Logged in: " + numCollections);


                }
                @Override
                public void onError(String error, String reason, String details) {
                    Log.d(TAG, "Error: " + error + " " + reason + " " + details);
                }
            });
        }


    }

    @Override
    public void onConnect(boolean signedInAutomatically) {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {

//        Log.d(TAG,""+list.size());
//        Log.d(TAG,""+newValuesJson);


        if(collectionName.equals("Statuses")){
            try {
                JSONObject jsonObject= new JSONObject(newValuesJson);
                String address=jsonObject.getString("address");
                double lat=jsonObject.getDouble("latitude");
                double lng=jsonObject.getDouble("longitude");
                String level=jsonObject.getString("level");
                String averageSpeed=jsonObject.getString("averageSpeed");
                String trafficVolume=jsonObject.getString("trafficVolume");
                String note=jsonObject.getString("note");
                String createdAt=jsonObject.getString("createdAt");
                String contactNo=jsonObject.getString("contactNo");
                if(!String.valueOf(lat).equals(null) && !String.valueOf(lng).equals(null) ){
                    list.add(new statusObject(address,lat,lng,level,averageSpeed,trafficVolume
                            ,note,createdAt,contactNo));

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


        for (statusObject statusObject:list) {
            Random r=new Random();
            double latRand=r.nextDouble();
            double lngRand=r.nextDouble();
           // Log.d(TAG,list.size()+" "+statusObject.lat+"  "+ statusObject.lng+ " "+latRand+"  "+lngRand);
            LatLng latLng=new LatLng(statusObject.lat,statusObject.lng);
            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .title(statusObject.level+" "+statusObject.trafficVolume+" "
                    +statusObject.averageSpeed));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        }


        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.727358, 90.389717),10), 2000, null);


    }
}
