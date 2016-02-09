package se.jolo.prototypenavigator.route.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.UUID;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class DeliveryOffice {

    @Element(required = false)
    private String name;

    @Element(required = false)
    private String uuid;

    public DeliveryOffice() {}

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }
}
