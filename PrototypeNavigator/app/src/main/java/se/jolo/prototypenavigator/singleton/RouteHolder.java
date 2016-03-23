package se.jolo.prototypenavigator.singleton;

import se.jolo.prototypenavigator.model.Route;

/**
 * Created by Holstad on 23/03/16.
 */
public enum  RouteHolder {
    INSTANCE;
    private Route route;
    private RouteHolder(){}

    public void setRoute(Route route){
        this.route=route;
    }

    public Route getRoute(){
        return route;
    }
}
