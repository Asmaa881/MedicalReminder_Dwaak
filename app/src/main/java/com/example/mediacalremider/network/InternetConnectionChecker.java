package com.example.mediacalremider.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetConnectionChecker {

    private Context context;

    public InternetConnectionChecker(Context context) {
        this.context = context;
    }

    public boolean isConnected(){
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()){
            Log.i("con","connected : "+ connected);
            return connected;
        }
        return connected;
    }

}
