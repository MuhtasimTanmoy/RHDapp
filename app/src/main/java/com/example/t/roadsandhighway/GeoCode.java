package com.example.t.roadsandhighway;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.t.roadsandhighway.Activity.AppController;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by t on 5/27/17.
 */

public class GeoCode {
    String place;
    String URL;
    Context context;

    GeoCode(Context context){
        this.context=context;


    }

    public void setPlace(String place) {
        this.place=place.replace(" ","+");
        Log.d("Checkme",this.place);

        this.URL=makeURL(this.place);
    }

    public String getURL() {
        return URL;
    }

    public void jsonReq(String URL, final CallBack callBack) {


        Log.d("PATH", "Entering");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    JSONObject latlong=jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

                    double lat=latlong.getDouble("lat");
                    double lng=latlong.getDouble("lng");

                    Log.d("Checkme","ROFVKXOCSOXSMOC>>>>>>"+Double.toString(lat));
                    callBack.onSuccess(lat,lng);




                } catch (JSONException e) {
                    callBack.onFail(e.toString());
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






    public String makeURL(String place) {

        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/geocode/json");
        urlString.append("?address=");// from
        urlString.append(place);
        urlString.append("&key=AIzaSyAuDPbEB8OfpLi2aXcPa4KnTQyiuQurZ_Y");
        return urlString.toString();
    }

    public interface CallBack {
        void onSuccess(Double lat,Double lng);

        void onFail(String msg);
    }

}
