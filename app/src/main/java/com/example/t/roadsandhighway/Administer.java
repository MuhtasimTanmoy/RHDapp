package com.example.t.roadsandhighway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.EventListener;

/**
 * Created by t on 5/14/17.
 */

public class Administer extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        button= (Button) findViewById(R.id.ministerButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Administer.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });



    }



    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
