package com.example.t.roadsandhighway;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.t.roadsandhighway.Activity.AppController;
import com.example.t.roadsandhighway.Activity.ShowNearbyPlaces;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by t on 5/24/17.
 */

public class TripPathShow {
    String destination;
    String start;
    String URL;
    Context context;

    List<LatLng> list;

    TripPathShow(Context context, String destination, String start) {
        destination = destination;
        start = start;
        URL = makeURL(start, destination);
        context = context;
        list = new ArrayList<LatLng>();


    }

    public String getURL() {
        return URL;
    }

    public void parseMovieDetails(final CallBack onCallBack) {


        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=Bu&types=establishment&location=23.726574,90.389868&radius=1000&strictbounds&key=AIzaSyAuDPbEB8OfpLi2aXcPa4KnTQyiuQurZ_Y";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("check",response.toString());

                onCallBack.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    public interface CallBack {
        void onSuccess(String str);

        void onFail(String msg);
    }


    public List<LatLng> jsonReq(String URL) {


        Log.d("PATH", "Entering");


        Log.d("PATH", URL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.d("PATH", "Success");


                //Log.d("PATH", String.valueOf(jsonObject));


                JSONArray routeArray = null;
                try {
                    routeArray = jsonObject.getJSONArray("routes");
                    JSONObject routes = routeArray.getJSONObject(0);
                    JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                    String encodedString = overviewPolylines.getString("points");
//                    list.clear();
//                    list.addAll( decodePoly(encodedString));
                    List<LatLng> l = new ArrayList();
                    l = decodePoly(encodedString);

                    for (LatLng li : l) {
                        Log.d("check", li.toString());

                    }


                    // startActivity(intent);
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
        if (list != null) {
            Log.d("check", "YO" + list.toString());
        }
        return list;
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

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


    public String makeURL(String source, String destination) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(source);
        urlString.append("&destination=");// to
        urlString.append(destination);
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyAuDPbEB8OfpLi2aXcPa4KnTQyiuQurZ_Y");
        return urlString.toString();
    }

    public String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyAuDPbEB8OfpLi2aXcPa4KnTQyiuQurZ_Y");
        return urlString.toString();
    }
}
