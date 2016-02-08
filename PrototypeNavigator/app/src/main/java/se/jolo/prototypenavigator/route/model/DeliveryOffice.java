package se.jolo.prototypenavigator.route.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.UUID;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class DeliveryOffice {

    @Element
    private String name;

    @Element
    private UUID uuid;

    public DeliveryOffice() {}

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }
}
