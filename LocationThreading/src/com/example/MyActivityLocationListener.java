package com.example;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: Jim
 * Date: 1/21/13
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyActivityLocationListener implements LocationListener{
    final static String LOGTAG = "Location Monitoring";
    TrackingActivity _activity;

    public void setActivity(TrackingActivity activity) {
        _activity = activity;
    }

    public void onLocationChanged(Location location) {
        Log.d(LOGTAG, "Location Monitoring Activity onLocationChanged - " + LogHelper.threadId());

        final Location theLocation = location;
        _activity.runOnUiThread(new Runnable() {
            public void run() {
                _activity.setLocation(theLocation);
            }
        });

    }

    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    public void onProviderEnabled(String s) {
    }

    public void onProviderDisabled(String s) {
    }
}
