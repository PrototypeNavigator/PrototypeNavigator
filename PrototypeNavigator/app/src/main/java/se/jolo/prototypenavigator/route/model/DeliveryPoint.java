package se.jolo.prototypenavigator.route.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class DeliveryPoint {

    @Element
    private boolean odr;

    @ElementList
    private List<OdrRecipient> odrRecipients;

    @ElementList
    private List<Resident> residents;

    public DeliveryPoint() {}

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
