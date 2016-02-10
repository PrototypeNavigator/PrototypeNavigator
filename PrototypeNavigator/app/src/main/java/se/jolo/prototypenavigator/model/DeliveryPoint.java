package se.jolo.prototypenavigator.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class DeliveryPoint {

    @Element(required = false)
    private boolean odr;

    @ElementList(required = false)
    private List<OdrRecipient> odrRecipients;

    @ElementList(required = false)
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
