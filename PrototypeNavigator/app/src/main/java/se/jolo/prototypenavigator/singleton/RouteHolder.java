package se.jolo.prototypenavigator.singleton;

import android.content.Context;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.List;

import se.jolo.prototypenavigator.model.Route;

/* The RouteHolder class holds information that is necessary for the app, across multiple activities. */
public enum  RouteHolder {
    INSTANCE;
    private Context context;
    private Route route;
    private List<MarkerOptions> markers;

    RouteHolder(){}

    public void setContext(Context context) {
        this.context = context;
    }
    public Context getContext() {
        return context;
    }
    public void setMarkers(List<MarkerOptions> markers) {
        this.markers = markers;
    }
    public List<MarkerOptions> getMarkers() {
        return markers;
    }
    public void setRoute(Route route){
        this.route=route;
    }
    public Route getRoute(){
        return route;
    }
}
