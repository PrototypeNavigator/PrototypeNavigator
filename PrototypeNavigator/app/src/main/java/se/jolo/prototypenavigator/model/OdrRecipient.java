package se.jolo.prototypenavigator.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class OdrRecipient {

    @Element(required = false)
    private int amount;

    @Element(required = false)
    private String type;

    public OdrRecipient() {}

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }
}
