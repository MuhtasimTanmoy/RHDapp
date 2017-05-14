package com.example.t.roadsandhighway;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by t on 5/14/17.
 */

public class Home extends AppCompatActivity {

    BottomBar bottomBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        bottomBar= (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            Fragment fragment;
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if(tabId==R.id.tab_maps){
                    //fragment=new MapFragment();
                }
                else if(tabId==R.id.tab_info){
                    //fragment=new MapFragment();
//                    fragment= new StatusSend();

                }
                else if (tabId==R.id.tab_send){
                    fragment=new StatusSendFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,fragment).commit();

            }
        });




    }
}
