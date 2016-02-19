package se.jolo.prototypenavigator.model;

import java.util.Date;

/**
 * Created by Joel on 2016-02-08.
 */
public class AuditInfo {

    private Date createdAt;
    private String createdBy;

    public AuditInfo() {}

    public AuditInfo(Date createdAt, String createdBy) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
