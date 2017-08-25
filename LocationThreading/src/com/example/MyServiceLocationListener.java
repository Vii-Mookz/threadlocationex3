package com.example;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: Jim
 * Date: 1/22/13
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyServiceLocationListener implements LocationListener{
    final static String LOGTAG = "Location Monitoring";

    public void onLocationChanged(Location location) {
        String threadId = LogHelper.threadId();
        Log.d(LOGTAG, "Location Monitoring Service onLocationChanged - " + threadId);

        String logMsg = LogHelper.formatLocationInfo(location);
        Log.d(LOGTAG, "Location Monitoring - " + logMsg);
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    public void onProviderEnabled(String s) {
    }

    public void onProviderDisabled(String s) {
    }
}
