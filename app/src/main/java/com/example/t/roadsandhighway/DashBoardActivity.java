package com.example.t.roadsandhighway;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by t on 6/8/17.
 */

public class DashBoardActivity extends AppCompatActivity {

    ImageButton post_incident;
    ImageButton eta;
    ImageButton forecast;
    ImageButton recent_incident;
    ImageButton near_me;
    ImageButton alternative_route;
    ImageButton benefits;
    ImageButton award;
    ImageButton emergency_call;
    ImageButton feedback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard);
        init();


    }


    void init() {
        post_incident = (ImageButton) findViewById(R.id.POST_INCIDENT);

        post_incident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatusSendActivity.class);
                startActivity(intent);

            }

        });

        eta = (ImageButton) findViewById(R.id.ETA);

        eta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LiveNavActivity.class);
                startActivity(intent);

            }

        });

        benefits = (ImageButton) findViewById(R.id.BENEFITS);

        benefits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);

            }

        });

    }

}
