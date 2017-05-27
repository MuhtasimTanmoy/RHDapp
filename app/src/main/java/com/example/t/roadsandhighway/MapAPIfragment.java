package com.example.t.roadsandhighway;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
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

import static com.example.t.roadsandhighway.R.color.*;

/**
 * Created by t on 5/24/17.
 */

public class MapAPIfragment extends Fragment implements AdapterView.OnItemClickListener {

    AutoCompleteTextView autoCompViewD;
    AutoCompleteTextView autoCompViewS;
    String desLatLng;
    String srcLatLng;

    ImageButton gpsEnable;

    Button getRoute;
    private static String TAG = "auto";

    TripPathShow tripPathShow;
    GeoCode geoCode;

    List<LatLng> list;
    ArrayList<String> arrayList;


    public String getDesLatLng() {
        return desLatLng;
    }

    public String getSrcLatLng() {
        return srcLatLng;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mapapi, container,
                false);
        autoCompViewD = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextViewD);

        autoCompViewD.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.list_item));
        autoCompViewD.setOnItemClickListener(this);
        autoCompViewS = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextViewS);

        autoCompViewS.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.list_item));
        autoCompViewS.setOnItemClickListener(this);

        getRoute = (Button) v.findViewById(R.id.getRoute);
        list = new ArrayList<LatLng>();
        arrayList = new ArrayList<>();

        gpsEnable= (ImageButton) v.findViewById(R.id.getFromGps);

        geoCode=new GeoCode(getContext());



        gpsEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleShotLocationProvider.requestSingleUpdate(getContext(), new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        Log.d("Location",location.toString());
                        srcLatLng=String.valueOf(location.getLat()) + "," + String.valueOf(location.getLang());
                        autoCompViewS.setText("Your location");
                        gpsEnable.setBackgroundColor(R.color.telenorBlue);


                    }
                });
            }
        });


        getRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(),autoCompViewD.getText().toString()+autoCompViewS.getText().toString(),Toast.LENGTH_SHORT).show();
                Log.d("check", getDesLatLng()+getSrcLatLng());



                tripPathShow = new TripPathShow(getContext(), getDesLatLng(), getSrcLatLng());

                Log.d("check", tripPathShow.getURL());

                tripPathShow.jsonReq(tripPathShow.getURL(), new TripPathShow.CallBack() {
                    @Override
                    public void onSuccess(List<LatLng> list) {
                        Intent intent= new Intent(getContext(), ShowNearbyPlaces.class);
                        intent.putExtra("locList", (Serializable) list);
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });

            }
        });



        return v;
    }


    private ArrayList makeJsonObjectRequest(String input) {

        String urlJsonObj = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + input + "&types=establishment&location=23.726574,90.389868&radius=1000&strictbounds&key=AIzaSyAuDPbEB8OfpLi2aXcPa4KnTQyiuQurZ_Y\n";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, String.valueOf(response));

                ArrayList<String> arrayLt = autocomplete(response);

                for (String s : arrayLt) {
                    Log.d(TAG, "1." + s);

                }
                arrayList = arrayLt;


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        return arrayList;
    }


    public static ArrayList autocomplete(JSONObject jsonObject) {
        ArrayList resultList = null;

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = jsonObject;
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {

                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {

        String str = (String) adapterView.getItemAtPosition(position);
        geoCode.setPlace(str);
        Log.d("check", geoCode.getURL());
//                Log.d("check", autoCompViewD.getText().toString());

        geoCode.jsonReq(geoCode.getURL(), new GeoCode.CallBack() {
            @Override
            public void onSuccess(Double lat,Double lng) {
                Log.d("success",String.valueOf(lat)+"++++++-------+++++"+String.valueOf(lng));
                if(desLatLng==null) {
                    desLatLng = String.valueOf(lat) + "," + String.valueOf(lng);
                }
                else{
                    srcLatLng=String.valueOf(lat) + "," + String.valueOf(lng);
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }


    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = makeJsonObjectRequest(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(),"resumed",Toast.LENGTH_SHORT).show();
        desLatLng=null;
        srcLatLng=null;

    }
}
