package se.jolo.prototypenavigator.singleton;

import se.jolo.prototypenavigator.model.Route;

/* The RouteHolder class holds information that is necessary for the app, across multiple activities. */
public enum  RouteHolder {
    INSTANCE;
    private Route route;

    RouteHolder(){}

    public void setRoute(Route route){
        this.route=route;
    }
    public Route getRoute(){
        return route;
    }

}
