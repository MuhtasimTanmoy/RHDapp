package com.example.t.roadsandhighway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class StatusSend extends AppCompatActivity   implements AdapterView.OnItemSelectedListener {

    private EditText etTrafficVolume, etAverageSpeed, etNotes;
    private Spinner spnrTrafficLevel;
    private Button btnSubmit;
    private static String TAG = "statusSend";
    final List<String> categories = new ArrayList<String>();
    ComWithServer comWithServer;
    final Map<String, Object> values = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_send);


        init();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show();

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


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        values.put("level", categories.get(position));
        Log.v(TAG, "" + position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void init() {

        comWithServer = new ComWithServer(this);
        etAverageSpeed = (EditText) findViewById(R.id.etAverageSpeed);
        etTrafficVolume = (EditText) findViewById(R.id.etTrafficVolume);
        etNotes = (EditText) findViewById(R.id.etNotes);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        spnrTrafficLevel = (Spinner) findViewById(R.id.spnrTrafficLevel);


        // Spinner Drop down elements
        categories.add("TJ-1");
        categories.add("TJ-2");
        categories.add("TJ-3");
        categories.add("TJ-4");
        categories.add("TJ-5");
        categories.add("TJ-6");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnrTrafficLevel.setAdapter(dataAdapter);



        spnrTrafficLevel.setOnItemSelectedListener(this);

    }


}
