package com.example.t.roadsandhighway;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

import static com.example.t.roadsandhighway.StaticData.ADDRESS;

public class SignIn extends AppCompatActivity implements MeteorCallback {

    private EditText etUsername, etPassword;
    private Button btnSignIn;
    private Button btnSignUp;
    public static Meteor mMeteor;
    private static String TAG = "signInPage";
    AutoCompleteTextView myLocation;
    DbHelper dbHelper = new DbHelper(this);
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        checkPreviousLogIn();
        init();


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(), Boolean.toString(mMeteor.isConnected()), Toast.LENGTH_SHORT).show();
                if (mMeteor.isConnected()) {
                    showpDialog();
                    String userName = etUsername.getText().toString();
                    String passWord = etPassword.getText().toString();
                    Log.d(TAG, userName + " " + passWord);
                    mMeteor.loginWithUsername(userName, passWord, new ResultListener() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d(TAG, "Logged in: " + result);
                            int row = dbHelper.numberOfRows("logIn");
                            // Toast.makeText(getApplicationContext(), "  "+row , Toast.LENGTH_SHORT).show();
                            if (row == 0) {

                                dbHelper.insertLogInStatus("true");

                            } else {
                                dbHelper.updateStatus(1, "true");

                            }


//                            Document[] documents=mMeteor.getDatabase().getCollection("users").find();
//                            for(Document d: documents){
//                                Log.d(TAG,d.toString());
//
//
//                                Document profile =(Document) d.getField("profile");
//
//                                Log.d(TAG,profile.toString());
//
//
////                                    dbHelper.insertUserDetails(etUsername.getText().toString(),
////                                            profile.getString("type"),profile.getString("contactNo"),
////                                            profile.getString("address"), etPassword.getText().toString());
//
//
//                            }

                            hidepDialog();
                            Intent intent = new Intent(getApplicationContext(), Administer.class);

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
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    mMeteor.connect();

                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
//                Intent intent = new Intent(getApplicationContext(), SignUp.class);

                startActivity(intent);
            }
        });


    }

    private void checkPreviousLogIn() {
        // Gets the data repository in write mode


        int row = dbHelper.numberOfRows("logIn");
        // Toast.makeText(getApplicationContext(), "  "+row , Toast.LENGTH_SHORT).show();
        if (row == 1) {
            Cursor res = dbHelper.getLoginStatus(1);
            res.moveToFirst();
            String status = res.getString(res.getColumnIndex("status"));
            if (status.equals("true")) {
                Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                startActivity(intent);
                finish();

            }

        }


    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void init() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        myLocation= (AutoCompleteTextView) findViewById(R.id.myLocation);

        // create a new instance
        mMeteor = new Meteor(this, ADDRESS,new InMemoryDatabase());

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the conngiection
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
        if(collectionName.equals("users")){
            Log.d(TAG,newValuesJson.toString()+"  "+collectionName);

            JSONObject jsonObject= null;
            try {

                int row = dbHelper.numberOfRows("users");
                // Toast.makeText(getApplicationContext(), "  "+row , Toast.LENGTH_SHORT).show();
                Log.d("profile",row+" ");
                if (row == 0) {
                    jsonObject = new JSONObject(newValuesJson);
                    JSONObject profile=jsonObject.getJSONObject("profile");

                    dbHelper.insertUserDetails(jsonObject.getString("username"),
                            profile.getString("type"),profile.getString("contactNo"),
                            profile.getString("address"), etPassword.getText().toString());

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
           }
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }
}
