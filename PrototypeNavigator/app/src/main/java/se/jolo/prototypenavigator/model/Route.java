package se.jolo.prototypenavigator.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
public final class Route implements Parcelable {

    private AuditInfo auditInfo;
    private DeliveryOffice deliveryOffice;
    private int name;
    private String type;
    private String uuid;
    private int validityDays;
    private List<RouteItem> routeItems;

    public Route() {}

    public Route(AuditInfo auditInfo, DeliveryOffice deliveryOffice, int name, String type,
                 String uuid, int validityDays, List<RouteItem> routeItems) {
        this.auditInfo = auditInfo;
        this.deliveryOffice = deliveryOffice;
        this.name = name;
        this.type = type;
        this.uuid = uuid;
        this.validityDays = validityDays;
        this.routeItems = routeItems;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public DeliveryOffice getDeliveryOffice() {
        return deliveryOffice;
    }

    public int getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public int getValidityDays() {
        return validityDays;
    }

    public List<RouteItem> getRouteItems() {
        return routeItems;
    }

    protected Route(Parcel in) {
        auditInfo = (AuditInfo) in.readValue(AuditInfo.class.getClassLoader());
        deliveryOffice = (DeliveryOffice) in.readValue(DeliveryOffice.class.getClassLoader());
        name = in.readInt();
        type = in.readString();
        uuid = in.readString();
        validityDays = in.readInt();
        if (in.readByte() == 0x01) {
            routeItems = new ArrayList<RouteItem>();
            in.readList(routeItems, RouteItem.class.getClassLoader());
        } else {
            routeItems = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(auditInfo);
        dest.writeValue(deliveryOffice);
        dest.writeInt(name);
        dest.writeString(type);
        dest.writeString(uuid);
        dest.writeInt(validityDays);
        if (routeItems == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(routeItems);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };
}