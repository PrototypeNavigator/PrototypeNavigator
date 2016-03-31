package se.jolo.prototypenavigator.singleton;

import com.mapbox.mapboxsdk.views.MapView;

import se.jolo.prototypenavigator.model.Route;

/**
 * The RouteHolder class holds information that is necessary for the app, across multiple activities.
 */
public enum  RouteHolder {
    INSTANCE;
    private Route route;
    private MapView mapView;

    private RouteHolder(){}

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }
    public MapView getMapView() {
        return mapView;
    }
    public void setRoute(Route route){
        this.route=route;
    }
    public Route getRoute(){
        return route;
    }

}
