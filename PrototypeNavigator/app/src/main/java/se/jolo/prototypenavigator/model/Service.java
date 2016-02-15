package se.jolo.prototypenavigator.model;

/**
 * Created by Joel on 2016-02-08.
 */
public class Service {

    private String agreementArrivalTime;
    private String agreementDepartureTime;
    private boolean delivery;
    private boolean pickup;
    private String products;
    private String serviceCode;
    private String serviceName;

    public Service() {}

    public Service(String agreementArrivalTime, String agreementDepartureTime, boolean delivery,
                   boolean pickup, String products, String serviceCode, String serviceName) {
        this.agreementArrivalTime = agreementArrivalTime;
        this.agreementDepartureTime = agreementDepartureTime;
        this.delivery = delivery;
        this.pickup = pickup;
        this.products = products;
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
    }

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