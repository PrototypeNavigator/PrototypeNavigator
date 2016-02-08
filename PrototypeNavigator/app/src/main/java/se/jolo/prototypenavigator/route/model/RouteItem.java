package se.jolo.prototypenavigator.route.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class RouteItem {

    @Element
    private int order;

    @Element
    private int primaryStopPointItemUuid;

    @Element
    private StopPoint stopPoint;

    public RouteItem() {}

    public int getOrder() {
        return order;
    }

    public int getPrimaryStopPointItemUuid() {
        return primaryStopPointItemUuid;
    }

    public StopPoint getStopPoint() {
        return stopPoint;
    }
}
