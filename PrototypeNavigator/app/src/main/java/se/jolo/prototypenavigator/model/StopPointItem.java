package se.jolo.prototypenavigator.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
public class StopPointItem implements Parcelable {

    private String uuid;
    private String type;
    private String name;
    private String deliveryAddress;
    private int deliveryPostalCode;
    private float easting;
    private float northing;
    private String freeText;
    private String plannedArrivalTime;      // should be some sort of time
    private String plannedDepartureTime;    // should be some sort of time
    private int validityDays;
    private List<DeliveryPoint> deliveryPoints;
    private Service service = null;

    public StopPointItem() {}

    public StopPointItem(String uuid, String type, String name, String deliveryAddress,
                         int deliveryPostalCode, float easting, float northing, String freeText,
                         String plannedArrivalTime, String plannedDepartureTime,
                         int validityDays,List <DeliveryPoint> deliveryPoints, Service service) {
        this.uuid = uuid;
        this.type = type;
        this.name = name;
        this.deliveryAddress = deliveryAddress;
        this.deliveryPostalCode = deliveryPostalCode;
        this.easting = easting;
        this.northing = northing;
        this.freeText = freeText;
        this.plannedArrivalTime = plannedArrivalTime;
        this.plannedDepartureTime = plannedDepartureTime;
        this.validityDays = validityDays;
        this.deliveryPoints = deliveryPoints;
        this.service = service;
    }

    public String getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public int getDeliveryPostalCode() {
        return deliveryPostalCode;
    }

    public float getEasting() {
        return easting;
    }

    public float getNorthing() {
        return northing;
    }

    public String getFreeText() {
        return freeText;
    }

    public String getPlannedArrivalTime() {
        return plannedArrivalTime;
    }

    public String getPlannedDepartureTime() {
        return plannedDepartureTime;
    }

    public int getValidityDays() {
        return validityDays;
    }

    public List <DeliveryPoint> getDeliveryPoints() {
        return deliveryPoints;
    }

    public Service getService() {
        return service;
    }

    protected StopPointItem(Parcel in) {
        uuid = in.readString();
        type = in.readString();
        name = in.readString();
        deliveryAddress = in.readString();
        deliveryPostalCode = in.readInt();
        easting = in.readFloat();
        northing = in.readFloat();
        freeText = in.readString();
        plannedArrivalTime = in.readString();
        plannedDepartureTime = in.readString();
        validityDays = in.readInt();
        if (in.readByte() == 0x01) {
            deliveryPoints = new ArrayList<DeliveryPoint>();
            in.readList(deliveryPoints, DeliveryPoint.class.getClassLoader());
        } else {
            deliveryPoints = null;
        }
        service = (Service) in.readValue(Service.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(deliveryAddress);
        dest.writeInt(deliveryPostalCode);
        dest.writeFloat(easting);
        dest.writeFloat(northing);
        dest.writeString(freeText);
        dest.writeString(plannedArrivalTime);
        dest.writeString(plannedDepartureTime);
        dest.writeInt(validityDays);
        if (deliveryPoints == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(deliveryPoints);
        }
        dest.writeValue(service);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String newLine = "\n";

        if (uuid!=null){
           // stringBuilder.append(uuid).append(newLine);
        }
        if (type!=null){
           // stringBuilder.append(type).append(newLine);
        }
        if (name!=null){
           // stringBuilder.append(name).append(newLine);
        }
        if (deliveryAddress!=null && !deliveryAddress.equals("")){
            stringBuilder.append("Adress: " + deliveryAddress+" ");
        }
        if (deliveryPostalCode!=0){
            stringBuilder.append(deliveryPostalCode).append(newLine);
        }
        if (freeText!=null && !freeText.equals("")){
            stringBuilder.append("info: " + freeText).append(newLine);
            Log.d("freeText", freeText);
        }
        if (plannedArrivalTime!=null && !plannedArrivalTime.equals("")){
            stringBuilder.append("Ankomst: " + plannedArrivalTime).append(newLine);
        }
        if (plannedDepartureTime!=null && !plannedDepartureTime.equals("")){
            stringBuilder.append("Angång: " + plannedDepartureTime).append(newLine);
        }
        if (validityDays!=0){
            stringBuilder.append("Giltighets dagar: "+ validityDays).append(newLine);
        }
        if (deliveryPoints!=null){
            for (DeliveryPoint d: deliveryPoints){
                List<Resident> residents = d.getResidents();
                stringBuilder.append("Mottagare: ");
                for (Resident r : residents){
                    if(residents.size()>1){
                        stringBuilder.append(r.getLastName()+" "+r.getFirstName()+", ");
                    }else {
                        stringBuilder.append(r.getLastName()+" "+r.getFirstName());
                    }


                }
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StopPointItem> CREATOR = new Parcelable.Creator<StopPointItem>() {
        @Override
        public StopPointItem createFromParcel(Parcel in) {
            return new StopPointItem(in);
        }

        @Override
        public StopPointItem[] newArray(int size) {
            return new StopPointItem[size];
        }
    };
}