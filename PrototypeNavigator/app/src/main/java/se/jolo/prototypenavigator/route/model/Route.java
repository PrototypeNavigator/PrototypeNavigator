package se.jolo.prototypenavigator.route.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public final class Route {

    @Element
    private AuditInfo auditInfo;

    @Element
    private DeliveryOffice deliveryOffice;

    @Element
    private int name;

    @Element
    private String type;

    @Element
    private UUID uuid;

    @Element
    private int validityDays;

    @ElementList
    private Map<Integer, RouteItem> routeItems;

    @ElementList
    private Map<UUID, StopPointItem> stopPointItems;

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

    public UUID getUuid() {
        return uuid;
    }

    public int getValidityDays() {
        return validityDays;
    }

    public Map<Integer, RouteItem> getRouteItems() {
        return routeItems;
    }

    public Map<UUID, StopPointItem> getStopPointItems() {
        return stopPointItems;
    }
}
