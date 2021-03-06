package se.jolo.prototypenavigator.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OdrRecipient implements Parcelable {

    private int amount = 0;

    @Override
    public String toString() {
        return "OdrRecipient{" +
                "amount=" + amount +
                ", type='" + type + '\'' +
                '}';
    }

    private String type = "";

    public OdrRecipient() {}

    public OdrRecipient(int amount, String type) {
        this.amount = amount;
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    protected OdrRecipient(Parcel in) {
        amount = in.readInt();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(amount);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OdrRecipient> CREATOR = new Parcelable.Creator<OdrRecipient>() {
        @Override
        public OdrRecipient createFromParcel(Parcel in) {
            return new OdrRecipient(in);
        }

        @Override
        public OdrRecipient[] newArray(int size) {
            return new OdrRecipient[size];
        }
    };
}