package se.jolo.prototypenavigator;

import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.directions.DirectionsCriteria;
import com.mapbox.directions.MapboxDirections;
import com.mapbox.directions.service.models.DirectionsResponse;
import com.mapbox.directions.service.models.DirectionsRoute;
import com.mapbox.directions.service.models.Waypoint;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Joel on 2016-02-24.
 */
public final class Router {

    private static final String LOG_TAG = "ROUTER";
    private MapView mapView;
    private String MAPBOX_ACCESS_TOKEN = "";
    private Location currentLocation;
    private DirectionsRoute directionsRoute;
    private List<Waypoint> waypointsRemaining;
    private boolean inProximity;

    public Router(MapView mapView, String MAPBOX_ACCESS_TOKEN) {
        this.mapView = mapView;
        this.MAPBOX_ACCESS_TOKEN = MAPBOX_ACCESS_TOKEN;
        this.inProximity = false;
    }

    public Router updateWaypointsRemaining() {

        if (inProximity) {
            waypointsRemaining.remove(0);
            inProximity = false;
        }

        return this;
    }

    public Router setWaypoints(List<Waypoint> waypoints) {
        waypointsRemaining = waypoints;
        return this;
    }

    public List<Waypoint> getCurrentRoute() {

        List<Waypoint> positionAndNextWaypoint = new ArrayList<>();

        positionAndNextWaypoint.add(new Waypoint(currentLocation.getLongitude(), currentLocation.getLatitude()));
        positionAndNextWaypoint.add(waypointsRemaining.get(0));

        return positionAndNextWaypoint;
    }

    public Router checkWaypointProximity() {

        if (directionsRoute.getDistance() <= 0.05) {
            Log.d(LOG_TAG, "you're within 0.05 mile of the next stop");
            inProximity = true;
        }

        return this;
    }

    public void drawRoute(DirectionsRoute currentRoute, String color) {

        List<Waypoint> waypoints = currentRoute.getGeometry().getWaypoints();

        LatLng[] point = new LatLng[waypoints.size()];
        for (int i = 0; i < waypoints.size(); i++) {
            point[i] = new LatLng(waypoints.get(i).getLatitude(),
                                  waypoints.get(i).getLongitude());
        }

        mapView.addPolyline(new PolylineOptions()
                .add(point)
                .color(Color.parseColor(color))
                .width(5));
    }

    public Router getRoute() {

        List<Waypoint> waypoints = (directionsRoute != null)
                ? checkWaypointProximity().updateWaypointsRemaining().getCurrentRoute()
                : updateWaypointsRemaining().getCurrentRoute();

        MapboxDirections md = new MapboxDirections.Builder()
                .setAccessToken(MAPBOX_ACCESS_TOKEN)
                .setWaypoints(waypoints)
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                .build();

        md.enqueue(new Callback<DirectionsResponse>() {

            @Override
            public void onResponse(Response<DirectionsResponse> response, Retrofit retrofit) {
                CallCounter.count();

                printResponseMessage(response);

                directionsRoute = response.body().getRoutes().get(0);
                checkWaypointProximity();

                drawRoute(directionsRoute, "#ff0000");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "getRoute-Error: " + t.getMessage());
            }
        });

        return this;
    }

    private void printResponseMessage(Response<DirectionsResponse> response) {
        if (!response.isSuccess()) {
            try {
                Log.d(LOG_TAG, "Error: " + response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(LOG_TAG, "Response code: " + response.code());
            Log.d(LOG_TAG, "content-type: " + response.raw().header("Content-Type"));
        }
    }

    public MapView getMapView() {
        return mapView;
    }

    public Router setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        return this;
    }
}
