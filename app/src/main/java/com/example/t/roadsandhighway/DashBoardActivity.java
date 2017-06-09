package com.example.t.roadsandhighway;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.t.roadsandhighway.Activity.NearByPlaces;

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

        near_me= (ImageButton) findViewById(R.id.NEAR_ME);

        near_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();



//                Intent intent = new Intent(getApplicationContext(), NearByPlaces.class);
//                startActivity(intent);
            }
        });

        alternative_route= (ImageButton) findViewById(R.id.ALTERNATIVE_ROUTE);

        alternative_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MasterRouteActivity.class);
                startActivity(intent);
            }
        });

    }


    void initDialog(){

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.search_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final Button setHospital = (Button) mView.findViewById(R.id.setHospital);
        final TextView searchFor= (TextView) mView.findViewById(R.id.searchForPlaceText);

        final Button setRestaurent = (Button) mView.findViewById(R.id.setRestaurent);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        String message=searchFor.getText().toString();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }

}
