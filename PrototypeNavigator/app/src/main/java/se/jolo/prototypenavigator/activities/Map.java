package se.jolo.prototypenavigator.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.directions.service.models.Waypoint;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.service.MarkerService;
import se.jolo.prototypenavigator.singleton.RouteHolder;
import se.jolo.prototypenavigator.task.Loader;
import se.jolo.prototypenavigator.utils.Locator;
import se.jolo.prototypenavigator.utils.RouteManager;
import se.jolo.prototypenavigator.utils.Speech;

/* The Map class displays a map and all map related components. */
public class Map extends AppCompatActivity implements LocationListener {

    private final static String LOG_TAG = "MapActivity";
    private final static String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoicHJvdG90eXBldGVhbSIsImEiOiJjaWs2bXQ3Y3owMDRqd2JtMTZsdjhvbzVnIn0.NBH7u7RG-lqxGq_PEIjFjw";

    private FloatingActionButton findMeBtn;
    private TextView textViewInstruction, loadText;
    private ImageView loadImage = null;

    private Toolbar toolbar;
    private Button plus, minus;

    private Speech speech;

    private List<Waypoint> waypoints = null;
    private RouteManager routeManager;
    private MapView mapView;
    private LatLng centroid;
    private Route route;

    private static final int PERMISSIONS_LOCATION = 0;
    private Locator locator;
    private Location location;

    private ViewGroup viewGroup;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        RouteHolder.INSTANCE.setContext(this.getApplicationContext());

        Loader loader = new Loader(this);
        Bundle extras = getIntent().getExtras();

        loadRoute(extras, loader);

        locator = new Locator(this, this);
        locator.init(this);

        routeManager = loadManager(locator);

        toolbar = makeToolbar();
        viewGroup = makeViewGroup();
        textViewInstruction = makeTextView();
        mapView = loadMap();
        findMeBtn = initFindMeBtn();
        plus = initPlusBtn();
        minus = initMinusBtn();
        centroid = setCentroid(locator);
        initProgress();
        setSupportActionBar(toolbar);

        addMarkers();

        mapView.onCreate(savedInstanceState);
    }

    private void addMarkers() {

        if (RouteHolder.INSTANCE.getMarkers() == null
                || RouteHolder.INSTANCE.getMarkers().isEmpty()) {
            showProgerss();
            Intent markerServiceIntent = new Intent(this, MarkerService.class);
            this.startService(markerServiceIntent);
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    broadcastReceiver, new IntentFilter("markers"));
        } else {
            List<MarkerOptions> markers;
            showProgerss();
            markers = RouteHolder.INSTANCE.getMarkers();
            Log.d(LOG_TAG, markers.size() + " markers added");

            mapView.addMarkers(markers);
            hideProgress();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hideProgress();
            addMarkers();
        }
    };

    /*********************************************************************************************/
    /****                                   Location                                          ****/
    /*********************************************************************************************/

    /* Set new location, set camera to new location, check new locations proximity to next
     * stop-point, updates remaining stop-points, loads route again and removes and re-draws
     * polyline from location to next stop-point. */
    @Override
    public void onLocationChanged(Location location) {
        locator.setLocation(location);
        this.location = location;

        if (mapView != null && routeManager != null) {

            if (Locator.ableToGetLocation) {
                animateCamera(new LatLng(location.getLatitude(), location.getLongitude()));
                routeManager.checkStopPointProximity().updateStopPointsRemaining().loadPolylineNextStop();
                textViewInstruction.setText(routeManager.getInstruction().getReadableInstruction());
            }

            Log.d(LOG_TAG, "Location changed to ::: "
                    + location.getLatitude()
                    + " "
                    + location.getLongitude());
        }
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

    public void enableMapViewLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        }

        mapView.setMyLocationEnabled(true);
    }

    public void toggleMapViewTracking() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        }

        mapView.setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
    }

    /* Sets camera position to device bearing, if unable to get bearing set it to north.
     * Sets tilt and zoom. */
    public CameraPosition getCameraPosition(LatLng latLng) {
        return new CameraPosition.Builder()
                .bearing(0.0f)
                .target(latLng)
                .tilt(80f)
                .zoom(15f)
                .build();
    }

    /*********************************************************************************************/
    /****                                     Other                                           ****/
    /*********************************************************************************************/

    private ViewGroup makeViewGroup() {
        return (ViewGroup) findViewById(R.id.textAndMenu);
    }

    private void initProgress(){
        loadText = (TextView) findViewById(R.id.loadText);
        loadImage = (ImageView) findViewById(R.id.loadImage);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.together);
        loadImage.startAnimation(animation);
        loadImage.setVisibility(View.INVISIBLE);
        loadText.setVisibility(View.INVISIBLE);
    }
    private void showProgerss(){
            loadImage.setVisibility(View.VISIBLE);
            loadText.setVisibility(View.VISIBLE);
    }
    private void hideProgress(){
        loadImage.clearAnimation();
        loadImage.setVisibility(View.INVISIBLE);
        loadText.setVisibility(View.INVISIBLE);
    }


    private Toolbar makeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        return toolbar;
    }

    /* Initialize TextView and setting OnClickListener to toggle visibility. */
    private TextView makeTextView() {

        textViewInstruction = (TextView) findViewById(R.id.textTop);

        textViewInstruction.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(viewGroup, new AutoTransition());
                toggleVisibility(textViewInstruction);
                speech = new Speech(Map.this);
                speech.say(textViewInstruction.getText().toString());
            }
        });

        return textViewInstruction;
    }

    /* Initialize FloatingActionButton to call animateCamera() with at current location.
     * If able to get location calls onLocationChange() in RouteManager. */
    private FloatingActionButton initFindMeBtn() {

        findMeBtn = (FloatingActionButton) findViewById(R.id.findMeBtn);

        findMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCamera(new LatLng(locator.getLocation()));
            }
        });

        return findMeBtn;
    }

    /* Initialize plus button */
    private Button initPlusBtn() {

        plus = (Button) findViewById(R.id.plus);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double currentZoom = mapView.getZoom();
                mapView.setZoom(currentZoom + 1, true);
            }
        });
        return plus;
    }

    /* Initialize plus button */
    private Button initMinusBtn() {

        minus = (Button) findViewById(R.id.minus);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double currentZoom = mapView.getZoom();
                Log.d(LOG_TAG, mapView.getZoom() + "");
                Log.d("id", minus.getId() + "");
                mapView.setZoom(currentZoom - 1, true);
            }
        });
        return minus;
    }

    /* Toggles visibility of NextStep-view. */
    private static void toggleVisibility(View... views) {
        for (View view : views) {
            boolean isVisible = view.getVisibility() == View.VISIBLE;
            view.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
        }
    }

    /*********************************************************************************************/
    /****                                     Route                                           ****/
    /*********************************************************************************************/

    /* Initialize RouteManager, loading RouteItems and Waypoints. Setting current location.
     * Loads selected route. */
    private RouteManager loadManager(Locator locator) {
        routeManager = new RouteManager(this, locator);
        routeManager.loadRouteItemsAndWaypoints(route).loadPolylines();

        return routeManager;
    }

    /* Loads a newly selected Route from URI, or loads previously saved Route. */
    public void loadRoute(Bundle extras, Loader loader) {

        if (extras.get("uri") != null) {

            uri = (Uri) extras.get("uri");
            loader.execute(uri);

            try {
                route = loader.get();
                RouteHolder.INSTANCE.setRoute(route);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        } else if (extras.get("str") != null) {

            try {
                route = loader.getPreLoadedRoute(extras.getString("str"));
                RouteHolder.INSTANCE.setRoute(route);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*********************************************************************************************/
    /****                                      Map                                            ****/
    /*********************************************************************************************/

    /*Initializes the MapView setting values and location tracking if able.*/
    private MapView loadMap() {

        mapView = (MapView) findViewById(R.id.mapboxMapView);
        mapView.setAccessToken(MAPBOX_ACCESS_TOKEN);

        mapView.setStyleUrl(Style.MAPBOX_STREETS);

        mapView.setCompassGravity(Gravity.BOTTOM);
        mapView.setLogoVisibility(View.INVISIBLE);
        mapView.removeView(mapView.getTouchables().get(1));

        mapView.setOnMapClickListener(new MapView.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                Waypoint target = new Waypoint(point.getLongitude(), point.getLatitude());
            }
        });

        if (Locator.ableToGetLocation) {
            //Show the instructions if they are not visible.
            if (textViewInstruction.getVisibility() != View.VISIBLE) {
                toggleVisibility(textViewInstruction);
            }

            enableMapViewLocation();
            toggleMapViewTracking();

            mapView.addPolyline(routeManager.getPolylineToNextStop());
            textViewInstruction.setText(routeManager.getInstruction().getReadableInstruction());
        } else {
            Toast.makeText(this, "Unable to acquire location. Please leave the "
                    + "woods/cave/cellar and/or the elevator", Toast.LENGTH_LONG).show();

            mapView.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .target(new LatLng(locator.getLocation().getLatitude(),
                                    locator.getLocation().getLongitude()))
                            .tilt(0.0f)
                            .zoom(11f)
                            .build()));
            //Hide the instructions if they are visible.
            if (textViewInstruction.getVisibility() == View.VISIBLE) {
                toggleVisibility(textViewInstruction);
            }
        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(StringUtils.capitalize(routeManager.getNextStop().getStopPointItems().get(0).getDeliveryAddress().toLowerCase()));

        return mapView;
    }

    /*Set centroid to current location if able, else set it to next waypoint.*/
    private LatLng setCentroid(Locator locator) {
        LatLng centroid = new LatLng(locator.getLocation().getLatitude(), locator.getLocation().getLongitude());

        mapView.setCenterCoordinate(centroid);
        animateCamera(centroid);

        return centroid;
    }

    /*Set camera position, angle and zoom.*/
    public void animateCamera(LatLng latLng) {
        if (Locator.ableToGetLocation) {
            mapView.animateCamera(CameraUpdateFactory.newCameraPosition(
                    getCameraPosition(latLng, 80f, 15f)));
        } else {
            mapView.animateCamera(CameraUpdateFactory.newCameraPosition(
                    getCameraPosition(latLng, 0f, 14f)));
        }
    }

    /* Sets camera position to device bearing, if unable to get bearing set it to north.
     * Sets tilt and zoom.*/
    public CameraPosition getCameraPosition(LatLng latLng, float tilt, float zoom) {
        return new CameraPosition.Builder()
                .target(latLng)
                .tilt(tilt)
                .zoom(zoom)
                .build();
    }

    /*********************************************************************************************/
    /****                                      Menu                                           ****/
    /*********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loadRoute:
                Intent sendToFileBrowser = new Intent(this, Launcher.class);
                startActivity(sendToFileBrowser);
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            case R.id.showDetails:
                Intent sendToDetails = new Intent(this, RouteList.class);
                startActivity(sendToDetails);
                return true;
            case R.id.showRoute:
                PolylineOptions polylineOptions = routeManager.getPolylinefullRoute();

                if (item.getTitle().equals("Visa rutt på karta")) {
                    item.setTitle("Göm rutt");
                    mapView.addPolyline(polylineOptions);
                    return true;
                } else {
                    item.setTitle("Visa rutt på karta");
                    mapView.removeAnnotation(polylineOptions.getPolyline());
                    return true;
                }

            case R.id.dayVsNight:
                if (item.getTitle().equals("Visa mörk karta")) {
                    item.setTitle("Visa ljus karta");
                    mapView.setStyleUrl(Style.DARK);
                    return true;
                } else {
                    item.setTitle("Visa mörk karta");
                    mapView.setStyleUrl(Style.MAPBOX_STREETS);
                    return true;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    /*********************************************************************************************/
    /****                                     Lifecycle                                       ****/
    /*********************************************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver, new IntentFilter("markers"));
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
