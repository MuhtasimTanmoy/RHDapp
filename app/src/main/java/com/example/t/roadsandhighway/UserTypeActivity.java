package com.example.t.roadsandhighway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserTypeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private Spinner spnrUserType;
    final List<UserType> categories = new ArrayList<UserType>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        init();

    }

    private void init() {
        spnrUserType = (Spinner) findViewById(R.id.spnrUsrTypeSpnr);
        //add item in catagories
        categories.add(new UserType("passenger",1));
        categories.add(new UserType("police",2));
        categories.add(new UserType("BRTA",3));
        categories.add(new UserType("RHD",4));
        categories.add(new UserType("LGERD",5));
        categories.add(new UserType("DRIVER",6));
        categories.add(new UserType("Bus Superviser",7));
        categories.add(new UserType("Journalist",8));
        categories.add(new UserType("Ministry",9));
        categories.add(new UserType("Observer",10));
        categories.add(new UserType("Road user",11));

        //update list for array adapter
        List<String> types = new ArrayList<String>();
        for(UserType u: categories){
            types.add(u.getTypeName());
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrUserType.setAdapter(dataAdapter);
        spnrUserType.setOnItemSelectedListener(this);


    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent==spnrUserType){
            Toast.makeText(getApplicationContext(), "userType", Toast.LENGTH_SHORT).show();
            if(categories.get(position).typeName.equals("RHD")){
                Toast.makeText(getApplicationContext(), "RHD", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private class UserType{
        String typeName;
        int typeNumber;

        public UserType(String typeName, int typeNumber) {
            this.typeName = typeName;
            this.typeNumber = typeNumber;
        }

        public String getTypeName() {
            return typeName;
        }

        public int getTypeNumber() {
            return typeNumber;
        }
    }
}
