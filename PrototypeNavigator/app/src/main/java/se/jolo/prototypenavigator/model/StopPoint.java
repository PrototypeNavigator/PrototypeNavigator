package se.jolo.prototypenavigator.model;

/**
 * Created by Joel on 2016-02-08.
 */
public class StopPoint {

    private float easting;
    private float northing;
    private String type;
    private String uuid;
    private String freeText;

    public StopPoint() {}

    public StopPoint(float easting, float northing, String type, String uuid, String freeText) {
        this.easting = easting;
        this.northing = northing;
        this.type = type;
        this.uuid = uuid;
        this.freeText = freeText;
    }

    public float getEasting() {
        return easting;
    }

    public float getNorthing() {
        return northing;
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFreeText() {
        return freeText;
    }
}
