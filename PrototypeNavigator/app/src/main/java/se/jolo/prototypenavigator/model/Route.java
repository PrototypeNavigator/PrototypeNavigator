package se.jolo.prototypenavigator.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public final class Route {

    @Element(required = false)
    private AuditInfo auditInfo;

    @Element(required = false)
    private DeliveryOffice deliveryOffice;

    @Element(required = false)
    private int name;

    @Element(required = false)
    private String type;

    @Element(required = false)
    private String uuid;

    @Element(required = false)
    private int validityDays;

    @ElementList(required = false)
    private List<RouteItem> routeItems;

    @ElementList(required = false)
    private List<StopPointItem> stopPointItems;

    public Route() {}

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public DeliveryOffice getDeliveryOffice() {
        return deliveryOffice;
    }

    public int getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public int getValidityDays() {
        return validityDays;
    }

    public List<RouteItem> getRouteItems() {
        return routeItems;
    }

    public List<StopPointItem> getStopPointItems() {
        return stopPointItems;
    }
}
