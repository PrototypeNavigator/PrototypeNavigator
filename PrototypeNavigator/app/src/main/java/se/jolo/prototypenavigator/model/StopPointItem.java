package se.jolo.prototypenavigator.model;

import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
public class StopPointItem {

    private String uuid;
    private String type;
    private String name;
    private String deliveryAddress;
    private int deliveryPostalCode;
    private float easting;
    private float northing;
    private String freeText;
    private String plannedArrivalTime;
    private String plannedDepartureTime;
    private int validityDays;
    private List<DeliveryPoint> deliveryPoints;
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
