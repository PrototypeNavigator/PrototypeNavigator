package se.jolo.prototypenavigator.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.views.MapView;

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

    public Locator() {}

    public Locator(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /*********************************************************************************************/
    /****                                 LocationListener                                    ****/
    /*********************************************************************************************/
    @Override
    public void onLocationChanged(Location location) {
        setLocation((location != null) ? location : this.location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

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
            locationManager.requestLocationUpdates(GPS_PROVIDER, 10, 5, locationListener);
            locationManager.requestLocationUpdates(NETWORK_PROVIDER, 10, 5, locationListener);
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
}
