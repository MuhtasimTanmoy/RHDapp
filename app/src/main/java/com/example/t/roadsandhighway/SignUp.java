package com.example.t.roadsandhighway;

import android.app.ProgressDialog;
import android.content.Intent;
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
import im.delight.android.ddp.db.memory.InMemoryDatabase;

import static com.example.t.roadsandhighway.StaticData.ADDRESS;

public class SignUp extends AppCompatActivity implements MeteorCallback {


    private EditText etUsername, etPassword, etConfirmPassword, etCoNtactNo, etLaitude, etLongitude, etAddress;
    private Button btnSingUp,btnLogIn;
    private Meteor mMeteor;
    private static String TAG = "signUpPage";
    private ProgressDialog pDialog;

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();


        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "Not granted", Toast.LENGTH_SHORT).show();
                if (mMeteor.isConnected() ) {
                    if(etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
                        showpDialog();
                        final Map<String, Object> values = new HashMap<String, Object>();
                        values.put("username", etUsername.getText().toString());
                        values.put("password", etPassword.getText().toString());
                        values.put("contactNo", etCoNtactNo.getText().toString());
                        values.put("address",etAddress.getText().toString());
                        values.put("latitude", Double.parseDouble(etLaitude.getText().toString()));
                        values.put("longitude", Double.parseDouble(etLongitude.getText().toString()));
                        Object[] queryParams = {values};

                        mMeteor.call("user.create", queryParams, new ResultListener() {

                            @Override
                            public void onSuccess(String result) {
                                hidepDialog();
                                Log.d(TAG, "success  in inserting");
                                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                startActivity(intent);

                            }

                            @Override
                            public void onError(String error, String reason, String details) {
                                hidepDialog();
                                Log.d(TAG, "Error: " + error + " " + reason + " " + details);
                                Toast.makeText(getApplicationContext(), "Enter valid information", Toast.LENGTH_SHORT).show();


                            }
                        });

                    }else{
                        Toast.makeText(getApplicationContext(), "Password not matched", Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    mMeteor.connect();

                }
            }

        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
            }
        });
    }


    private void init() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        etUsername = (EditText) findViewById(R.id.etSignUpUsername);
        etPassword = (EditText) findViewById(R.id.etSignUpPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etSignUpConfirmPasword);
        etCoNtactNo = (EditText) findViewById(R.id.etSignUpContactNo);
        etLaitude = (EditText) findViewById(R.id.etSignUpLatitude);
        etAddress = (EditText) findViewById(R.id.etSignUpAddress);
        etLongitude = (EditText) findViewById(R.id.etSignUpLongitude);
        btnSingUp = (Button) findViewById(R.id.signUp);
        btnLogIn= (Button) findViewById(R.id.signIn);


        // create a new instance
        mMeteor = new Meteor(this,ADDRESS,new InMemoryDatabase());

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
        Log.d("Data", newValuesJson);

    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {


    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }
}
