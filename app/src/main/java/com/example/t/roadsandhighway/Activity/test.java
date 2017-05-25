package com.example.t.roadsandhighway.Activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.t.roadsandhighway.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;
import im.delight.android.ddp.db.Document;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

/**
 * Created by t on 5/15/17.
 */

public class test extends AppCompatActivity implements MeteorCallback {

    private Meteor mMeteor;
    private Button button;
    private static String TAG = "checkThisTag";
    EditText editText;
    Button seeLatLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.test);
        seeLatLang= (Button) findViewById(R.id.seeLatLang);
        editText= (EditText) findViewById(R.id.et);



        // create a new instance
        mMeteor = new Meteor(this, "ws://52.175.255.59/websocket",new InMemoryDatabase());

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the connection
        mMeteor.connect();






        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d(TAG,"YYYYYYYYYYYYYYYYYY"+ String.valueOf(  mMeteor.isConnected()));

                if (mMeteor.isConnected()) {
                    mMeteor.loginWithUsername("admin", "admin", new ResultListener() {
                        @Override
                        public void onSuccess(String result) {
                            String[] params = {};
                            String subscriptionId = mMeteor.subscribe("statuses", params, new SubscribeListener() {

                                @Override
                                public void onSuccess() {
                                   Log.d(TAG," onsuccess Yes");

                                        Document[] documents = mMeteor.getDatabase().getCollection("Statuses").find();

                                        for (Document x : documents) {
                                            Log.d(TAG, (String) x.getField("level"));
                                        }

                                }

                                @Override
                                public void onError(String error, String reason, String details) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();

                                }
                            });




//                            String[] collectionNames = mMeteor.getDatabase().getCollectionNames();
//                            for (String doc :collectionNames){
//
//                                Log.d(TAG, "Logged in: " + doc);
//
//                            }
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
        });





        seeLatLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


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

            Log.d(TAG,"ON Data added>>>>>>>>"+newValuesJson+" ------------ "+collectionName);


    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        //Log.d(TAG,updatedValuesJson+removedValuesJson);
    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }
}
