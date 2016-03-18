package se.jolo.prototypenavigator.utils;

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
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import se.jolo.prototypenavigator.activities.Map;
import se.jolo.prototypenavigator.demo.MockLocationProvider;

/**
 * Created by Joel on 2016-02-25.
 */
public class Locator implements LocationListener {

    private static final String LOG_TAG = "LOCATOR";
    private static final String GPS_PROVIDER = LocationManager.GPS_PROVIDER;
    private static final String NETWORK_PROVIDER = LocationManager.NETWORK_PROVIDER;
    private static final int PERMISSIONS_LOCATION = 0;

    public static boolean isGpsEnabled = false;
    public static boolean ableToGetLocation = false;

    private static LocationManager locationManager;
    private Location location;

    private Context context;
    private Activity activity;

    private RouteManager routeManager;
    private MapView mapView;

    private MockLocationProvider mockLocationProvider;
    private Handler handler;
    private Runnable task;
    private boolean demoRunning = false;

    public Locator() {}

    public Locator(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /*********************************************************************************************/
    /****                                  MockLocation                                       ****/
    /*********************************************************************************************/

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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }

        mockLoop();
    }

    public void mockLoop() {
        handler = new Handler();
        task = new Runnable() {
            @Override
            public void run() {
                if (!routeManager.getPoints().isEmpty()) {
                    mockLocationProvider.pushLocation(routeManager.getPoints().get(1).getLatitude(),
                            routeManager.getPoints().get(1).getLongitude());
                }

                handler.postDelayed(task, 3000);
            }
        };

        if (!routeManager.getPoints().isEmpty()) {
            task.run();
            demoRunning = true;
        } else {
            handler.removeCallbacks(task);
            demoRunning = false;
        }
    }

    /*********************************************************************************************/
    /****                                 LocationListener                                    ****/
    /*********************************************************************************************/

    /**
     * Set new location, set camera to new location, check new locations proximity to next
     * stop-point, updates remaining stop-points, loads route again and removes and re-draws
     * polyline from location to next stop-point.
     *
     * @param location the new location
     */
    @Override
    public void onLocationChanged(Location location) {
        //setLocation((location != null) ? location : this.location);

        if (mapView != null && routeManager != null) {
            mapView.animateCamera(CameraUpdateFactory.newCameraPosition(
                    getCameraPosition(new LatLng(location.getLatitude(), location.getLongitude()))));

            routeManager.setCurrentLocation(location).checkStopPointProximity().updateStopPointsRemaining().loadPolylines();
            //routeManager.removePolyline(routeManager.getPolylineToNextStop());

            Log.d(LOG_TAG, "Location changed to ::: "
                    + location.getLatitude()
                    + " "
                    + location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Location", "Enabled location provider ::: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Location", "Disabled location provider ::: " + provider);
    }

    /*********************************************************************************************/
    /****                                     Location                                        ****/
    /*********************************************************************************************/

    /**
     * Initialize Locator setting LocationManager, LocationListener, checking permissions for
     * gps and network. If able, set last known location and boolean ableToGetLocation.
     */
    public void init() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = this;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        } else {
            locationManager.requestLocationUpdates(GPS_PROVIDER, 100, 5, locationListener);
            locationManager.requestLocationUpdates(NETWORK_PROVIDER, 100, 5, locationListener);
            location = locationManager.getLastKnownLocation(GPS_PROVIDER);

            if (location == null) {
                location = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
            }
        }

        ableToGetLocation = (location != null);
    }

    /**
     * Check with provider if GPS is enabled.
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Get location from GPS, if unable, get location from network.
     *
     * @return location
     */
    @SuppressWarnings("ResourceType")
    public Location getLocation() {
        Location newLocation =
                (locationManager.getLastKnownLocation(GPS_PROVIDER) != null)
                ?locationManager.getLastKnownLocation(GPS_PROVIDER)
                :locationManager.getLastKnownLocation(NETWORK_PROVIDER);

        return (newLocation != null) ? newLocation : location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /*********************************************************************************************/
    /****                                      Mapbox                                         ****/
    /*********************************************************************************************/
    @SuppressWarnings("ResourceType")
    public static void enableLocation(MapView mapView) {
        mapView.setMyLocationEnabled(true);
    }

    @SuppressWarnings("ResourceType")
    public static void toggleTracking(MapView mapView) {
        mapView.setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
    }

    /*********************************************************************************************/
    /****                                       Other                                         ****/
    /*********************************************************************************************/

    public void setMapViewAndRouteManager(MapView mapView, RouteManager routeManager) {
        this.mapView = mapView;
        this.routeManager = routeManager;
    }

    /**
     * Sets camera position to device bearing, if unable to get bearing set it to north.
     * Sets tilt and zoom.
     *
     * @param latLng current location
     * @return returns newly set CameraPosition
     */
    public CameraPosition getCameraPosition(LatLng latLng) {
        return new CameraPosition.Builder()
                .bearing(0.0f)
                .target(latLng)
                .tilt(80f)
                .zoom(15f)
                .build();
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
}
