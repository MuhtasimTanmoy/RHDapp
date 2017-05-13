package com.example.t.roadsandhighway.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.t.roadsandhighway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Document;

public class MainActivity extends AppCompatActivity implements MeteorCallback {

    private Meteor mMeteor;
    private Button button;
    private Button smsActivity;
    private Button ocr;
    private static String TAG = "checkThisTag";
    ToggleButton toggleButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.test);
        smsActivity= (Button) findViewById(R.id.sendsms);
        toggleButton= (ToggleButton) findViewById(R.id.toggle);
        ocr= (Button) findViewById(R.id.ocr);



        // create a new instance
        mMeteor = new Meteor(this, "ws://192.168.0.105:3000/websocket");

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the connection
        mMeteor.connect();

        ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,OcrActivity.class);
                startActivity(intent);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                meteorTestDataInsert();


            }
        });

        if(!runtime_permissions())
            enable_buttons();





        smsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SmsActivity.class);
                startActivity(intent);

            }

        });


    }

    private void enable_buttons() {
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent i = new Intent(getApplicationContext(), GpsService.class);
                    startService(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), GpsService.class);
                    stopService(i);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else {
                runtime_permissions();
            }
        }
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    void meteorTestDataInsert(){
        if (mMeteor.isConnected()) {
            mMeteor.loginWithUsername("tanmoy", "tanmoy", new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    Log.d(TAG, "Logged in: " + result);

                    String subscriptionId = mMeteor.subscribe("Statuses");
                    Toast.makeText(getApplicationContext(), Boolean.toString(mMeteor.isConnected()) + " and" + subscriptionId, Toast.LENGTH_LONG).show();
                    Map<String, Object> values = new HashMap<String, Object>();
                    values.put("level", "TJ-2");
                    values.put("averageSpeed", "");
                    values.put("trafficVolume", "");
                    values.put("note", "");
                    values.put("filePath", null);
                    values.put("latitude", 22.4);
                    values.put("longitude", 90);
                    values.put("address", "BUET");
                    Object[] queryParams = {values};
                    mMeteor.call("statuses.insert",queryParams,new ResultListener() {

                        @Override
                        public void onSuccess(String result) {
                            Log.d(TAG, "success  in inserting" );

                        }

                        @Override
                        public void onError(String error, String reason, String details) {
                            Log.d(TAG, "Error: " + error + " " + reason + " " + details);

                        }
                    });
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

    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }
}
