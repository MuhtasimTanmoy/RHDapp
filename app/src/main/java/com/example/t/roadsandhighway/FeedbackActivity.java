package com.example.t.roadsandhighway;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by t on 6/9/17.
 */

public class FeedbackActivity extends AppCompatActivity {

    EditText feedback;
    Button buttonFeedback;
    ComWithServer comWithServer;
    final Map<String, Object> feedValues = new HashMap<String, Object>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback= (EditText) findViewById(R.id.etFeedback);
        buttonFeedback= (Button) findViewById(R.id.btnFeedback);
        comWithServer=new ComWithServer(getApplicationContext());

        buttonFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("connection",Boolean.toString(comWithServer.isConnected()));
                if (comWithServer.isConnected()) {
                    Toast.makeText(getApplicationContext(), "Delivered", Toast.LENGTH_SHORT).show();

                    feedValues.put("text", feedback.getText());

                    Object[] queryParams = {feedValues};

                    comWithServer.callFucntion("feedbacks.insert", queryParams);
            }
        }});




    }
}
