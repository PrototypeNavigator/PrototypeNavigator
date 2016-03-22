package se.jolo.prototypenavigator.utils;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.directions.DirectionsCriteria;
import com.mapbox.directions.MapboxDirections;
import com.mapbox.directions.service.models.DirectionsResponse;
import com.mapbox.directions.service.models.DirectionsRoute;
import com.mapbox.directions.service.models.RouteStep;
import com.mapbox.directions.service.models.Waypoint;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.GeoConstants;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.jolo.prototypenavigator.model.Instruction;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.task.OsrmJsonTask;

/**
 * Created by Joel on 2016-02-24.
 */
public final class RouteManager {

    private static final String LOG_TAG = "ROUTER";

    private Route route;
    private Context context;

    private List<LatLng> fullRoute;
    private List<RouteItem> routeItems;
    private List<Instruction> instructions;
    private ArrayList<RouteItem> nextStop;

    private boolean inProximity = false;
    private Locator locator;

    private DirectionsRoute currentRoute;
    private PolylineOptions polylineToNextStop;
    private PolylineOptions polylinefullRoute;

    public RouteManager(Context context, Locator locator) {
        this.context = context;
        this.locator = locator;
    }

    /*********************************************************************************************/
    /****                                     Location                                        ****/
    /*********************************************************************************************/

    /**
     * Set new location, set camera to new location, check new locations proximity to next
     * stop-point, updates remaining stop-points, loads route again and removes and re-draws
     * polyline from location to next stop-point.
     */
/*    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        location = (location != null) ? location : locator.getLocation() ;

        mapView.animateCamera(CameraUpdateFactory.newCameraPosition(
                getCameraPosition(new LatLng(location.getLatitude(), location.getLongitude()))));

        setCurrentLocation(location).checkStopPointProximity().updateStopPointsRemaining().loadRouteToNextStop();
        removePolyline(getPolylineToNextStop());

    }*/
    public List<LatLng> getPoints() {
        return polylineToNextStop.getPoints();
    }

    /*********************************************************************************************/
    /****                                     Routing                                         ****/
    /*********************************************************************************************/

    /**
     * Makes a list of waypoints containing current location and next stop-point, to enable
     * drawing a route between the two.
     *
     * @return list of waypoints
     */
   /* public List<LatLng> getCurrentRoute() {

        List<LatLng> positionAndNextWaypoint = new ArrayList<>();

        positionAndNextWaypoint.add(new LatLng(
                currentLocation.getLatitude(),
                currentLocation.getLongitude()));
        positionAndNextWaypoint.add(nextStop.get(0));

        return positionAndNextWaypoint;
    }*/

    /**
     *
     *
     *
     */
    public PolylineOptions createPolylineOption(List<RouteItem> routeItems) {
        HashMap<String, List> stopsAndInstructions = stopsAndInstructions(routeItems);
        if (routeItems.size() <= 1) {
            instructions = stopsAndInstructions.get(OsrmJsonTask.INSTRUCTIONS);
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(stopsAndInstructions.get(OsrmJsonTask.ROUTE_GEOMETRY));
        return polylineOptions;
    }


    public HashMap<String, List> stopsAndInstructions(List<RouteItem> routeItems) {

        OsrmJsonTask osrmJsonTask = new OsrmJsonTask();
        ArrayList<LatLng> routeStops = new ArrayList<>();

        if (routeItems.size() <= 1) {
            routeStops.add(new LatLng(locator.getLocation().getLatitude(), locator.getLocation().getLongitude()));
            routeStops.add(new LatLng(routeItems.get(0).getStopPoint().getNorthing(), routeItems.get(0).getStopPoint().getEasting()));
        } else {
            for (RouteItem routeItem : routeItems) {
                routeStops.add(new LatLng(routeItem.getStopPoint().getNorthing(), routeItem.getStopPoint().getEasting()));
            }
        }

        osrmJsonTask.execute(UrlBuilderRoute.multiplePoints(routeStops));

        try {
            return osrmJsonTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*********************************************************************************************/
    /****                                    RoutItems                                        ****/
    /*********************************************************************************************/

    /**
     * Loads waypoints and route-items from parent Route object.
     *
     * @param route object containing the route-items
     * @return self for fluidity
     */
    public RouteManager loadRouteItemsAndWaypoints(Route route) {

        routeItems = new ArrayList<>();
        nextStop = new ArrayList<>();
        nextStop.add(route.getRouteItems().get(0));

        fullRoute = new ArrayList<>();

        routeItems = route.getRouteItems();

        for (RouteItem ri : routeItems) {
            fullRoute.add(new LatLng(
                    ri.getStopPoint().getNorthing(),
                    ri.getStopPoint().getEasting()));
        }

        return this;
    }

    public RouteManager loadPolylines() {
        polylineToNextStop = createPolylineOption(nextStop).width(5).color(Color.GREEN);
        polylinefullRoute = createPolylineOption(routeItems).width(5).color(Color.BLUE);
        return this;
    }

    public RouteManager loadPolylineNextStop(){
        polylineToNextStop = createPolylineOption(nextStop).width(5).color(Color.GREEN);
        return this;
    }


    /**
     * Check if current location is in proximity of next StopPoint.
     *
     * @return self for fluidity
     */
    public RouteManager checkStopPointProximity() {

        LatLng latLngRouteItem = new LatLng(
                routeItems.get(0).getStopPoint().getNorthing(),
                routeItems.get(0).getStopPoint().getEasting());

        LatLng latLngPosition = new LatLng(
                locator.getLocation().getLatitude(),
                locator.getLocation().getLongitude());

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
     * If in proximity of next StopPoint, remove it to get next coming StopPoint.
     *
     * @return self for fluidity
     */
    public RouteManager updateStopPointsRemaining() {

        if (inProximity) {

            Log.d(LOG_TAG, "removing stoppoint: "
                    + routeItems.get(0).getOrder() + " "
                    + routeItems.get(0).getStopPoint().getType() + " at: "
                    + routeItems.get(0).getStopPoint().getEasting() + " "
                    + routeItems.get(0).getStopPoint().getNorthing());

            instructions.remove(0);
            routeItems.remove(0);
            nextStop.remove(0);

            inProximity = false;
        }

        Log.d(LOG_TAG, "next stoppoint: "
                + routeItems.get(0).getOrder() + " "
                + routeItems.get(0).getStopPoint().getType() + " at: "
                + routeItems.get(0).getStopPoint().getEasting() + " "
                + routeItems.get(0).getStopPoint().getNorthing());

        return this;
    }

    /**
     * @return next stop in route
     */
    public RouteItem getNextStop() {
        return routeItems.get(0);
    }

    public Instruction getInstruction() {
        return instructions.get(0);
    }

    public PolylineOptions getPolylinefullRoute() {
        return polylinefullRoute;
    }

    public PolylineOptions getPolylineToNextStop() {
        return polylineToNextStop;
    }

    public List<RouteItem> getRouteItems() {
        return routeItems;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    /*********************************************************************************************/
    /****                                  Info & Other                                       ****/
    /*********************************************************************************************/

    /**
     * Sets camera position to device bearing, if unable to get bearing set it to north.
     * Sets tilt and zoom.
     *
     * @param latLng current location
     * @return returns newly set CameraPosition
     */
    public CameraPosition getCameraPosition(LatLng latLng) {
        return new CameraPosition.Builder()
                .bearing((instructions != null) ? (float) instructions.get(0).getPostTurnAzimuth() : 0.0f)
                .target(latLng)
                .tilt(80f)
                .zoom(15f)
                .build();
    }

    /**
     * Check in target Waypoint is on rout.
     *
     * @param target to be checked
     */
    public void checkOffRoute(Waypoint target) {
        if (currentRoute == null) {
            showMessage("Unable to get location");
        } else if (currentRoute.isOffRoute(target)) {
            showMessage("You are off-route.");
        } else {
            showMessage("You are not off-route.");
        }
    }

    /**
     * Simple toaster.
     *
     * @param message toast text
     */
    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}