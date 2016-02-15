package se.jolo.prototypenavigator.model;

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
}
