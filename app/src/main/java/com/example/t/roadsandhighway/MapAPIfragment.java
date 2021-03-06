package com.example.t.roadsandhighway;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.SubscribeListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;


/**
 * Created by t on 5/24/17.
 */

public class MapAPIfragment extends Fragment implements AdapterView.OnItemClickListener, MeteorCallback, OnMapReadyCallback {

    private Meteor mMeteor;

    AutoCompleteTextView autoCompViewD;
    AutoCompleteTextView autoCompViewS;
    String desLatLng;
    String srcLatLng;

    LinearLayout holder;
    LinearLayout openHolder;

    TextView open;


    // ImageButton gpsEnable;

    Button getRoute;

    private static String TAG = "auto";

    TripPathShow tripPathShow;
    GeoCode geoCode;
    TextView close;

    ArrayList<String> arrayList;

    ProgressDialog progressDialog;

    SupportMapFragment supportMapFragment;

    ArrayList<StatusObject> statusList = new ArrayList<>();

    List<LatLng> pathList = new ArrayList<>();




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
        holder = (LinearLayout) v.findViewById(R.id.holder);
        openHolder = (LinearLayout) v.findViewById(R.id.openHolder);
        open = (TextView) v.findViewById(R.id.open);
        autoCompViewD.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.list_item));
        autoCompViewD.setOnItemClickListener(this);
        autoCompViewS = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextViewS);
        close = (TextView) v.findViewById(R.id.close);
        autoCompViewS.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.list_item));
        autoCompViewS.setOnItemClickListener(this);

        getRoute = (Button) v.findViewById(R.id.getRoute);

        arrayList = new ArrayList<>();

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Route loading with recent jam as red marker");
        progressDialog.setCancelable(true);

//        gpsEnable = (ImageButton) v.findViewById(R.id.getFromGps);

        geoCode = new GeoCode(getContext());


        //supportMapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        supportMapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapfragment);
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(23.727358, 90.389717))      // Sets the center of the map to Mountain View
                .zoom(7)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.727358, 90.389717), 7 ));
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        });


        // create a new instance
        mMeteor = new Meteor(getContext(), "ws://52.175.255.59/websocket", new InMemoryDatabase());

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the connection
        mMeteor.connect();


//        gpsEnable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SingleShotLocationProvider.requestSingleUpdate(getContext(), new SingleShotLocationProvider.LocationCallback() {
//                    @Override
//                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
//                        Log.d("Location", location.toString());
//                        srcLatLng = String.valueOf(location.getLat()) + "," + String.valueOf(location.getLang());
//                        autoCompViewS.setText("Your location");
//                        gpsEnable.setBackgroundColor(R.color.telenorBlue);
//
//
//                    }
//                });
//            }
//        });



        close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                holder.setVisibility(View.INVISIBLE);
                openHolder.setVisibility(View.VISIBLE);


                return true;
            }
        });

        open.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.setVisibility(View.VISIBLE);
                openHolder.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        autoCompViewS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (autoCompViewS.getRight() - autoCompViewS.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        SingleShotLocationProvider.requestSingleUpdate(getContext(), new SingleShotLocationProvider.LocationCallback() {
                            @Override
                            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                                Log.d("Location", location.toString());
                                srcLatLng = String.valueOf(location.getLat()) + "," + String.valueOf(location.getLang());
                                autoCompViewS.setText("Your location");


                            }
                        });

                        return true;
                    }
                }
                return false;
            }
        });


        getRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                hideKeyboard();
                showpDialog();

                holder.setVisibility(View.INVISIBLE);
                openHolder.setVisibility(View.VISIBLE);

                //Toast.makeText(getContext(),autoCompViewD.getText().toString()+autoCompViewS.getText().toString(),Toast.LENGTH_SHORT).show();
                Log.d("check", getDesLatLng() + getSrcLatLng());


                tripPathShow = new TripPathShow(getContext(), getDesLatLng(), getSrcLatLng());

                Log.d("check", tripPathShow.getURL());

                tripPathShow.jsonReq(tripPathShow.getURL(), new TripPathShow.CallBack() {
                    @Override
                    public void onSuccess(List<LatLng> list) {
//                        Intent intent = new Intent(getContext(), ShowNearbyPlaces.class);
//                        intent.putExtra("locList", (Serializable) list);
//                        startActivity(intent);
                        pathList = list;
                        //supportMapFragment.getMapAsync(MapAPIfragment.this);

                        fetchData();
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });

            }
        });
        //supportMapFragment.getMapAsync(this);

        return v;
    }


    void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private ArrayList makeJsonObjectRequest(String input) {

        String urlJsonObj = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + input + "&types=establishment&location=23.726574,90.389868&radius=20000&strictbounds&key=AIzaSyAuDPbEB8OfpLi2aXcPa4KnTQyiuQurZ_Y\n";


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
            public void onSuccess(Double lat, Double lng) {
                Log.d("success", String.valueOf(lat) + "++++++-------+++++" + String.valueOf(lng));
                if (desLatLng == null) {
                    desLatLng = String.valueOf(lat) + "," + String.valueOf(lng);
                } else {
                    srcLatLng = String.valueOf(lat) + "," + String.valueOf(lng);
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
        //Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        if (statusList.size() > 0) {

            for (StatusObject statusObject : statusList) {
                LatLng latLng = new LatLng(statusObject.lat, statusObject.lng);
                googleMap.addMarker(new MarkerOptions().position(latLng)
                        .title(statusObject.level + " ")).showInfoWindow();
//            + statusObject.trafficVolume + " "
//                    + statusObject.averageSpeed
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }

        if (pathList.size() > 0) {
            final CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(pathList.get(1))      // Sets the center of the map to Mountain View// Sets the zoom// Sets the orientation of the camera to east
                    .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);


            Polyline line = googleMap.addPolyline(new PolylineOptions()
                    .addAll(pathList)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );


            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pathList.get(0), 15), 2000, null);

            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(1200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        hidepDialog();
                    }
                }
            };
            timerThread.start();



            //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.727358, 90.389717), 10), 2000, null);

        }
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

        if (collectionName.equals("Statuses")) {
            try {
                JSONObject jsonObject = new JSONObject(newValuesJson);
                String address = jsonObject.getString("address");
                double lat = jsonObject.getDouble("latitude");
                double lng = jsonObject.getDouble("longitude");
                String level = jsonObject.getString("level");
                String averageSpeed = jsonObject.getString("averageSpeed");
                String trafficVolume = jsonObject.getString("trafficVolume");
                String note = jsonObject.getString("note");
                String createdAt = jsonObject.getString("createdAt");
                String contactNo = jsonObject.getString("contactNo");
                if (!String.valueOf(lat).equals(null) && !String.valueOf(lng).equals(null)) {
                    statusList.add(new StatusObject(address, lat, lng, level, averageSpeed, trafficVolume
                            , note, createdAt, contactNo));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            supportMapFragment.getMapAsync(this);

        }


    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

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

    private void fetchData() {
        Log.d(TAG, "connection " + String.valueOf(mMeteor.isConnected()));
        if (mMeteor.isConnected()) {
            final String[] params = {};

            String subscriptionId = mMeteor.subscribe("statuses", params, new SubscribeListener() {

                @Override
                public void onSuccess() {
                    Log.d(TAG, params.toString());


                }

                @Override
                public void onError(String error, String reason, String details) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

                }
            });
        }


    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
           progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getContext(), "resumed", Toast.LENGTH_SHORT).show();
        desLatLng = null;
        srcLatLng = null;

    }
}
