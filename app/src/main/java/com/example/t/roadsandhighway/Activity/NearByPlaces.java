package com.example.t.roadsandhighway.Activity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.t.roadsandhighway.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class NearByPlaces extends AppCompatActivity {


    private EditText etRadius;
    private Button btnSubmit;
    private static String TAG = "Json";
    private Button btnMakeObjectRequest, btnMakeArrayRequest;

    private ProgressDialog pDialog;

    private TextView txtResponse;

    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);


        etRadius=(EditText) findViewById(R.id.etFindRadius) ;
        btnSubmit= (Button) findViewById(R.id.btnFindSubmit);
        txtResponse = (TextView) findViewById(R.id.txtResponse);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // making json object request
                makeJsonObjectRequest();
            }
        });



    }

    /**
     * Method to make json object request where json response starts wtih {
     * */


    ArrayList<LatLng> latLngs= new ArrayList<>();

    private void makeJsonObjectRequest() {

        String urlJsonObj = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=-33.8670522,151.1957362&radius="+etRadius.getText().toString()+
                "&type=restaurant&keyword=cruise&key="
                +"AIzaSyAuDPbEB8OfpLi2aXcPa4KnTQyiuQurZ_Y";


        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Json Data Found");

                try {
                    jsonResponse = "";
                    JSONArray results=response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {

                        JSONObject place = (JSONObject) results
                                .get(i);

                        String name = place.getString("name");
                        JSONObject geometry = place
                                .getJSONObject("geometry");
                        JSONObject location =geometry.getJSONObject("location");
                        String lat=location.getString("lat").toString();
                        String lng=location.getString("lng").toString();
                        jsonResponse += "name: " + name + "\n";
                        jsonResponse += "lat: " + location.getString("lat").toString() + "\n";
                        jsonResponse += "lat: " + location.getString("lng").toString() + "\n";
                        jsonResponse+="\n\n\n";

                        latLngs.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
                    }

                    txtResponse.setText(jsonResponse);

                    Intent intent= new Intent(getApplicationContext(), ShowNearbyPlaces.class);
                    intent.putExtra("locList", latLngs);
                    Log.d(TAG, latLngs.size()+"");

                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }




}