package se.jolo.prototypenavigator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.views.MapView;

/**
 * Created by Joel on 2016-02-25.
 */
public class Locator implements LocationListener {

    private static final String LOG_TAG = "LOCATOR";
    private final static int PERMISSIONS_LOCATION = 0;
    private Location location;
    public static boolean isGpsEnabled = false;
    public static boolean allowInit = false;

    protected Locator() {}

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public Location getLocation() {
        return location;
    }

    public static MapView enableLocation(Context context, Activity activity, MapView mapView) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_LOCATION);
        } else {
            mapView.setMyLocationEnabled(true);
        }

        return mapView;
    }

    public static MapView toggleTracking(Context context, Activity activity, MapView mapView) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_LOCATION);
        } else {
            mapView.setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
        }

        return mapView;
    }
}
