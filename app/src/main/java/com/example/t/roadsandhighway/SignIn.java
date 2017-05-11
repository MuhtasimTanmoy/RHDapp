package com.example.t.roadsandhighway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.t.roadsandhighway.R;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;

public class SignIn extends AppCompatActivity implements MeteorCallback {

    private EditText etUsername, etPassword;
    private Button btnSignIn;
    private Meteor mMeteor;
    private static String TAG = "signInPage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMeteor.isConnected()) {
                    String userName = etUsername.getText().toString();
                    String passWord = etPassword.getText().toString();
                    Log.d(TAG,userName+ " "+passWord );
                    mMeteor.loginWithUsername(userName, passWord, new ResultListener() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d(TAG, "Logged in: " + result);
                            Intent intent = new Intent(getApplicationContext(), StatusSend.class);
                            startActivity(intent);

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
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);


        // create a new instance
        mMeteor = new Meteor(this, "ws://192.168.0.106:3000/websocket");

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
