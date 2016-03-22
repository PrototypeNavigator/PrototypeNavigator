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
import android.util.Log;

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

    private Location location;
    private Location lastLocation;

    private Context context;
    private Activity activity;

    public Locator() {
    }

    public Locator(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
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
        lastLocation = this.location;
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

    /*********************************************************************************************/
    /****                                     Location                                        ****/
    /*********************************************************************************************/

    /**
     * Initialize Locator setting LocationManager, LocationListener, checking permissions for
     * gps and network. If able, set last known location and boolean ableToGetLocation.
     */
    public void init(LocationListener locationListener) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationListener != this) {
            locationManager.removeUpdates(this);
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        }

        locationManager.requestLocationUpdates(GPS_PROVIDER, 1000, 5, locationListener);
        location = locationManager.getLastKnownLocation(GPS_PROVIDER);

        if (location == null) {
            locationManager.requestLocationUpdates(NETWORK_PROVIDER, 1000, 5, locationListener);
            location = locationManager.getLastKnownLocation(GPS_PROVIDER);
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
    public Location getLocation() {

        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        }

        return (location != null)
                ? location
                : (locationManager.getLastKnownLocation(GPS_PROVIDER) != null)
                        ? locationManager.getLastKnownLocation(GPS_PROVIDER)
                        : locationManager.getLastKnownLocation(NETWORK_PROVIDER);
    }

    public void setLocation(Location location) {
        lastLocation = this.location;
        this.location = location;
    }
}
