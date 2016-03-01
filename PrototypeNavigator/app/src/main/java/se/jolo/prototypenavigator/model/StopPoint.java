package se.jolo.prototypenavigator.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Joel on 2016-02-08.
 */
public class StopPoint implements Parcelable {

    private float easting;
    private float northing;
    private String type;
    private String uuid;
    private String freeText;

    public StopPoint() {}

    public StopPoint(float easting, float northing, String type, String uuid, String freeText) {
        this.easting = easting;
        this.northing = northing;
        this.type = type;
        this.uuid = uuid;
        this.freeText = freeText;
    }

    public float getEasting() {
        return easting;
    }

    public float getNorthing() {
        return northing;
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFreeText() {
        return freeText;
    }

    protected StopPoint(Parcel in) {
        easting = in.readFloat();
        northing = in.readFloat();
        type = in.readString();
        uuid = in.readString();
        freeText = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(easting);
        dest.writeFloat(northing);
        dest.writeString(type);
        dest.writeString(uuid);
        dest.writeString(freeText);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StopPoint> CREATOR = new Parcelable.Creator<StopPoint>() {
        @Override
        public StopPoint createFromParcel(Parcel in) {
            return new StopPoint(in);
        }

        @Override
        public StopPoint[] newArray(int size) {
            return new StopPoint[size];
        }
    };
}