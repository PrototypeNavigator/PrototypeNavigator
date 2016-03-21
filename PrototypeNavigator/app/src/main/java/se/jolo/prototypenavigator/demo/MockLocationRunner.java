package se.jolo.prototypenavigator.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

/**
 * Created by super on 2016-03-21.
 */
public final class MockLocationRunner implements LocationListener {

    private static final String LOG_TAG = "MockLocation";

    private Context context;
    private Activity activity;

    private MockLocationProvider mockLocationProvider;
    private Handler handler;
    private Runnable task;
    private boolean demoRunning = false;

    private List<LatLng> points;
    private Location location;

    public MockLocationRunner(Context context, Activity activity, List<LatLng> points) {
        this.context = context;
        this.activity = activity;
        this.points = points;
    }

    public void mockLocation() {

        if ((activity.getApplication().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {

            mockLocationProvider = new MockLocationProvider(LocationManager.GPS_PROVIDER, context);

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5, this);
            }
        }

        mockLoop();
    }

    public void mockLoop() {
        handler = new Handler();
        task = new Runnable() {
            @Override
            public void run() {
                if (!points.isEmpty()) {
                    for (LatLng point : points) {
                        if ((location.getLatitude() == point.getLatitude()) &&
                                (location.getLongitude() == point.getLongitude())) {
                            points.remove(0);
                        }
                    }

                    mockLocationProvider.pushLocation(points.get(0).getLatitude(),
                            points.get(0).getLongitude());
                }

                handler.postDelayed(task, 100);
            }
        };

        if (!points.isEmpty()) {
            task.run();
            demoRunning = true;
        } else {
            handler.removeCallbacks(task);
            demoRunning = false;
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public Runnable getTask() {
        return task;
    }

    public boolean isDemoRunning() {
        return demoRunning;
    }

    public void setDemoRunning(boolean demoRunning) {
        this.demoRunning = demoRunning;
    }

    public MockLocationProvider getMockLocationProvider() {
        return mockLocationProvider;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(LOG_TAG, "Status changed to ::: " + status + " for provider ::: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(LOG_TAG, "Enabled location provider ::: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(LOG_TAG, "Disabled location provider ::: " + provider);
    }

    public void kill() {
        if (task != null) {
            handler.removeCallbacks(task);
        }

        demoRunning = false;
        mockLocationProvider.shutdown();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
