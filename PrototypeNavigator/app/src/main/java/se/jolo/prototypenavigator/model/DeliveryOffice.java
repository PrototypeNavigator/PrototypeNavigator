package se.jolo.prototypenavigator.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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

    public DeliveryOffice(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }
}
