package se.jolo.prototypenavigator.route.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * Created by Joel on 2016-02-08.
 */
@Root
public final class AuditInfo {

    @Element(required = false)
    private Date createdAt;

    @Element(required = false)
    private String createdBy;

    public AuditInfo() {}

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
