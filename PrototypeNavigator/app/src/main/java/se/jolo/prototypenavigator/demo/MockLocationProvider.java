package se.jolo.prototypenavigator.demo;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by super on 2016-03-16.
 */
public final class MockLocationProvider {

    private String MOCK_PROVIDER;
    private Context context;

    public MockLocationProvider(String MOCK_PROVIDER, Context context) {
        this.MOCK_PROVIDER = MOCK_PROVIDER;
        this.context = context;

        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addTestProvider(MOCK_PROVIDER,
                false, false, false, false, false, true, true, 0, 5);
        locationManager.setTestProviderEnabled(MOCK_PROVIDER, true);
    }

    public void pushLocation(double lat, double lng) {

        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        Location location = new Location(MOCK_PROVIDER);
        location.setLatitude(lat);
        location.setLongitude(lng);
        location.setAltitude(0);
        location.setAccuracy(1.0f);
        location.setTime(System.currentTimeMillis());
        location.setElapsedRealtimeNanos(System.currentTimeMillis());

        locationManager.setTestProviderLocation(MOCK_PROVIDER, location);
    }

    public void shutdown() {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        locationManager.removeTestProvider(MOCK_PROVIDER);
    }
}
