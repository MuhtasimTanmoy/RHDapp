package com.example.t.roadsandhighway;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StatusSendFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText etTrafficVolume, etAverageSpeed, etNotes;
    private Spinner spnrTrafficLevel;
    private Button btnSubmit;
    private static String TAG = "statusSend";
    final List<String> categories = new ArrayList<String>();
    ComWithServer comWithServer;
    final Map<String, Object> values = new HashMap<String, Object>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.activity_status_send,container,false);

        etAverageSpeed = (EditText) v.findViewById(R.id.etAverageSpeed);
        etTrafficVolume = (EditText) v.findViewById(R.id.etTrafficVolume);
        etNotes = (EditText) v.findViewById(R.id.etNotes);
        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        spnrTrafficLevel = (Spinner) v.findViewById(R.id.spnrTrafficLevel);



        init();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Delivered", Toast.LENGTH_SHORT).show();


                if (comWithServer.isConnected()) {
                    values.put("averageSpeed", etAverageSpeed.getText().toString());
                    values.put("trafficVolume", etTrafficVolume.getText().toString());
                    values.put("note", etNotes.getText().toString());
                    values.put("filePath", null);
                    values.put("latitude", 22.4);
                    values.put("longitude", 90);
                    values.put("address", "BUET");
                    Object[] queryParams = {values};
                    comWithServer.callFucntion("statuses.insert", queryParams);

                }
            }
        });
        return v;
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        values.put("level", categories.get(position));
        Log.v(TAG, "" + position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void init() {

        comWithServer = new ComWithServer(getContext());



        // Spinner Drop down elements
        categories.add("TJ-1");
        categories.add("TJ-2");
        categories.add("TJ-3");
        categories.add("TJ-4");
        categories.add("TJ-5");
        categories.add("TJ-6");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnrTrafficLevel.setAdapter(dataAdapter);



        spnrTrafficLevel.setOnItemSelectedListener(this);

    }


}
