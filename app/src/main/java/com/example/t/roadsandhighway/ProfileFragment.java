package com.example.t.roadsandhighway;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by t on 5/14/17.
 */

public class ProfileFragment extends Fragment {

    private Button btnSignOut;
    private DbHelper dbHelper ;
    private TextView tvName, tvType, tvAddresss,tvContactNo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile,container,false);
        init(v);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "wow", Toast.LENGTH_SHORT).show();
                dbHelper.updateStatus(1,"false");
                dbHelper.deleteUserDetails(1);
                SignIn.mMeteor.logout();
                Intent intent = new Intent(getActivity(), SignIn.class);
                startActivity(intent);


            }
        });
        return v;
    }



    private void init(View v) {

        btnSignOut=(Button) v.findViewById(R.id.btnProfileSignOut);
        dbHelper=new DbHelper(getActivity());
        tvName=(TextView) v.findViewById(R.id.user_profile_name);
        tvType=(TextView) v.findViewById(R.id.user_profile_short_bio);
        tvAddresss=(TextView) v.findViewById(R.id.user_profile_address);
        tvContactNo=(TextView) v.findViewById(R.id.user_profile_contactNO);


        int row = dbHelper.numberOfRows("users");
        // Toast.makeText(getApplicationContext(), "  "+row , Toast.LENGTH_SHORT).show();
        if (row == 1) {
            Cursor res = dbHelper.getUserDetails(1);
            res.moveToFirst();
            String name = res.getString(res.getColumnIndex("name"));
            String type = res.getString(res.getColumnIndex("type"));
            String contactNo = res.getString(res.getColumnIndex("contactNo"));
            String address = res.getString(res.getColumnIndex("address"));

            tvName.setText("Name: "+name);
            tvType.setText("Type: "+type);
            tvAddresss.setText("Address: "+address);
            tvContactNo.setText("Contact No: "+contactNo);
        }
    }
}
