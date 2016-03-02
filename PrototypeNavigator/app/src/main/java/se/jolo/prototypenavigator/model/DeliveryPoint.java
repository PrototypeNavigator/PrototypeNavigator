package se.jolo.prototypenavigator.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
public class DeliveryPoint implements Parcelable {

    private boolean odr;
    private List<OdrRecipient> odrRecipients;
    private List<Resident> residents;

    public DeliveryPoint() {}

    public DeliveryPoint(boolean odr, List<OdrRecipient> odrRecipients, List<Resident> residents) {
        this.odr = odr;
        this.odrRecipients = odrRecipients;
        this.residents = residents;
    }

    public boolean isOdr() {
        return odr;
    }

    public List<OdrRecipient> getOdrRecipients() {
        return odrRecipients;
    }

    public List<Resident> getResidents() {
        return residents;
    }

    protected DeliveryPoint(Parcel in) {
        odr = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            odrRecipients = new ArrayList<OdrRecipient>();
            in.readList(odrRecipients, OdrRecipient.class.getClassLoader());
        } else {
            odrRecipients = null;
        }
        if (in.readByte() == 0x01) {
            residents = new ArrayList<Resident>();
            in.readList(residents, Resident.class.getClassLoader());
        } else {
            residents = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (odr ? 0x01 : 0x00));
        if (odrRecipients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(odrRecipients);
        }
        if (residents == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(residents);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeliveryPoint> CREATOR = new Parcelable.Creator<DeliveryPoint>() {
        @Override
        public DeliveryPoint createFromParcel(Parcel in) {
            return new DeliveryPoint(in);
        }

        @Override
        public DeliveryPoint[] newArray(int size) {
            return new DeliveryPoint[size];
        }
    };
}