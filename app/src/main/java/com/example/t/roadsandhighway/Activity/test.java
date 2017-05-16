package com.example.t.roadsandhighway.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.t.roadsandhighway.R;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Document;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

/**
 * Created by t on 5/15/17.
 */

public class test extends AppCompatActivity implements MeteorCallback {

    private Meteor mMeteor;
    private Button button;
    private static String TAG = "checkThisTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.test);



        // create a new instance
        mMeteor = new Meteor(this, "ws://192.168.1.103:3000/websocket",new InMemoryDatabase());

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the connection
        mMeteor.connect();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //                if (mMeteor.isConnected()) {
//                    mMeteor.loginWithUsername("tanmoy", "tanmoy", new ResultListener() {
//                        @Override
//                        public void onSuccess(String result) {
//                            //Log.d(TAG, "Logged in: " + result);
//
//                            String subscriptionId = mMeteor.subscribe("Statuses");
//                            Toast.makeText(getApplicationContext(), Boolean.toString(mMeteor.isConnected()) + " and" + subscriptionId, Toast.LENGTH_LONG).show();




                Log.d(TAG, "Logged in: " + mMeteor.isConnected());

                if (mMeteor.isConnected()) {
                    mMeteor.loginWithUsername("tanmoy", "tanmoy", new ResultListener() {
                        @Override
                        public void onSuccess(String result) {
                            //Log.d(TAG, "Logged in: " + result);

                            String subscriptionId = mMeteor.subscribe("Statuses");
                            Toast.makeText(getApplicationContext(), Boolean.toString(mMeteor.isConnected()) + " and" + subscriptionId, Toast.LENGTH_LONG).show();

                            mMeteor.call("testMethod",null,new ResultListener() {

                                @Override
                                public void onSuccess(String result) {
                                    Log.d(TAG, "success  in inserting"+result);

                                }

                                ;

                                @Override
                                public void onError(String error, String reason, String details) {
                                    Log.d(TAG,error+reason+details);
                                }

                                ;
                            });
//                            Document[] documents = mMeteor.getDatabase().getCollection("Statuses").find();
//
//                            Log.d(TAG, "Logged in: " + documents);
//                            for (Document doc :documents){
//
//                                Log.d(TAG, "Logged in: " + doc);
//
//                            }

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

            Log.d(TAG,newValuesJson);


    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        Log.d(TAG,updatedValuesJson+removedValuesJson);
    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }
}
