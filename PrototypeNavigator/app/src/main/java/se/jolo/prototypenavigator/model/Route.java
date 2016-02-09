package se.jolo.prototypenavigator.model;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Joel on 2016-02-08.
 */
public final class Route {

    private AuditInfo auditInfo;
    private DeliveryOffice deliveryOffice;
    private int name;
    private String type;
    private UUID uuid;
    private int validityDays;
    private Map<Integer, RouteItem> routeItems;
    private Map<UUID, StopPointItem> stopPointItems;

}
