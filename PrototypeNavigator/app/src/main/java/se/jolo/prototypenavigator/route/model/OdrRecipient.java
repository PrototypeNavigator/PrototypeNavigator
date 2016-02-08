package se.jolo.prototypenavigator.route.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class OdrRecipient {

    @Element
    private int amount;

    @Element
    private String type;

    public OdrRecipient() {}

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }
}
