package se.jolo.prototypenavigator.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class RouteItem {

    @Element(required = false)
    private int order;

    @Element(required = false)
    private int primaryStopPointItemUuid;

    @Element(required = false)
    private StopPoint stopPoint;

    @ElementList(required = false)
    private List<StopPointItem> stopPointItems;

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

    public List<StopPointItem> getStopPointItems() {
        return stopPointItems;
    }
}
