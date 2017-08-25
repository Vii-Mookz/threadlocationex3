package com.example;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA.
 * User: Jim
 * Date: 1/21/13
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrackingActivity extends Activity {
    final static String LOGTAG = "Location Monitoring";

    Looper _looper;
    MyActivityLocationListener _locationListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_activity);

        LocationState state = (LocationState) getLastNonConfigurationInstance();
        if (state != null) {
            _looper = state.getLooper();
            _locationListener = state.getLocationListener();

            if(_locationListener != null)
                _locationListener.setActivity(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater() ;
        inflater.inflate(R.menu.tracking_menu, menu) ;
        return true;
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return new LocationState(_looper, _locationListener);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.menuStartTracking:
                onMenuStartTracking(item);
                break;
            case R.id.menuStopTracking:
                onMenuStopTracking(item);
                break;
        }

        return true;
    }

    private void onMenuStartTracking(MenuItem item) {
        doStartTracking();
    }

    private void onMenuStopTracking(MenuItem item) {
        doStopTracking();
    }

    public void setLocation(Location location) {
        Log.d(LOGTAG, "Location Monitoring Activity setLocation - " + LogHelper.threadId());

        String latitudeString = String.format("%.6f", location.getLatitude());
        String longitudeString = String.format("%.6f", location.getLongitude());

        try{
            TextView latitudeView = (TextView) findViewById(R.id.textViewLatitude);
            TextView longitudeView = (TextView) findViewById(R.id.textViewLongitude);

            latitudeView.setText(latitudeString);
            longitudeView.setText(longitudeString);
        }
        catch (Exception ex) {
            Log.e(MyActivity.LOGTAG, "Location Monitor setLocation Exception", ex);
        }
    }

    private void doStartTracking() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        HandlerThread thread = new HandlerThread("ActivityLocThread");
        thread.start();
        _looper =thread.getLooper();

        _locationListener = new MyActivityLocationListener();
        _locationListener.setActivity(this);

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, _locationListener,
                _looper);

    }

    private void doStopTracking() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (_locationListener != null) {
            lm.removeUpdates(_locationListener);
            _locationListener = null;
        }

        if(_looper != null) {
            _looper.quit();
            _looper = null;
        }

    }

    class LocationState {
        Looper _looper;
        MyActivityLocationListener _locationListener;

        LocationState(Looper looper, MyActivityLocationListener locationListener) {
            _looper = looper;
            _locationListener = locationListener;
        }

        Looper getLooper() {
            return _looper;
        }

        MyActivityLocationListener getLocationListener() {
            return _locationListener;
        }
    }
}