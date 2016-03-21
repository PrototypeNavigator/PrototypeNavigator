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

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.List;

/**
 * Created by super on 2016-03-21.
 */
public final class MockLocationRunner implements LocationListener {

    private static final String LOG_TAG = "MockLocation";

    private Context context;
    private Activity activity;
    private MapView mapView;

    private MockLocationProvider mockLocationProvider;
    private Handler handler;
    private Runnable task;
    private boolean demoRunning = false;

    private List<LatLng> points;
    private Location location;

    public MockLocationRunner(Context context, Activity activity, List<LatLng> points, MapView mapView) {
        this.context = context;
        this.activity = activity;
        this.points = points;
        this.mapView = mapView;

        Log.d(LOG_TAG, LOG_TAG + " created!");
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
                    for (LatLng point : points) { // ConcurrentModificationException
                        if (location != null) {
                            if ((location.getLatitude() == point.getLatitude()) &&
                                    (location.getLongitude() == point.getLongitude())) {
                                points.remove(0);
                            }
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
            Log.d(LOG_TAG, "MockLoop ::: run()");
            demoRunning = true;
        } else {
            handler.removeCallbacks(task);
            Log.d(LOG_TAG, "MockLoop ::: removeCallbacks()");
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

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

        Log.d(LOG_TAG, "OLC - LatLng ::: " + location.getLatitude() + ", " + location.getLongitude());

        mapView.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .build()));
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
            Log.d(LOG_TAG, "kill() ::: removeCallbacks()");
        }

        demoRunning = false;
        mockLocationProvider.shutdown();
        Log.d(LOG_TAG, "kill() ::: shutdown()");
        android.os.Process.killProcess(android.os.Process.myPid());
        Log.d(LOG_TAG, "kill() ::: killProcess()");
    }
}
