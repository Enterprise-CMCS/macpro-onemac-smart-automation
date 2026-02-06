package gov.cms.smart.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class BasePackage {

    protected String packageId;

    protected String state;
    protected String authority; // SPA: Medicaid SPA | CHIP SPA; Waiver: 1915(b) | 1915(c)
    protected String status;    // "", Approved, etc.
    protected PackageDetails details;

    public abstract void generatePackageId(); // unused (IDs come from Excel)
}
