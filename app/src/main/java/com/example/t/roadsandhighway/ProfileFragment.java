package com.example.t.roadsandhighway;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by t on 5/14/17.
 */

public class ProfileFragment extends Fragment {

    private Button btnSignOut;
    private DbHelper dbHelper ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile,container,false);
        init(v);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "wow", Toast.LENGTH_SHORT).show();
                dbHelper.updateStatus(1,"false");
                Intent intent = new Intent(getActivity(), SignIn.class);
                startActivity(intent);
            }
        });
        return v;
    }



    private void init(View v) {

        btnSignOut=(Button) v.findViewById(R.id.btnProfileSignOut);
        dbHelper=new DbHelper(getActivity());
    }
}
