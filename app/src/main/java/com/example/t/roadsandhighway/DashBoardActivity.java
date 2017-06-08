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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard);
        post_incident= (ImageButton) findViewById(R.id.POST_INCIDENT);

        post_incident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),StatusSendActivity.class);
                startActivity(intent);

            }
        });

    }
}
