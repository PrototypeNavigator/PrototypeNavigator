package se.jolo.prototypenavigator.route.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.UUID;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class StopPoint {

    @Element
    private float easting;

    @Element
    private float northing;

    @Element
    private String type;

    @Element
    private UUID uuid;

    @Element
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

    public UUID getUuid() {
        return uuid;
    }

    public String getFreeText() {
        return freeText;
    }
}
