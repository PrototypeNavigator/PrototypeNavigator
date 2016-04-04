package se.jolo.prototypenavigator.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.directions.service.models.DirectionsRoute;
import com.mapbox.directions.service.models.Waypoint;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.jolo.prototypenavigator.model.Instruction;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.task.OsrmCall;
/* The RouteManager class manages polylines and instructions. */
public final class RouteManager {

    private static final String LOG_TAG = "ROUTER";

    private Context context;

    private List<LatLng> fullRoute;
    private List<RouteItem> routeItems;
    private List<Instruction> instructions;
    private ArrayList<RouteItem> nextStop;

    private boolean inProximity = false;
    private Locator locator;

    private PolylineOptions polylineToNextStop, polylinefullRoute;

    public RouteManager(Context context, Locator locator) {
        this.context = context;
        this.locator = locator;
    }

    public List<LatLng> getPoints() {
        return polylineToNextStop.getPoints();
    }

    /*********************************************************************************************/
    /****                                     Routing                                         ****/
    /*********************************************************************************************/

    public PolylineOptions createPolylineOption(List<RouteItem> routeItems) {
        HashMap<String, List> stopsAndInstructions = stopsAndInstructions(routeItems);
        if (routeItems.size() <= 1) {
            instructions = stopsAndInstructions.get(OsrmCall.INSTRUCTIONS);
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(stopsAndInstructions.get(OsrmCall.ROUTE_GEOMETRY));
        return polylineOptions;
    }

    public HashMap<String, List> stopsAndInstructions(List<RouteItem> routeItems) {

        OsrmCall osrmCall = new OsrmCall();
        ArrayList<LatLng> routeStops = new ArrayList<>();

        if (routeItems.size() <= 1) {
            routeStops.add(new LatLng(locator.getLocation().getLatitude(), locator.getLocation().getLongitude()));
            routeStops.add(new LatLng(routeItems.get(0).getStopPoint().getNorthing(), routeItems.get(0).getStopPoint().getEasting()));
        } else {
            for (RouteItem routeItem : routeItems) {
                routeStops.add(new LatLng(routeItem.getStopPoint().getNorthing(), routeItem.getStopPoint().getEasting()));
            }
        }

        osrmCall.execute(UrlBuilderRoute.multiplePoints(routeStops));

        try {
            return osrmCall.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*********************************************************************************************/
    /****                                    RoutItems                                        ****/
    /*********************************************************************************************/

    /* Loads waypoints and route-items from parent Route object. */
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

    /* Check if current location is in proximity of next StopPoint. */
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

    /* If in proximity of next StopPoint, remove it to get next coming StopPoint. */
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

    /*********************************************************************************************/
    /****                                  Info & Other                                       ****/
    /*********************************************************************************************/

    /* Simple toaster. */
    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /*********************************************************************************************/
    /****                                      Getters                                        ****/
    /*********************************************************************************************/

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

}