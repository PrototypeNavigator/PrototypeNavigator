package se.jolo.prototypenavigator.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
public final class RouteItem implements Comparable<RouteItem>, Parcelable {

    private int order;
    private int primaryStopPointItemUuid;
    private StopPoint stopPoint;
    private List<StopPointItem> stopPointItems;

    public RouteItem() {}

    public RouteItem(int order, int primaryStopPointItemUuid, StopPoint stopPoint,
                     List<StopPointItem> stopPointItems) {
        this.order = order;
        this.primaryStopPointItemUuid = primaryStopPointItemUuid;
        this.stopPoint = stopPoint;
        this.stopPointItems = stopPointItems;
    }

    public int getOrder() {
        return order;
    }

    public int getPrimaryStopPointItemUuid() {
        return primaryStopPointItemUuid;
    }

    public StopPoint getStopPoint() {
        return stopPoint;
    }

    public List<StopPointItem> getStopPointItems() {
        return stopPointItems;
    }


    @Override
    public int compareTo(RouteItem another) {

        if (order > another.order) {
            return 1;
        }
        else if (order <  another.order) {
            return -1;
        }
        else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "RouteItem{" +
                "order=" + order +
                ", primaryStopPointItemUuid=" + primaryStopPointItemUuid +
                ", stopPoint=" + stopPoint +
                ", stopPointItems=" + stopPointItems +
                '}';
    }


    protected RouteItem(Parcel in) {
        order = in.readInt();
        primaryStopPointItemUuid = in.readInt();
        stopPoint = (StopPoint) in.readValue(StopPoint.class.getClassLoader());
        if (in.readByte() == 0x01) {
            stopPointItems = new ArrayList<StopPointItem>();
            in.readList(stopPointItems, StopPointItem.class.getClassLoader());
        } else {
            stopPointItems = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(order);
        dest.writeInt(primaryStopPointItemUuid);
        dest.writeValue(stopPoint);
        if (stopPointItems == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(stopPointItems);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RouteItem> CREATOR = new Parcelable.Creator<RouteItem>() {
        @Override
        public RouteItem createFromParcel(Parcel in) {
            return new RouteItem(in);
        }

        @Override
        public RouteItem[] newArray(int size) {
            return new RouteItem[size];
        }
    };
}