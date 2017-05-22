package com.example.t.roadsandhighway.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.t.roadsandhighway.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by t on 5/22/17.
 */

public class TripActivity extends AppCompatActivity {

    String URL;
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.seeLatLang);

        URL=makeURL(23.727358, 90.389717,23.729401, 90.395568);

        Log.d("PATH",URL);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonReq(URL);
            }
        });




    }


    public void jsonReq(String URL){

        Log.d("PATH", "Entering");


        Log.d("PATH", URL);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Method.GET,
                URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.d("PATH", "Success");


                //Log.d("PATH", String.valueOf(jsonObject));


                JSONArray routeArray = null;
                try {
                    routeArray = jsonObject.getJSONArray("routes");
                    JSONObject routes = routeArray.getJSONObject(1);
                    JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                    String encodedString = overviewPolylines.getString("points");
                    List<LatLng> list = decodePoly(encodedString);

                    Intent intent= new Intent(getApplicationContext(), ShowNearbyPlaces.class);
                    intent.putExtra("locList", (Serializable) list);
                    startActivity(intent);
//                    for(LatLng l : list){
//                        Log.d("PATH", l.latitude +"-----"+l.longitude);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("PATH", String.valueOf(volleyError));

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }






    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyAuDPbEB8OfpLi2aXcPa4KnTQyiuQurZ_Y");
        return urlString.toString();
    }
}
