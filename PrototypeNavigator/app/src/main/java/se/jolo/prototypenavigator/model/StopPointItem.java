package se.jolo.prototypenavigator.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class StopPointItem {

    @Element(required = false)
    private String uuid;

    @Element(required = false)
    private String type;

    @Element(required = false)
    private String name;

    @Element(required = false)
    private String deliveryAddress;

    @Element(required = false)
    private int deliveryPostalCode;

    @Element(required = false)
    private float easting;

    @Element(required = false)
    private float northing;

    @Element(required = false)
    private String freeText;

    @Element(required = false)
    private String plannedArrivalTime;      // should be some sort of time

    @Element(required = false)
    private String plannedDepartureTime;    // should be some sort of time

    @Element(required = false)
    private int validityDays;

    @ElementList(required = false)
    private List<DeliveryPoint> deliveryPoints;

    @Element(required = false)
    private Service service = null;

    public StopPointItem() {}

    public StopPointItem(String uuid, String type, String name, String deliveryAddress,
                         int deliveryPostalCode, float easting, float northing, String freeText,
                         String plannedArrivalTime, String plannedDepartureTime,
                         int validityDays,List <DeliveryPoint> deliveryPoint, Service service) {
        this.uuid = uuid;
        this.type = type;
        this.name = name;
        this.deliveryAddress = deliveryAddress;
        this.deliveryPostalCode = deliveryPostalCode;
        this.easting = easting;
        this.northing = northing;
        this.freeText = freeText;
        this.plannedArrivalTime = plannedArrivalTime;
        this.plannedDepartureTime = plannedDepartureTime;
        this.validityDays = validityDays;
        this.deliveryPoints = deliveryPoints;
        this.service = service;
    }

    public String getUuid() {
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

    public List <DeliveryPoint> getDeliveryPoints() {
        return deliveryPoints;
    }

    public Service getService() {
        return service;
    }
}