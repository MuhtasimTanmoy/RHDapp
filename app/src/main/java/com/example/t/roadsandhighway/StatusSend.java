package com.example.t.roadsandhighway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;

public class StatusSend extends AppCompatActivity implements MeteorCallback {

    private EditText etTrafficVolume, etAverageSpeed, etNotes;
    private Spinner spnrTrafficLevel;
    private Button btnSubmit;
    private Meteor mMeteor;
    private static String TAG = "statusSend";
    final List<String> categories = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_send);


        init();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMeteor.isConnected()){
                    final Map<String, Object> values = new HashMap<String, Object>();
                    spnrTrafficLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getApplicationContext(), position, Toast.LENGTH_SHORT).show();
                            values.put("level", categories.get(position));
                            Log.d(TAG, ""+position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    values.put("averageSpeed",etAverageSpeed.getText().toString());
                    values.put("trafficVolume", etTrafficVolume.getText().toString());
                    values.put("note", etNotes.getText().toString());
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
            }
        });
    }

    private void init() {


        etAverageSpeed = (EditText) findViewById(R.id.etAverageSpeed);
        etTrafficVolume = (EditText) findViewById(R.id.etTrafficVolume);
        etNotes = (EditText) findViewById(R.id.etNotes);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        spnrTrafficLevel=(Spinner) findViewById(R.id.spnrTrafficLevel);

        // create a new instance
        mMeteor = new Meteor(this, "ws://192.168.0.102:3000/websocket");

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the connection
        mMeteor.connect();




        // Spinner Drop down elements
        categories.add("TJ-1");
        categories.add("TJ-2");
        categories.add("TJ-3");
        categories.add("TJ-4");
        categories.add("TJ-5");
        categories.add("TJ-6");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnrTrafficLevel.setAdapter(dataAdapter);
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
