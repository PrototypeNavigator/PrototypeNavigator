package se.jolo.prototypenavigator.model;

import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
public final class Route {

    private AuditInfo auditInfo;
    private DeliveryOffice deliveryOffice;
    private int name;
    private String type;
    private String uuid;
    private int validityDays;
    private List<RouteItem> routeItems;

    public Route() {}

    public Route(AuditInfo auditInfo, DeliveryOffice deliveryOffice, int name, String type,
                 String uuid, int validityDays, List<RouteItem> routeItems) {
        this.auditInfo = auditInfo;
        this.deliveryOffice = deliveryOffice;
        this.name = name;
        this.type = type;
        this.uuid = uuid;
        this.validityDays = validityDays;
        this.routeItems = routeItems;
    }

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
}
