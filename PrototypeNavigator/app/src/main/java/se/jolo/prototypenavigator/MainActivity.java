package se.jolo.prototypenavigator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.model.StopPoint;
import se.jolo.prototypenavigator.utils.JsonMapper;
import se.jolo.prototypenavigator.model.Route;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = "MainActivity";
    private final static String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoicHJvdG90eXBldGVhbSIsImEiOiJjaWs2bXQ3Y3owMDRqd2JtMTZsdjhvbzVnIn0.NBH7u7RG-lqxGq_PEIjFjw";
    private MapView mapView = null;
    private DirectionsRoute currentRoute = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (loadRoute() == null) {
            showMessage("Failed to load route");
        } else {

            Route route = loadRoute();
            List<Waypoint> waypoints = loadWaypoints(route);

            // centroid goes here
            LatLng centroid = new LatLng(
                    (waypoints.get(0).getLatitude() + waypoints.get(waypoints.size() - 1).getLatitude()) / 2,
                    (waypoints.get(0).getLongitude() + waypoints.get(waypoints.size() - 1).getLongitude()) / 2);

            // initialize the mapView
            mapView = loadMap(savedInstanceState, centroid, waypoints);

            // get route from API
            getRoute(fewerWaypointsPlis(waypoints));
        }
    }

    public List<Waypoint> fewerWaypointsPlis(List<Waypoint> allWaypoints) {

        List<Waypoint> fewerWaypoints = new ArrayList<>();

        fewerWaypoints.add(allWaypoints.get(0));
        fewerWaypoints.add(allWaypoints.get(1));

        return fewerWaypoints;
    }

    private Route loadRoute() {

        JsonMapper jsonMapper = new JsonMapper(this);

        try {
            return jsonMapper.objectifyRoute();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
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

    private MapView loadMap(Bundle savedInstanceState, LatLng centroid, List<Waypoint> waypoints) {

        mapView = (MapView) findViewById(R.id.mapboxMapView);
        mapView.setAccessToken(MAPBOX_ACCESS_TOKEN);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.setCenterCoordinate(centroid);

        mapView.onCreate(savedInstanceState);

        mapView.setOnMapClickListener(new MapView.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                Waypoint target = new Waypoint(point.getLongitude(), point.getLatitude());
                checkOffRoute(target);
            }
        });

        for (Waypoint w : waypoints) {
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(w.getLatitude(), w.getLongitude())));
        }

        // set camera angle
        mapView.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(centroid, 16, 45, 0)));

        return mapView;
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
                Log.d(LOG_TAG, "Distance: " + currentRoute.getDistance());
                showMessage(String.format("Route is %d meters long.", currentRoute.getDistance()));

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