package com.example.t.roadsandhighway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
    private static String TAG="checkThisTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.test);


        // create a new instance
        mMeteor = new Meteor(this, "ws://192.168.0.105:3000/websocket");

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the connection
        mMeteor.connect();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (mMeteor.isConnected()) {
//                    mMeteor.loginWithEmail("tanmoyme", "tanmoyme", new ResultListener() {
//                        @Override
//                        public void onSuccess(String result) {
//                            Log.d(TAG, "Logged in: " + result);
//
//                            try {
//                                JSONObject login = new JSONObject(result);
//
//                                String userId = login.getString("id");
//                                String token = login.getString("token");
//                                long expiry = login.get                                                                   JSONObject("tokenExpires").getLong("$date");
//
//                                Map<String, Object> user = new HashMap<String, Object>();
//                                user.put("_id", userId);
//
//                                Object[] queryParams = {user};
//

//                                mMeteor.call("/Users/find", queryParams, new ResultListener() {
//                                    @Override
//                                    public void onSuccess(String result) {
//                                        Log.d(TAG, "Call result: " + result);
//                                    }
//
//                                    @Override
//                                    public void onError(String error, String reason, String details) {
//                                        Log.d(TAG, "Error: " + error + " " + reason + " " + details);
//                                    }
//                                });
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onError(String error, String reason, String details) {
//                            Log.d(TAG, "Error: " + error + " " + reason + " " + details);
//                        }
//                    });
//                }

                String subscriptionId = mMeteor.subscribe("Statuses");
                Toast.makeText(getApplicationContext(),Boolean.toString(mMeteor.isConnected())+" and" + subscriptionId,Toast.LENGTH_LONG).show();
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
