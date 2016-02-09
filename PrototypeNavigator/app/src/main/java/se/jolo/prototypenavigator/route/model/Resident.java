package se.jolo.prototypenavigator.route.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public class Resident {

    @Element(required = false)
    private String firstname;

    @Element(required = false)
    private String lastname;

    public Resident() {}

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }
}
