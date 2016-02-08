package se.jolo.prototypenavigator.route.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Joel on 2016-02-08.
 */

@Root
public class Service {

    @Element
    private String agreementArrivalTime;    // should be time

    @Element
    private String agreementDepartureTime;  // should be time

    @Element
    private boolean delivery;

    @Element
    private boolean pickup;

    @Element
    private String products;

    @Element
    private String serviceCode;

    @Element
    private String serviceName;

    public Service() {}

    public String getAgreementArrivalTime() {
        return agreementArrivalTime;
    }

    public String getAgreementDepartureTime() {
        return agreementDepartureTime;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public boolean isPickup() {
        return pickup;
    }

    public String getProducts() {
        return products;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }
}
