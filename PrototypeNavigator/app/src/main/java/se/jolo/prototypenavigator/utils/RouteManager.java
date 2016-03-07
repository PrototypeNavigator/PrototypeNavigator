package se.jolo.prototypenavigator.utils;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.mapbox.directions.DirectionsCriteria;
import com.mapbox.directions.MapboxDirections;
import com.mapbox.directions.service.models.DirectionsResponse;
import com.mapbox.directions.service.models.DirectionsRoute;
import com.mapbox.directions.service.models.RouteStep;
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
public final class RouteManager extends Locator {

    private static final String LOG_TAG = "ROUTER";
    private Context context;
    private MapView mapView;
    private String MAPBOX_ACCESS_TOKEN = "";

    private List<Waypoint> waypoints;
    private List<RouteItem> routeItems;
    private List<RouteStep> steps;

    private boolean inProximity;
    private Locator locator;
    private Location currentLocation;

    private DirectionsRoute currentRoute;
    private PolylineOptions polylineToNextStop;

    private TextView textView;
    private Toolbar toolbar;

    public RouteManager(Context context, MapView mapView, String MAPBOX_ACCESS_TOKEN,
                        Locator locator, TextView textView,Toolbar toolbar) {
        this.context = context;
        this.mapView = mapView;
        this.MAPBOX_ACCESS_TOKEN = MAPBOX_ACCESS_TOKEN;
        this.locator = locator;
        this.inProximity = false;
        this.textView = textView;
        this.toolbar = toolbar;
    }

    /*********************************************************************************************/
    /****                                     Location                                        ****/
    /*********************************************************************************************/
    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        mapView.animateCamera(CameraUpdateFactory.newCameraPosition(
                getCameraPosition(new LatLng(location.getLatitude(), location.getLongitude()))));

        setCurrentLocation(location).checkStopPointProximity().updateStopPointsRemaining().loadRoute();
        removePolyline(getPolylineToNextStop());

        Toast.makeText(context, "calls made ::: " + CallCounter.getCounts(), Toast.LENGTH_LONG).show();
    }

    public Location getLocation() {
        if (!Locator.ableToGetLocation) {
            currentLocation.setLatitude(routeItems.get(0).getStopPoint().getEasting());
            currentLocation.setLongitude(routeItems.get(0).getStopPoint().getNorthing());

            return currentLocation;
        }

        return locator.getLocation();
    }

    public RouteManager setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;

        return this;
    }

    /*********************************************************************************************/
    /****                                     drawing                                         ****/
    /*********************************************************************************************/
    public void drawRoute(DirectionsRoute directionsRoute) {

        List<Waypoint> waypoints = directionsRoute.getGeometry().getWaypoints();

        LatLng[] point = new LatLng[waypoints.size()];
        for (int i = 0; i < waypoints.size(); i++) {
            point[i] = new LatLng(waypoints.get(i).getLatitude(),
                    waypoints.get(i).getLongitude());
        }

        polylineToNextStop = new PolylineOptions()
                .add(point)
                .color(Color.parseColor("#ff0000"))
                .width(5);
        mapView.addPolyline(polylineToNextStop);
    }

    public boolean removePolyline(PolylineOptions polylineOptions) {

        if (polylineOptions != null) {
            mapView.removeAnnotation(polylineOptions.getPolyline());

            return true;
        }

        return false;
    }

    public PolylineOptions getPolylineToNextStop() {
        return polylineToNextStop;
    }

    public CameraPosition getCameraPosition(LatLng latLng) {
        return new CameraPosition.Builder()
                .bearing((steps != null) ? (float) steps.get(0).getHeading() : 0.0f)
                .target(latLng)
                .tilt(80f)
                .zoom(15f)
                .build();
    }

    /*********************************************************************************************/
    /****                                     Routing                                         ****/
    /*********************************************************************************************/
    public List<Waypoint> getCurrentRoute() {

        List<Waypoint> positionAndNextWaypoint = new ArrayList<>();

        positionAndNextWaypoint.add(new Waypoint(
                currentLocation.getLongitude(),
                currentLocation.getLatitude()));
        positionAndNextWaypoint.add(waypoints.get(0));

        return positionAndNextWaypoint;
    }

    public List<Waypoint> getNoLocationRoute() {

        List<Waypoint> noLocationRoute = new ArrayList<>();

        for (int i = 0; i < 13; i++) {
            noLocationRoute.add(waypoints.get(i));
        }

        return noLocationRoute;
    }

    public RouteManager loadRoute() {

        MapboxDirections md = new MapboxDirections.Builder()
                .setAccessToken(MAPBOX_ACCESS_TOKEN)
                .setWaypoints((Locator.ableToGetLocation) ? getCurrentRoute() : getNoLocationRoute()) // location check
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                .setSteps(true)
                .build();

        md.enqueue(new Callback<DirectionsResponse>() {

            @Override

            public void onResponse(Response<DirectionsResponse> response, Retrofit retrofit) {
                CallCounter.count();

                printResponseMessage(response);

                currentRoute = response.body().getRoutes().get(0);

                steps = currentRoute.getSteps();
                textView.setText(steps.get(0).getManeuver().getInstruction());
                toolbar.setTitle(routeItems.get(0).getStopPointItems().get(0).getDeliveryAddress());
                toolbar.setTitleTextColor(Color.WHITE);
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

    /*********************************************************************************************/
    /****                                    RoutItems                                        ****/
    /*********************************************************************************************/
    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public List<RouteItem> getRouteItems() {
        return routeItems;
    }

    public List<RouteStep> getSteps() {
        return steps;
    }

    public RouteItem getNextStop() {
        return routeItems.get(0);
    }

    public RouteManager loadRouteItemsAndWaypoints(Route route) {

        waypoints = new ArrayList<>();

        routeItems = new ArrayList<>();

        routeItems = route.getRouteItems();

        for (RouteItem ri : routeItems) {
            waypoints.add(new Waypoint(
                    ri.getStopPoint().getEasting(),
                    ri.getStopPoint().getNorthing()));
        }

        return this;
    }

    /**
     * check if position is in proximity of next StopPoint
     **/
    public RouteManager checkStopPointProximity() {

        LatLng latLngRouteItem = new LatLng(
                routeItems.get(0).getStopPoint().getNorthing(),
                routeItems.get(0).getStopPoint().getEasting());

        LatLng latLngPosition = new LatLng(
                currentLocation.getLatitude(),
                currentLocation.getLongitude());

        if (latLngRouteItem.distanceTo(latLngPosition) < 10.0) {
            inProximity = true;
        }

        Log.d(LOG_TAG, "you're "
                + latLngRouteItem.distanceTo(latLngPosition)
                + " meters from stoppoint: "
                + routeItems.get(0).getOrder() + " "
                + routeItems.get(0).getStopPoint().getType());

        return this;
    }

    /**
     * when in proximity of next StopPoint, remove it
     */
    public RouteManager updateStopPointsRemaining() {

        if (inProximity) {

            Log.d(LOG_TAG, "removing stoppoint: "
                    + routeItems.get(0).getOrder() + " "
                    + routeItems.get(0).getStopPoint().getType() + " at: "
                    + routeItems.get(0).getStopPoint().getEasting() + " "
                    + routeItems.get(0).getStopPoint().getNorthing());

            routeItems.remove(0);
            waypoints.remove(0);

            inProximity = false;
        }

        Log.d(LOG_TAG, "next stoppoint: "
                + routeItems.get(0).getOrder() + " "
                + routeItems.get(0).getStopPoint().getType() + " at: "
                + routeItems.get(0).getStopPoint().getEasting() + " "
                + routeItems.get(0).getStopPoint().getNorthing());

        return this;
    }

    /*********************************************************************************************/
    /****                                   Information                                       ****/
    /*********************************************************************************************/
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
        if (currentRoute == null) {
            showMessage("Unable to get location");
        } else if (currentRoute.isOffRoute(target)) {
            showMessage("You are off-route.");
        } else {
            showMessage("You are not off-route.");
        }
    }
}