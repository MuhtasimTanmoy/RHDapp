package com.example.t.roadsandhighway.Activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.t.roadsandhighway.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by t on 5/27/17.
 */

public class SampleTest extends AppCompatActivity {

    final String TAG="aaaaaaaaaaaa";

    EditText editText;
    Button button;
    Geocoder geocoder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        button= (Button) findViewById(R.id.sampleButton);
        editText= (EditText) findViewById(R.id.sample);
        geocoder=new Geocoder(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Address> list=null;

                try {
                    list=geocoder.getFromLocationName(String.valueOf(editText.getText()),1);
                    Log(list.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(list!=null && list.size()!=0){
                Address address=list.get(0);

                    Log.d(TAG,editText.getText().toString());

                String locality=address.getLocality();


                LatLng l=new LatLng(address.getLatitude(),address.getLongitude());

                Log.d(TAG,locality+l.toString());}


                Log("Hello");


            }
        });


    }
    void Log(String s){
        Log.d(TAG,s);
    }
}
