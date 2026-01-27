package gov.cms.smart.models;

public abstract class BasePackage {
    protected String packageId;

    protected String state;
    protected String authority; // SPA: Medicaid SPA | CHIP SPA; Waiver: 1915(b) | 1915(c)
    protected String status;    // "", Approved, etc.

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public abstract void generatePackageId(); // unused (IDs come from Excel)
}
