package com.example.t.roadsandhighway;

import android.content.Context;
import android.util.Log;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;

/**
 * Created by vacuum on 5/11/17.
 */

public class ComWithServer implements MeteorCallback {

    private Meteor mMeteor;
    private boolean success;

    ComWithServer(Context context){
        mMeteor = new Meteor(context, "ws://52.175.255.59/websocket");

        mMeteor.addCallback(this);

        mMeteor.connect();
    }


    public  boolean callFucntion(String fucnName, Object[] queryParams){
        success=false;
        mMeteor.call(fucnName,queryParams,new ResultListener() {

            @Override
            public void onSuccess(String result) {
                success=true;
            }

            @Override
            public void onError(String error, String reason, String details) {
                success=false;
            }
        });

        return success;
    }
    public boolean isConnected(){
        if (mMeteor.isConnected()){
            return true;
        }
        return false;
    }
    @Override
    public void onConnect(boolean signedInAutomatically) {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {

    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }
}
