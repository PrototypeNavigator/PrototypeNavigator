package se.jolo.prototypenavigator.model;

/**
 * Created by Joel on 2016-02-08.
 */
public class DeliveryOffice {

    private String name;
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
