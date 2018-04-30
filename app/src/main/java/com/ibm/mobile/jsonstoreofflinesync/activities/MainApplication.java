package com.ibm.mobile.jsonstoreofflinesync.activities;

import android.app.Application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import android.util.Log;
import android.widget.Toast;

import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;

import com.worklight.wlclient.api.WLAccessTokenListener;
import com.worklight.wlclient.api.WLAuthorizationManager;
import com.worklight.wlclient.auth.AccessToken;
import com.ibm.mobile.jsonstoreofflinesync.manager.FlightAttendantManager;


/**
 * Created by abhilash_m on 02/03/18.
 */

//This class will be called on the start of the application
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        //Registering the network connectivity filter
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        super.onCreate();
        //Creating an instance of WLClient
        // Initialize the Mobile Foundation SDK and instantiate the Message Log.
        WLClient.createInstance(this);
        try {
            FlightAttendantManager.getInstance().initJsonStoreCollections(this);
        } catch (Exception e) {
            Log.d("Error:","error in initjsonstorecollections"+e.getStackTrace());
        }

    }

    //This object holds needed information about mobile network connectivity
    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == ConnectivityManager.CONNECTIVITY_ACTION) {
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                //If network connection is identified, then post a toast notification stating "Connected"
                if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    Log.d("Internet", "We have internet connection. Good to go.");
                    //An upstream sync is performed on connection
                    FlightAttendantManager.getInstance().upstreamsync();
                    Toast.makeText(getApplicationContext(), "Synced With Server ", Toast.LENGTH_SHORT).show();
                }
                //If network is disconnected, theN post a toast notification stating "Disconnected"
                else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                    Log.d("Internet", "We have lost internet connection");
                    Toast.makeText(getApplicationContext(), "Disconnected ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

}
