package se.jolo.prototypenavigator.model;

import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
public final class RouteItem implements Comparable<RouteItem> {

    private int order;
    private int primaryStopPointItemUuid;
    private StopPoint stopPoint;
    private List<StopPointItem> stopPointItems;

    public RouteItem() {}

    public RouteItem(int order, int primaryStopPointItemUuid, StopPoint stopPoint,
                     List<StopPointItem> stopPointItems) {
        this.order = order;
        this.primaryStopPointItemUuid = primaryStopPointItemUuid;
        this.stopPoint = stopPoint;
        this.stopPointItems = stopPointItems;
    }

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


    @Override
    public int compareTo(RouteItem another) {

        if (order > another.order) {
            return 1;
        }
        else if (order <  another.order) {
            return -1;
        }
        else {
            return 0;
        }
    }

}
