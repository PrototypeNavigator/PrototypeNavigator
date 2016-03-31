package se.jolo.prototypenavigator.singleton;

import se.jolo.prototypenavigator.model.Route;

/**
 * The RouteHolder class holds information that is necessary for the app, across multiple activities.
 */
public enum  RouteHolder {
    INSTANCE;
    private Route route;
    private boolean isLocationEnabled;

    private RouteHolder(){}

    public void setRoute(Route route){
        this.route=route;
    }
    public Route getRoute(){
        return route;
    }

    public boolean isLocationEnabled() {
        return isLocationEnabled;
    }
    public void setIsLocationEnabled(boolean isLocationEnabled) {
        this.isLocationEnabled = isLocationEnabled;
    }

}
