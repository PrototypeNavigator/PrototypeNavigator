package se.jolo.prototypenavigator.model;

import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
public class DeliveryPoint {

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
}
