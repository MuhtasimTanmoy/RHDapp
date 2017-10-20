package com.example.t.roadsandhighway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserTypeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private Spinner spnrUserType, spnrRHD;
    private Spinner[]  spnrRHDSub= new Spinner[7];
    private TextView[] tvRHDSub=new TextView[7];
    final List<UserType> categories = new ArrayList<UserType>();
    private TextView tvRHD;
    private EditText etName, etContact,etRegion;
    private Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        init();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void init() {
        spnrUserType = (Spinner) findViewById(R.id.spnrUsrTypeSpnr);
        tvRHD=(TextView) findViewById(R.id.tvRHD);
        tvRHD.setVisibility(View.GONE);
        spnrRHD=(Spinner) findViewById(R.id.spnrUsrTypeRHD);
        spnrRHD.setVisibility(View.GONE);
        spnrRHDSub[0]=(Spinner)findViewById(R.id.spnrUsrTypeCE);
        spnrRHDSub[1]=(Spinner)findViewById(R.id.spnrUsrTypeACE);
        spnrRHDSub[2]=(Spinner)findViewById(R.id.spnrUsrTypeSE);
        spnrRHDSub[3]=(Spinner)findViewById(R.id.spnrUsrTypeEXN);
        spnrRHDSub[4]=(Spinner)findViewById(R.id.spnrUsrTypeSDE);
        spnrRHDSub[5]=(Spinner)findViewById(R.id.spnrUsrTypeAE);
        spnrRHDSub[6]=(Spinner)findViewById(R.id.spnrUsrTypeSO);

        tvRHDSub[0]=(TextView) findViewById(R.id.tvCE);
        tvRHDSub[1]=(TextView) findViewById(R.id.tvACE);
        tvRHDSub[2]=(TextView) findViewById(R.id.tvSE);
        tvRHDSub[3]=(TextView) findViewById(R.id.tvEXN);
        tvRHDSub[4]=(TextView) findViewById(R.id.tvSDE);
        tvRHDSub[5]=(TextView) findViewById(R.id.tvAE);
        tvRHDSub[6]=(TextView) findViewById(R.id.tvSO);


        etName=(EditText)findViewById(R.id.etUserTypeName);
        etContact=(EditText)findViewById(R.id.etUserTypeContact);
        etRegion=(EditText)findViewById(R.id.etUserTypeRegion);

        btnAdd=(Button)findViewById(R.id.btnUserTypeAdd);


        etName.setVisibility(View.GONE);
        etContact.setVisibility(View.GONE);
        etRegion.setVisibility(View.GONE);
        btnAdd.setVisibility(View.GONE);




        for(int i=0;i<7;i++){
            spnrRHDSub[i].setVisibility(View.GONE);
            tvRHDSub[i].setVisibility(View.GONE);
        }




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
                spnrRHD.setVisibility(View.VISIBLE);
                tvRHD.setVisibility(View.VISIBLE);
                etName.setVisibility(View.VISIBLE);
                etContact.setVisibility(View.VISIBLE);
                etRegion.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.VISIBLE);


                List<String> types = new ArrayList<String>();

                types = new ArrayList<String>();
                types.add("CE");
                types.add("ACE");
                types.add("SE");
                types.add("EXN");
                types.add("SDE");
                types.add("AE");
                types.add("SO");
                types.add("Volunteer");

                ArrayAdapter<String> dataAdapter;
                dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, types);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnrRHD.setAdapter(dataAdapter);
                spnrRHD.setOnItemSelectedListener(this);



            }
        }
        if(parent==spnrRHD){
            for(int i=0;i<7;i++){
                if(i<=position){
                    tvRHDSub[i].setVisibility(View.VISIBLE);
                    spnrRHDSub[i].setVisibility(View.VISIBLE);
                }
                else{
                    tvRHDSub[i].setVisibility(View.GONE);
                    spnrRHDSub[i].setVisibility(View.GONE);
                }
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
