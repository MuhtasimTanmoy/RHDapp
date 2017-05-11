package com.example.t.roadsandhighway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;

public class SignUp extends AppCompatActivity implements MeteorCallback {


    private EditText etUsername, etPassword, etConfirmPassword, etCoNtactNo, etLaitude, etLongitude, etAddress;
    private Button btnSingUp;
    private Meteor mMeteor;
    private static String TAG = "signUpPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();


        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMeteor.isConnected() && etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    final Map<String, Object> values = new HashMap<String, Object>();
//                    username: data.username,
//                            password: data.password,
//                            profile:{
//                        type: "Reporter",
//                                contactNo: data.contactNo,
//                                address: data.address,
//                                latitude: data.latitude,
//                                longitude: data.longitude,
//                    }
                    values.put("username", etUsername.getText().toString());
                    values.put("password", etPassword.getText().toString());
                    values.put("contactNo", etCoNtactNo.getText().toString());
                    values.put("address",etAddress.getText().toString());
                    values.put("latitude", etLaitude.getText().toString());
                    values.put("longitude", etLongitude.getText().toString());
                    Object[] queryParams = {values};
                    mMeteor.call("user.create", queryParams, new ResultListener() {

                        @Override
                        public void onSuccess(String result) {
                            Log.d(TAG, "success  in inserting");

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
        etUsername = (EditText) findViewById(R.id.etSignUpUsername);
        etPassword = (EditText) findViewById(R.id.etSignUpPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etSignUpConfirmPasword);
        etCoNtactNo = (EditText) findViewById(R.id.etSignUpContactNo);
        etLaitude = (EditText) findViewById(R.id.etSignUpLatitude);
        etAddress = (EditText) findViewById(R.id.etSignUuAddress);
        etLongitude = (EditText) findViewById(R.id.etSignUpLongitude);
        btnSingUp = (Button) findViewById(R.id.btnSignUp);


        // create a new instance
        mMeteor = new Meteor(this, "ws://192.168.0.102:3000/websocket");

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the connection
        mMeteor.connect();
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
