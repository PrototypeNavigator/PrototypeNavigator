package se.jolo.prototypenavigator.route.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.UUID;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class StopPointItem {

    @Element
    private UUID uuid;

    @Element
    private String type;

    @Element
    private String name;

    @Element
    private String deliveryAddress;

    @Element
    private int deliveryPostalCode;

    @Element
    private float easting;

    @Element
    private float northing;

    @Element
    private String freeText;

    @Element
    private String plannedArrivalTime;      // should be some sort of time

    @Element
    private String plannedDepartureTime;    // should be some sort of time

    @Element
    private int validityDays;

    @ElementList
    private List<DeliveryPoint> deliveryPoints;

    @Element
    private Service service;

    public StopPointItem() {}

    public UUID getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public int getDeliveryPostalCode() {
        return deliveryPostalCode;
    }

    public float getEasting() {
        return easting;
    }

    public float getNorthing() {
        return northing;
    }

    public String getFreeText() {
        return freeText;
    }

    public String getPlannedArrivalTime() {
        return plannedArrivalTime;
    }

    public String getPlannedDepartureTime() {
        return plannedDepartureTime;
    }

    public int getValidityDays() {
        return validityDays;
    }

    public List<DeliveryPoint> getDeliveryPoints() {
        return deliveryPoints;
    }

    public Service getService() {
        return service;
    }
}
