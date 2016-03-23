package se.jolo.prototypenavigator.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.IOException;
import java.util.List;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.demo.MockLocationProvider;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.utils.Locator;
import se.jolo.prototypenavigator.utils.RouteManager;

public class MapDemo extends AppCompatActivity implements LocationListener {

    private static final String LOG_TAG = "MapDemo";
    private final static String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoicHJvdG90eXBldGVhbSIsImEiOiJjaWs2bXQ3Y3owMDRqd2JtMTZsdjhvbzVnIn0.NBH7u7RG-lqxGq_PEIjFjw";

    private MapView mapView;
    private RouteManager routeManager;
    private Route route;

    private Handler handler;
    private Runnable task;
    private boolean running = false;

    private List<LatLng> points;
    private Location location;
    private MockLocationProvider mock;

    private FloatingActionButton killSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_demo);

        Bundle extras = getIntent().getExtras();
        Loader loader = new Loader(this);

        if (extras.get("str") != null) {
            try {
                route = loader.getPreLoadedRoute(extras.getString("str"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Locator locator = new Locator(this, this);
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(route.getRouteItems().get(0).getStopPoint().getNorthing());
        location.setLongitude(route.getRouteItems().get(0).getStopPoint().getEasting());
        locator.setLocation(location);

        routeManager = new RouteManager(this, locator);
        routeManager.loadRouteItemsAndWaypoints(route).loadPolylines();

        mapView = (MapView) findViewById(R.id.mapboxMapView2);
        mapView.setAccessToken(MAPBOX_ACCESS_TOKEN);

        mapView.setStyleUrl(Style.EMERALD);
        mapView.setCompassGravity(Gravity.BOTTOM);
        mapView.setLogoVisibility(View.INVISIBLE);
        mapView.removeView(mapView.getTouchables().get(1));

        mapView.addPolyline(routeManager.getPolylineToNextStop());

        killSwitch = (FloatingActionButton) findViewById(R.id.findMeBtn);
        killSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    kill();
                } else {
                    mockLoop();
                }
            }
        });

        addMarkers();

        points = routeManager.getPoints();

        initMockLocation(this);

        mapView.onCreate(savedInstanceState);
    }

    public void mockLoop() {

        handler = new Handler();
        task = new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "MockLoop ::: run()");
                if (!points.isEmpty()) {

                    points = routeManager.updatePolylineNextStop();

                    mock.pushLocation(points.get(0).getLatitude(),
                            points.get(0).getLongitude());

                    Log.d(LOG_TAG, "location pushed "
                            + points.get(0).getLatitude()
                            + " "
                            + points.get(0).getLongitude());
                }

                handler.postDelayed(task, 1000);
            }
        };

        if (!points.isEmpty()) {
            task.run();
            running = true;
        } else {
            handler.removeCallbacks(task);
            Log.d(LOG_TAG, "MockLoop ::: removeCallbacks()");
            running = false;
        }
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

    public void initMockLocation(LocationListener locationListener) {

        if ((getApplication().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {

            mock = new MockLocationProvider(LocationManager.GPS_PROVIDER, this);

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (locationListener != this) {
                locationManager.removeUpdates(locationListener);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
            }
        }
    }

    public void kill() {
        if (task != null) {
            handler.removeCallbacks(task);
            Log.d(LOG_TAG, "kill() ::: removeCallbacks()");
        }

        running = false;
        mock.shutdown();
        Log.d(LOG_TAG, "kill() ::: shutdown()");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void addMarkers() {
        IconFactory mIconFactory = IconFactory.getInstance(this);
        Drawable mIconDrawable = getScaledDrawable(25, 25, R.drawable.dot2);

        Icon icon = mIconFactory.fromDrawable(mIconDrawable);

        List<RouteItem> routeItems = routeManager.getRouteItems();
        for (int i = 0; i < routeItems.size(); i++) {
            RouteItem r = routeItems.get(i);
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(r.getStopPoint().getNorthing(), r.getStopPoint().getEasting()))
                    .icon(icon)
                    .title(routeItems.get(i).getStopPointItems().get(0).getDeliveryAddress()));

        }
    }

    private Drawable getScaledDrawable(int newWidth, int newHeight, int id) {

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), id);

        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(0);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return new BitmapDrawable(this.getResources(), scaledBitmap);
    }

    /*********************************************************************************************/
    /****                                     Lifecycle                                       ****/
    /*********************************************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "Start");
        mapView.onStart();
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "Resume");
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "Pause");
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "Stop");
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "Destroy");
        if (mock != null) {
            kill();
        }
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
