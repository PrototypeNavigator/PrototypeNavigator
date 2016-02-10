package se.jolo.prototypenavigator.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class StopPoint {

    @Element(required = false)
    private float easting;

    @Element(required = false)
    private float northing;

    @Element(required = false)
    private String type;

    @Element(required = false)
    private String uuid;

    @Element(required = false)
    private String freeText;

    public StopPoint() {}

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
