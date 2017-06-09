package com.example.t.roadsandhighway;

/**
 * Created by t on 6/8/17.
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class StatusSendActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,AdapterView.OnItemClickListener {

    private EditText etNotes;
    private AutoCompleteTextView etMyLocation;
    private Spinner spnrTrafficLevel;
    private Button btnSubmit;
    private LatLng myLocation;
    private GeoCode geoCode;
    private static String TAG = "statusSend";
    final List<String> categories = new ArrayList<String>();
    ComWithServer comWithServer;
    final Map<String, Object> values = new HashMap<String, Object>();
    private Map<String, String> trafiic = new HashMap<String, String>();
    private FloatingActionButton fab;
    private String message;
    private String  number="01521465317";




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_send);

        //View v=inflater.inflate(R.layout.activity_status_send,container,false);

        etMyLocation= (AutoCompleteTextView)findViewById(R.id.myLocation);
        etNotes = (EditText) findViewById(R.id.etNotes);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        spnrTrafficLevel = (Spinner) findViewById(R.id.spnrTrafficLevel);
        etMyLocation.setAdapter(new GooglePlacesAutocompleteAdapter(getApplicationContext(),R.layout.list_item));
        etMyLocation.setOnItemClickListener(this);
        geoCode=new GeoCode(getApplicationContext());
        fab= (FloatingActionButton) findViewById(R.id.triggerMessageInFab);





        init();


        etMyLocation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etMyLocation.getRight() - etMyLocation.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        SingleShotLocationProvider.requestSingleUpdate(getApplicationContext(), new SingleShotLocationProvider.LocationCallback() {
                            @Override
                            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                                Log.d("Location", location.toString());
                                myLocation=new LatLng(location.getLat(),location.getLang());
                                etMyLocation.setText("Your location");


                            }
                        });

                        return true;
                    }
                }

                return false;
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (comWithServer.isConnected()) {
                    Toast.makeText(getApplicationContext(), "Delivered", Toast.LENGTH_SHORT).show();

                    values.put("averageSpeed", null);
                    values.put("trafficVolume", null);
                    values.put("note", etNotes.getText().toString());
                    values.put("filePath", null);

                    values.put("latitude", myLocation.latitude);
                    values.put("longitude",myLocation.longitude);

                    values.put("address", "BUET");
                    Log.d(TAG,values.toString());


                    Object[] queryParams = {values};

                    comWithServer.callFucntion("statuses.insertWithLatLong", queryParams);

                }
            }
        });
    }




    public void onItemClick(AdapterView adapterView, View view, int position, long id) {

        String str = (String) adapterView.getItemAtPosition(position);
        geoCode.setPlace(str);
        Log.d("check", geoCode.getURL());
//                Log.d("check", autoCompViewD.getText().toString());

        geoCode.jsonReq(geoCode.getURL(), new GeoCode.CallBack() {
            @Override
            public void onSuccess(Double lat, Double lng) {
                Log.d("Hello","world"+lat.toString()+ lng.toString());
                myLocation=new LatLng(lat,lng);


            }

            @Override
            public void onFail(String msg) {

            }
        });
        //Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        values.put("level", trafiic.get(categories.get(position)));
        Log.v(TAG, "" + position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void init() {

        comWithServer = new ComWithServer(getApplicationContext());



        // Spinner Drop down elements

        trafiic.put("Traffic Jam : Level 1","TJ-1");
        trafiic.put("Traffic Jam : Level 2","TJ-2");
        trafiic.put("Traffic Jam : Level 3","TJ-3");
        trafiic.put("Traffic Jam : Level 4","TJ-4");
        trafiic.put("Traffic Jam : Level 5","TJ-5");
        trafiic.put("Traffic Jam : Level 6","TJ-6");


        categories.add("Traffic Jam : Level 1");
        categories.add("Traffic Jam : Level 2");
        categories.add("Traffic Jam : Level 3");
        categories.add("Traffic Jam : Level 4");
        categories.add("Traffic Jam : Level 5");
        categories.add("Traffic Jam : Level 6");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        // attaching data adapter to spinner
        spnrTrafficLevel.setAdapter(dataAdapter);



        spnrTrafficLevel.setOnItemSelectedListener(this);
        final Context context=this;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TEMP","fffff");

                // Toast.makeText(getApplicationContext(),"fsdf",Toast.LENGTH_SHORT).show();
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                                message=userInputDialogEditText.getText().toString();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                performSMS(number,message);

                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }

        });


    }





    void performSMS(String number,String body){


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null,body, null, null);
                Toast.makeText(this, "Message Sent",
                        Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(this,ex.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    performSMS(number,message);

                } else {

                    Toast.makeText(this, "Not granted", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
