package se.jolo.prototypenavigator.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class Service implements Parcelable {

    private String agreementArrivalTime;    // should be time
    private String agreementDepartureTime;  // should be time
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

    protected Service(Parcel in) {
        agreementArrivalTime = in.readString();
        agreementDepartureTime = in.readString();
        delivery = in.readByte() != 0x00;
        pickup = in.readByte() != 0x00;
        products = in.readString();
        serviceCode = in.readString();
        serviceName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(agreementArrivalTime);
        dest.writeString(agreementDepartureTime);
        dest.writeByte((byte) (delivery ? 0x01 : 0x00));
        dest.writeByte((byte) (pickup ? 0x01 : 0x00));
        dest.writeString(products);
        dest.writeString(serviceCode);
        dest.writeString(serviceName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    @Override
    public String toString() {
        return "Service{" +
                "agreementArrivalTime='" + agreementArrivalTime + '\'' +
                ", agreementDepartureTime='" + agreementDepartureTime + '\'' +
                ", delivery=" + delivery +
                ", pickup=" + pickup +
                ", products='" + products + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}