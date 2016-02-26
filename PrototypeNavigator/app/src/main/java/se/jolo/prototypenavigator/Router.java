package se.jolo.prototypenavigator;

import android.content.Context;
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
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;

/**
 * Created by Joel on 2016-02-24.
 */
public final class Router extends Locator {

    private static final String LOG_TAG = "ROUTER";
    private Context context;
    private MapView mapView;
    private String MAPBOX_ACCESS_TOKEN = "";
    private Location currentLocation;
    private DirectionsRoute currentRoute, fullRoute;
    private List<Waypoint> waypoints;
    private List<Waypoint> waypointsRemaining;
    private boolean inProximity;
    private PolylineOptions polylineToNextStop;

    public Router(Context context, MapView mapView, String MAPBOX_ACCESS_TOKEN) {
        this.context = context;
        this.mapView = mapView;
        this.MAPBOX_ACCESS_TOKEN = MAPBOX_ACCESS_TOKEN;
        this.inProximity = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        mapView.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition(
                        new LatLng(location.getLatitude(), location.getLongitude()), 13, 45, 0)));

        setCurrentLocation(location).loadRoute();
        removePolyline(getPolylineToNextStop());

        Toast.makeText(context, "calls made ::: " + CallCounter.getCounts(), Toast.LENGTH_LONG).show();
    }

    private List<Waypoint> fewerWaypoints() {

        List<Waypoint> fewerWaypoints = new ArrayList<>();

        fewerWaypoints.add(waypoints.get(0));
        fewerWaypoints.add(waypoints.get(1));
        fewerWaypoints.add(waypoints.get(2));
        fewerWaypoints.add(waypoints.get(3));
        fewerWaypoints.add(waypoints.get(4));

        return fewerWaypoints;
    }

    public Router loadWaypoints(Route route) {

        waypoints = new ArrayList<>();
        List<RouteItem> routeItems = route.getRouteItems();

        for (RouteItem ri : routeItems) {
            waypoints.add(new Waypoint(
                    ri.getStopPoint().getEasting(),
                    ri.getStopPoint().getNorthing()));  // altitude can be set as a third parameter
        }

        setWaypointsRemaining(waypoints);

        return this;
    }

    public Router updateWaypointsRemaining() {

        if (inProximity) {
            waypointsRemaining.remove(0);
            inProximity = false;
        }

        return this;
    }

    public Router checkWaypointProximity() {
        LatLng latLngWaypoint = new LatLng(
                waypointsRemaining.get(0).getLatitude(),
                waypointsRemaining.get(0).getLongitude());

        LatLng latLngPosition = new LatLng(
                currentLocation.getLatitude(),
                currentLocation.getLongitude());

        if (latLngWaypoint.distanceTo(latLngPosition) < 100) {
            Log.d(LOG_TAG, "you're within 100 meters of your next stop");
            inProximity = true;
        }

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
        }
    }

    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void checkOffRoute(Waypoint target) {
        if (fullRoute.isOffRoute(target)) {
            showMessage("You are off-route.");
        } else {
            showMessage("You are not off-route.");
        }
    }

    public void drawRoute(DirectionsRoute directionsRoute) {

        List<Waypoint> waypoints = directionsRoute.getGeometry().getWaypoints();

        LatLng[] point = new LatLng[waypoints.size()];
        for (int i = 0; i < waypoints.size(); i++) {
            point[i] = new LatLng(waypoints.get(i).getLatitude(),
                    waypoints.get(i).getLongitude());
        }

        if (waypoints.size() >= 3) {
            mapView.addPolyline(new PolylineOptions()
                    .add(point)
                    .color(Color.parseColor("#3887be"))
                    .width(5));
        } else {
            polylineToNextStop = new PolylineOptions()
                    .add(point)
                    .color(Color.parseColor("#ff0000"))
                    .width(5);
            mapView.addPolyline(polylineToNextStop);
        }
    }

    public boolean removePolyline(PolylineOptions polylineOptions) {

        boolean removed = false;

        if (polylineOptions != null) {
            mapView.removeAnnotation(polylineOptions.getPolyline());
            removed = true;

            return removed;
        }

        return removed;
    }

    public Location getLocation() {
        return Locator.getLocation(context);
    }

    public List<Waypoint> getCurrentRoute() {

        List<Waypoint> positionAndNextWaypoint = new ArrayList<>();

        positionAndNextWaypoint.add(new Waypoint(
                currentLocation.getLongitude(),
                currentLocation.getLatitude()));
        positionAndNextWaypoint.add(waypointsRemaining.get(0));

        return positionAndNextWaypoint;
    }

    public Router loadFullRoute() {

        MapboxDirections md = new MapboxDirections.Builder()
                .setAccessToken(MAPBOX_ACCESS_TOKEN)
                .setWaypoints(fewerWaypoints())
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                .build();

        md.enqueue(new Callback<DirectionsResponse>() {

            @Override
            public void onResponse(Response<DirectionsResponse> response, Retrofit retrofit) {
                CallCounter.count();

                printResponseMessage(response);

                fullRoute = response.body().getRoutes().get(0);

                showMessage(String.format("Route is %d meters long.", fullRoute.getDistance()));

                drawRoute(fullRoute);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "loadFullRoute-Error: " + t.getMessage());
                showMessage("loadFullRoute-Error: " + t.getMessage());
            }
        });

        return this;
    }

    public Router loadRoute() {

        List<Waypoint> waypoints = (currentRoute != null)
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

                currentRoute = response.body().getRoutes().get(0);

                checkWaypointProximity();

                drawRoute(currentRoute);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "loadRoute-Error: " + t.getMessage());
                showMessage("loadRoute-Error: " + t.getMessage());
            }
        });

        return this;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public PolylineOptions getPolylineToNextStop() {
        return polylineToNextStop;
    }

    public Router setWaypointsRemaining(List<Waypoint> waypoints) {
        waypointsRemaining = waypoints;

        return this;
    }

    public Router setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;

        return this;
    }
}
