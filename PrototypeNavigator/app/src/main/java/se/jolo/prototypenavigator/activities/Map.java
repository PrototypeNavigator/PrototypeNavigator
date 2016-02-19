package se.jolo.prototypenavigator.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mapbox.directions.DirectionsCriteria;
import com.mapbox.directions.MapboxDirections;
import com.mapbox.directions.service.models.DirectionsResponse;
import com.mapbox.directions.service.models.DirectionsRoute;
import com.mapbox.directions.service.models.Waypoint;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;

public class Map extends AppCompatActivity implements LocationListener {

    private final static String LOG_TAG = "MapActivity";
    private final static String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoicHJvdG90eXBldGVhbSIsImEiOiJjaWs2bXQ3Y3owMDRqd2JtMTZsdjhvbzVnIn0.NBH7u7RG-lqxGq_PEIjFjw";
    private final static int PERMISSIONS_LOCATION = 0;
    private MapView mapView;
    private FloatingActionButton findMeBtn;
    private static LocationServices locationService;
    private DirectionsRoute currentRoute = null;
    private List<Waypoint> waypoints = null;
    private Uri uri;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        Loader loader = new Loader(this);

        findMeBtn = (FloatingActionButton) findViewById(R.id.findMeBtn);
        mapView = loadMap(savedInstanceState);
        uri = (Uri) extras.get("uri");
        locationService = LocationServices.getLocationServices(this);

        loader.execute(uri);
        try {
            route = loader.get();
            Log.d(LOG_TAG, "in Map " + route.getUuid());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_LOCATION);
        } else {
            mapView.setMyLocationEnabled(true);
        }

        waypoints = loadWaypoints(route);

        // centroid goes here
        LatLng centroid = new LatLng(
                (waypoints.get(0).getLatitude() + waypoints.get(waypoints.size() - 1).getLatitude()) / 2,
                (waypoints.get(0).getLongitude() + waypoints.get(waypoints.size() - 1).getLongitude()) / 2);

        setCentroid(centroid);

        addMarkers(waypoints);

        // get route from API
        getRoute(fewerWaypointsPlis(waypoints));

        findMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCamera(new LatLng(mapView.getLatLng()));
                toggleTracking();
            }
        });

        mapView.onCreate(savedInstanceState);
    }

    public void toggleTracking() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_LOCATION);
        } else {
            mapView.setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
        }
    }

    public void animateCamera(LatLng latLng) {
        mapView.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 16, 45, 0)));
    }

    public List<Waypoint> fewerWaypointsPlis(List<Waypoint> allWaypoints) {

        List<Waypoint> fewerWaypoints = new ArrayList<>();

        fewerWaypoints.add(allWaypoints.get(0));
        fewerWaypoints.add(allWaypoints.get(1));

        return fewerWaypoints;
    }

    private List<Waypoint> loadWaypoints(Route route) {

        List<Waypoint> waypoints = new ArrayList<>(); // kanske går med en HashMap för Order...
        List<RouteItem> routeItems = route.getRouteItems();

        

        for (RouteItem ri : routeItems) {
            waypoints.add(new Waypoint(
                    ri.getStopPoint().getEasting(),
                    ri.getStopPoint().getNorthing()));  // altitude can be set as a third parameter
        }

        return waypoints;
    }

    private MapView loadMap(Bundle savedInstanceState) {

        mapView = (MapView) findViewById(R.id.mapboxMapView);
        mapView.setAccessToken(MAPBOX_ACCESS_TOKEN);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);

        mapView.onCreate(savedInstanceState);

        mapView.setOnMapClickListener(new MapView.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                Waypoint target = new Waypoint(point.getLongitude(), point.getLatitude());
                checkOffRoute(target);
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

    private void getRoute(List<Waypoint> waypoints) {
        MapboxDirections md = new MapboxDirections.Builder()
                .setAccessToken(MAPBOX_ACCESS_TOKEN)
                .setWaypoints(waypoints)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .build();

        md.enqueue(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Response<DirectionsResponse> response, Retrofit retrofit) {

                // You can get generic HTTP info about the response
                if (!response.isSuccess()) {
                    try {
                        Log.d(LOG_TAG, "Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(LOG_TAG, "Response code: " + response.code());
                }

                // Print some info about the route
                currentRoute = response.body().getRoutes().get(0);
                //Log.d(LOG_TAG, "Distance: " + currentRoute.getDistance());
                //showMessage(String.format("Route is %d meters long.", currentRoute.getDistance()));

                // Draw the route on the map
                drawRoute(currentRoute);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "Error: " + t.getMessage());
                showMessage("Error: " + t.getMessage());
            }
        });
    }

    private void drawRoute(DirectionsRoute route) {
        // Convert List<Waypoint> into LatLng[]
        List<Waypoint> waypoints = route.getGeometry().getWaypoints();
        LatLng[] point = new LatLng[waypoints.size()];
        for (int i = 0; i < waypoints.size(); i++) {
            point[i] = new LatLng(
                    waypoints.get(i).getLatitude(),
                    waypoints.get(i).getLongitude());
        }

        // Draw Points on MapView
        mapView.addPolyline(new PolylineOptions()
                .add(point)
                .color(Color.parseColor("#3887be"))
                .width(5));

    }

    private void checkOffRoute(Waypoint target) {
        if (currentRoute.isOffRoute(target)) {
            showMessage("You are off-route.");
        } else {
            showMessage("You are not off-route.");
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        animateCamera(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
