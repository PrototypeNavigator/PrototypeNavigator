package se.jolo.prototypenavigator.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.directions.service.models.RouteStep;
import com.mapbox.directions.service.models.Waypoint;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.jolo.prototypenavigator.utils.Locator;
import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.utils.RouteManager;
import se.jolo.prototypenavigator.model.Route;

public class Map extends AppCompatActivity {

    private final static String LOG_TAG = "MapActivity";
    private final static String MAPBOX_ACCESS_TOKEN =
            "pk.eyJ1IjoicHJvdG90eXBldGVhbSIsImEiOiJjaWs2b" +
                    "XQ3Y3owMDRqd2JtMTZsdjhvbzVnIn0.NBH7u7RG-lqxGq_PEIjFjw";

    private FloatingActionButton findMeBtn;
    private TextView textView;

    private List<Waypoint> waypoints = null;
    private List<RouteStep> steps;
    private RouteManager routeManager;
    private MapView mapView;
    private Route route;

    private ViewGroup viewGroup;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Locator locator = new Locator(this, this);
        locator.init();

        Bundle extras = getIntent().getExtras();
        Loader loader = new Loader(this);

        mapView = loadMap(savedInstanceState);

        if (Locator.ableToGetLocation) {
            Locator.enableLocation(mapView);
            Locator.toggleTracking(mapView);
        } else {
            Toast.makeText(this, "Unable to acquire location. Please leave the "
                    + "woods/cave/cellar and/or the elevator", Toast.LENGTH_LONG).show();
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        textView = (TextView) findViewById(R.id.textTop);
        viewGroup = (ViewGroup) findViewById(R.id.textAndMenu);
        setSupportActionBar(myToolbar);

        findMeBtn = (FloatingActionButton) findViewById(R.id.findMeBtn);

        loadRoute(extras, loader);

        routeManager = new RouteManager(this, mapView, MAPBOX_ACCESS_TOKEN, locator, 
                textView, myToolbar);
        routeManager.loadRouteItemsAndWaypoints(route);

        if (locator.getLocation() == null) {
            Log.d(LOG_TAG, "just a test");
        } else {
            routeManager.setCurrentLocation(locator.getLocation()).loadRoute();
        }

        waypoints = routeManager.getWaypoints();

        LatLng centroid = (locator.getLocation() != null)
                ? new LatLng(locator.getLocation().getLatitude(), locator.getLocation().getLongitude())
                : new LatLng(waypoints.get(0).getLatitude(), waypoints.get(0).getLongitude());

        setCentroid(centroid);

        addMarkers(waypoints);

        findMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCamera(new LatLng(mapView.getLatLng()));

                if (Locator.ableToGetLocation) {
                    routeManager.onLocationChanged(routeManager.getLocation());
                }

                Toast.makeText(v.getContext(), "at: "
                                + routeManager.getNextStop().getOrder() + " "
                                + routeManager.getNextStop().getStopPoint().getType(),
                        Toast.LENGTH_LONG).show();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(viewGroup, new AutoTransition());
                toggleVisibility(textView);
            }
        });

        mapView.onCreate(savedInstanceState);
    }

    public void loadRoute(Bundle extras, Loader loader) {

        if (extras.get("uri") != null) {

            uri = (Uri) extras.get("uri");
            loader.execute(uri);

            try {
                route = loader.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        } else if (extras.get("str") != null) {

            try {
                route = loader.getPreLoadedRoute(extras.getString("str"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void toggleBearing() throws SecurityException {

        //mapView.setMyBearingTrackingMode(MyBearingTracking.COMPASS);
    }

    public void animateCamera(LatLng latLng) {
        mapView.animateCamera(CameraUpdateFactory.newCameraPosition(
                routeManager.getCameraPosition(latLng)));
    }

    private MapView loadMap(Bundle savedInstanceState) {

        mapView = (MapView) findViewById(R.id.mapboxMapView);
        mapView.setAccessToken(MAPBOX_ACCESS_TOKEN);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);

        mapView.setCompassGravity(Gravity.BOTTOM);
        mapView.setLogoVisibility(View.INVISIBLE);

        mapView.setOnMapClickListener(new MapView.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                Waypoint target = new Waypoint(point.getLongitude(), point.getLatitude());
                routeManager.checkOffRoute(target);
            }
        });

        return mapView;
    }

    private void addMarkers(List<Waypoint> waypoints) {
        for (Waypoint w : waypoints) {
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(w.getLatitude(), w.getLongitude())));
            Log.d("marker", w.toString());
        }
    }

    private void setCentroid(LatLng centroid) {
        mapView.setCenterCoordinate(centroid);
        animateCamera(centroid);
    }


    private static void toggleVisibility(View... views) {
        for (View view : views) {
            boolean isVisible = view.getVisibility() == View.VISIBLE;
            view.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
        }
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
                Intent sendToFileBrowser = new Intent(this, FileBrowser.class);
                startActivity(sendToFileBrowser);
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            case R.id.showDetails:
                Intent sendToDetails = new Intent(this, RouteListActivity.class);
                sendToDetails.putExtra("route", route);
                startActivity(sendToDetails);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
        toggleBearing();
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
        mapView.removeAllAnnotations();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "Destroy");
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
