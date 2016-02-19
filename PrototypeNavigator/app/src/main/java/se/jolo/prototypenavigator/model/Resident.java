package se.jolo.prototypenavigator.model;

/**
 * Created by Joel on 2016-02-08.
 */
public class Resident {

    private String firstname;
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
