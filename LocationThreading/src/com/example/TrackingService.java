package com.example;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.*;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: Jim
 * Date: 1/22/13
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrackingService extends Service implements Handler.Callback{
    final static String LOGTAG = "Location Monitoring";

    //Start tracking service
    public final static String ACTION_START_MONITORING = "com.pluralsight.START_MONITORING";
    //Stop tracking service
    public final static String ACTION_STOP_MONITORING = "com.pluralsight.STOP_MONITORING";
    private final static String HANDLER_THREAD_NAME = "MyLocationThread";

    LocationListener _listener;
    Looper _looper;
    android.os.Handler _handler;

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread(HANDLER_THREAD_NAME);
        thread.start();

        _looper = thread.getLooper();
        _handler = new Handler(_looper, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        doStopTracking();

        if(_looper != null)
            _looper.quit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String threadId = LogHelper.threadId();
        Log.d(LOGTAG, "Location Monitoring Service onStartCommand - " + threadId);

        _handler.sendMessage(_handler.obtainMessage(0, intent));
        return START_STICKY;
    }

    public boolean handleMessage(Message message) {
        String threadId = LogHelper.threadId();
        Log.d(LOGTAG, "Location Monitoring Service onStartCommand - " + threadId);

        Intent intent = (Intent) message.obj;

        String action = intent.getAction();
        Log.d(LOGTAG, "Location Service onStartCommand Action:" + action);

        if(action.equals(ACTION_START_MONITORING)) {
            doStartTracking();
        }
        else if(action.equals(ACTION_STOP_MONITORING)) {
            doStopTracking();
            stopSelf();
        }

        return true;
    }

    private void doStartTracking() {
        doStopTracking();

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        _listener = new MyServiceLocationListener();
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, _listener, _looper);

    }

    private void doStopTracking() {
        if (_listener != null) {
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            lm.removeUpdates(_listener);
            _listener = null;
        }

    }

    public IBinder onBind(Intent intent) {
        return null;
    }

}
