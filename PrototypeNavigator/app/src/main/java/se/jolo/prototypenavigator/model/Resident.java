package se.jolo.prototypenavigator.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Joel on 2016-02-08.
 */
public class Resident implements Parcelable {

    private String firstname;
    private String lastname;

    public Resident() {}

    public Resident(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    protected Resident(Parcel in) {
        firstname = in.readString();
        lastname = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstname);
        dest.writeString(lastname);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Resident> CREATOR = new Parcelable.Creator<Resident>() {
        @Override
        public Resident createFromParcel(Parcel in) {
            return new Resident(in);
        }

        @Override
        public Resident[] newArray(int size) {
            return new Resident[size];
        }
    };
}