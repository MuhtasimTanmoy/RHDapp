package com.example.t.roadsandhighway.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    private static String TAG = "checkThisTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.test);
        smsActivity= (Button) findViewById(R.id.sendsms);


        // create a new instance
        mMeteor = new Meteor(this, "ws://192.168.0.109:3000/websocket");

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the connection
        mMeteor.connect();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                meteorTestDataInsert();


            }
        });




        smsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SmsActivity.class);
                startActivity(intent);
            }
        });


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
        //                int numCollections = mMeteor.getDatabase().count();

//                  String subscriptionId = mMeteor.subscribe("suddenSicknesses");
//
//                mMeteor.call("myMethod", new Object[] { "description", "ddfsdfs" }  );
//
//                Log.v("myTag",Integer.toString(numCollections));
    }
}