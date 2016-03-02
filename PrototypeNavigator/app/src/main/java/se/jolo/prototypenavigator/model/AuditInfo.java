package se.jolo.prototypenavigator.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Joel on 2016-02-08.
 */
public final class AuditInfo implements Parcelable {


    private Date createdAt;
    private String createdBy;

    public AuditInfo() {}

    public AuditInfo(Date createdAt, String createdBy) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    protected AuditInfo(Parcel in) {
        long tmpCreatedAt = in.readLong();
        createdAt = tmpCreatedAt != -1 ? new Date(tmpCreatedAt) : null;
        createdBy = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1L);
        dest.writeString(createdBy);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AuditInfo> CREATOR = new Parcelable.Creator<AuditInfo>() {
        @Override
        public AuditInfo createFromParcel(Parcel in) {
            return new AuditInfo(in);
        }

        @Override
        public AuditInfo[] newArray(int size) {
            return new AuditInfo[size];
        }
    };
}