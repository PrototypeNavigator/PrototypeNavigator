package se.jolo.prototypenavigator.model;

import android.os.Parcel;
import android.os.Parcelable;

import se.jolo.prototypenavigator.utils.InstructionTranslator;

public final class Instruction implements Parcelable {

    private String instructionCode;
    private String streetName;
    private int length;
    private int routeGeometryListIndex;
    private float timeSec;
    private String formatedLength;
    private String postTurnDirection;
    private float postTurnAzimuth;
    private String mode;


    /**********************************************************************************************/
    /**********************                Constructor                        *********************/
    /**********************************************************************************************/

    public Instruction(String instructionCode, String streetName, int length, int routeGeometryListIndex,
                       float timeSec, String formatedLength, String postTurnDirection, float postTurnAzimuth, String mode) {
        this.instructionCode = instructionCode;
        this.streetName = streetName;
        this.length = length;
        this.routeGeometryListIndex = routeGeometryListIndex;
        this.timeSec = timeSec;
        this.formatedLength = formatedLength;
        this.postTurnDirection = postTurnDirection;
        this.postTurnAzimuth = postTurnAzimuth;
        this.mode = mode;
    }


    /**********************************************************************************************/
    /**********************                    Getters                        *********************/
    /**********************************************************************************************/

    public String getInstructionCode() {
        return instructionCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getLength() {
        return length;
    }

    public int getRouteGeometryListIndex() {
        return routeGeometryListIndex;
    }

    public float getTimeSec() {
        return timeSec;
    }

    public String getFormatedLength() {
        return formatedLength;
    }

    public String getPostTurnDirection() {
        return postTurnDirection;
    }

    public float getPostTurnAzimuth() {
        return postTurnAzimuth;
    }

    public String getMode() {
        return mode;
    }

    public String getReadableInstruction(){
        InstructionTranslator instructionTranslator = new InstructionTranslator(instructionCode,streetName);
        return instructionTranslator.getInstruction();
    }

    /**********************************************************************************************/
    /**********************                  toString                         *********************/
    /**********************************************************************************************/

    @Override
    public String toString() {
        return "Instruction{" +
                "instructionCode='" + instructionCode + '\'' +
                ", streetName='" + streetName + '\'' +
                ", length=" + length +
                ", routeGeometryListIndex=" + routeGeometryListIndex +
                ", timeSec=" + timeSec +
                ", formatedLength='" + formatedLength + '\'' +
                ", postTurnDirection='" + postTurnDirection + '\'' +
                ", postTurnAzimuth=" + postTurnAzimuth +
                ", mode='" + mode + '\'' +
                '}';
    }

    /**********************************************************************************************/
    /**********************                  Parcelable                       *********************/
    /**********************************************************************************************/

    protected Instruction(Parcel in) {
        instructionCode = in.readString();
        streetName = in.readString();
        length = in.readInt();
        routeGeometryListIndex = in.readInt();
        timeSec = in.readFloat();
        formatedLength = in.readString();
        postTurnDirection = in.readString();
        postTurnAzimuth = in.readFloat();
        mode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(instructionCode);
        dest.writeString(streetName);
        dest.writeInt(length);
        dest.writeInt(routeGeometryListIndex);
        dest.writeFloat(timeSec);
        dest.writeString(formatedLength);
        dest.writeString(postTurnDirection);
        dest.writeFloat(postTurnAzimuth);
        dest.writeString(mode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Instruction> CREATOR = new Parcelable.Creator<Instruction>() {
        @Override
        public Instruction createFromParcel(Parcel in) {
            return new Instruction(in);
        }

        @Override
        public Instruction[] newArray(int size) {
            return new Instruction[size];
        }
    };
}